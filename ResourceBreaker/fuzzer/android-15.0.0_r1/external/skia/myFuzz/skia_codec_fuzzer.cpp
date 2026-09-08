#include "include/core/SkStream.h"
#include "include/core/SkData.h"
#include "include/codec/SkCodec.h"
#include "include/codec/SkAndroidCodec.h"
#include "include/core/SkBitmap.h"
#include "include/core/SkImageInfo.h"
#include <memory>
#include <stdio.h>



// 保留原来的 fuzzer 逻辑为独立函数
static int fuzz_one_input(const uint8_t* data, size_t size) {
    if (size < 4) return 0;

    auto skdata = SkData::MakeWithoutCopy(data, size);
    if (!skdata) return 0;

    auto stream = std::make_unique<SkMemoryStream>(skdata);
    auto codec = SkCodec::MakeFromStream(std::move(stream));
    if (!codec) {
        fprintf(stderr, "no codec\n");
        return 0;
    }

    SkImageInfo info = codec->getInfo()
                               .makeColorType(kN32_SkColorType)
                               .makeAlphaType(kPremul_SkAlphaType);

    if (info.width() <= 0 || info.height() <= 0 ||
        info.width() > 16384 || info.height() > 16384) {
        fprintf(stderr, "width too small\n");
        return 0;
    }

    SkBitmap bitmap;
    if (!bitmap.tryAllocPixels(info)) {
        fprintf(stderr, "cannot tryAllocPixels\n");
        return 0;
    }

    codec->getPixels(info, bitmap.getPixels(), bitmap.rowBytes());
    fprintf(stderr, "all good!\n");

    return 0;
}

// libFuzzer 接口（可选保留）
extern "C" int LLVMFuzzerTestOneInput(const uint8_t* data, size_t size) {
    return fuzz_one_input(data, size);
}

// 主函数 - 用于 AFL 和独立运行
int main(int argc, char **argv) {
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <input_file>\n", argv[0]);
        return 1;
    }

    FILE *f = fopen(argv[1], "rb");
    if (!f) {
        perror("fopen");
        return 1;
    }

    fseek(f, 0, SEEK_END);
    long file_size = ftell(f);
    fseek(f, 0, SEEK_SET);

    if (file_size <= 0 || file_size > 10 * 1024 * 1024) {
        fclose(f);
        return 0;
    }

    uint8_t *buffer = (uint8_t*)malloc(file_size);
    if (!buffer) {
        fclose(f);
        return 1;
    }

    fread(buffer, 1, file_size, f);
    fclose(f);

    int result = fuzz_one_input(buffer, file_size);

    free(buffer);
    return result;
}

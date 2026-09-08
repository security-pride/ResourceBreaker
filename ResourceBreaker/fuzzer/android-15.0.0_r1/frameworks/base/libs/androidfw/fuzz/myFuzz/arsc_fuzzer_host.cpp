#include <androidfw/ResourceTypes.h>
#include <utils/String8.h>
#include <utils/String16.h>

#include <cstdio>
#include <cstdint>
#include <cstdlib>
#include <cstring>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>

using namespace android;

// 深度解析 ResXMLTree，触发更多代码路径
void fuzzResXMLTree(const uint8_t* data, size_t size) {
    ResXMLTree tree;
    
    // 尝试解析
    status_t err = tree.setTo(data, size, false);
    if (err != NO_ERROR) {
        return;
    }

    // 获取字符串池信息
    const ResStringPool& strPool = tree.getStrings();
    size_t strCount = strPool.size();
    
    // 遍历字符串池
    for (size_t i = 0; i < strCount && i < 1000; i++) {
        auto str16 = strPool.stringAt(i);
        auto str8 = strPool.string8At(i);
        (void)str16;
        (void)str8;
    }

    // 遍历 XML 节点
    ResXMLParser::event_code_t event;
    int nodeCount = 0;
    
    while ((event = tree.next()) != ResXMLParser::END_DOCUMENT && 
           event != ResXMLParser::BAD_DOCUMENT &&
           nodeCount++ < 10000) {
        
        switch (event) {
            case ResXMLParser::START_NAMESPACE:
            case ResXMLParser::END_NAMESPACE: {
                size_t len;
                tree.getNamespacePrefix(&len);
                tree.getNamespaceUri(&len);
                tree.getNamespacePrefixID();
                tree.getNamespaceUriID();
                break;
            }
            
            case ResXMLParser::START_TAG: {
                size_t len;
                
                // 获取元素信息
                tree.getElementName(&len);
                tree.getElementNamespace(&len);
                tree.getElementNameID();
                tree.getElementNamespaceID();
                tree.getLineNumber();
                tree.getComment(&len);
                
                // 遍历所有属性
                size_t attrCount = tree.getAttributeCount();
                for (size_t i = 0; i < attrCount && i < 500; i++) {
                    // 属性名
                    tree.getAttributeName(i, &len);
                    tree.getAttributeName8(i, &len);
                    tree.getAttributeNameID(i);
                    
                    // 属性命名空间
                    tree.getAttributeNamespace(i, &len);
                    tree.getAttributeNamespace8(i, &len);
                    tree.getAttributeNamespaceID(i);
                    
                    // 属性值
                    tree.getAttributeValueStringID(i);
                    tree.getAttributeStringValue(i, &len);
                    tree.getAttributeDataType(i);
                    tree.getAttributeData(i);
                    
                    // 资源 ID
                    tree.getAttributeNameResID(i);
                    
                    // Res_value
                    Res_value value;
                    tree.getAttributeValue(i, &value);
                }
                
                // 特殊属性索引
                tree.indexOfID();
                tree.indexOfClass();
                tree.indexOfStyle();
                
                // 查找特定属性
                tree.indexOfAttribute(NULL, "name");
                tree.indexOfAttribute("http://schemas.android.com/apk/res/android", "name");
                
                break;
            }
            
            case ResXMLParser::END_TAG: {
                size_t len;
                tree.getElementName(&len);
                tree.getElementNamespace(&len);
                break;
            }
            
            case ResXMLParser::TEXT: {
                size_t len;
                tree.getText(&len);
                tree.getTextID();
                
                Res_value value;
                tree.getTextValue(&value);
                break;
            }
            
            default:
                break;
        }
    }
}

// AFL 持久模式支持
#ifdef __AFL_HAVE_MANUAL_CONTROL
__AFL_FUZZ_INIT();
#endif

// 从文件读取数据
bool readFile(const char* path, uint8_t** outData, size_t* outSize) {
    int fd = open(path, O_RDONLY);
    if (fd < 0) return false;

    struct stat st;
    if (fstat(fd, &st) != 0) {
        close(fd);
        return false;
    }

    size_t fileSize = st.st_size;
    uint8_t* buffer = (uint8_t*)malloc(fileSize);
    if (!buffer) {
        close(fd);
        return false;
    }

    ssize_t bytesRead = read(fd, buffer, fileSize);
    close(fd);

    if (bytesRead != (ssize_t)fileSize) {
        free(buffer);
        return false;
    }

    *outData = buffer;
    *outSize = fileSize;
    return true;
}

// Fuzzing 入口点
extern "C" int LLVMFuzzerTestOneInput(const uint8_t* data, size_t size) {
    // 最小有效 AXML 大小
    if (size < sizeof(ResXMLTree_header)) {
        return 0;
    }
    
    fuzzResXMLTree(data, size);
    return 0;
}

#ifndef __AFL_HAVE_MANUAL_CONTROL
// 命令行模式
int main(int argc, char** argv) {
    if (argc < 2) {
        printf("Usage: %s <axml_file>\n", argv[0]);
        printf("  Fuzz compiled Android binary XML files\n");
        return 1;
    }

    uint8_t* data = nullptr;
    size_t size = 0;

    if (!readFile(argv[1], &data, &size)) {
        perror("Failed to read file");
        return 1;
    }

    printf("Processing: %s (%zu bytes)\n", argv[1], size);
    LLVMFuzzerTestOneInput(data, size);
    printf("Done\n");

    free(data);
    return 0;
}
#else
// AFL 持久模式
int main(int argc, char** argv) {
    __AFL_INIT();
    
    unsigned char *buf = __AFL_FUZZ_TESTCASE_BUF;
    
    while (__AFL_LOOP(100000)) {
        int len = __AFL_FUZZ_TESTCASE_LEN;
        if (len >= (int)sizeof(ResXMLTree_header)) {
            LLVMFuzzerTestOneInput(buf, len);
        }
    }
    
    return 0;
}
#endif
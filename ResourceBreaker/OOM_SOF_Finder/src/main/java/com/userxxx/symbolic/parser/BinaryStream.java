package com.userxxx.symbolic.parser;

interface BinaryStream {
    int readUInt();
    int readUShort();
    void seek(int pos);
    int tell();
    // 其它IO方法
}
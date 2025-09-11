package com.example.wuaicode.core.parser;

public interface CodeParser<T> {
    T parse(String codeContent);
}

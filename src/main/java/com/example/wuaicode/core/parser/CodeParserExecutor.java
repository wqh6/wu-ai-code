package com.example.wuaicode.core.parser;

import com.example.wuaicode.model.enums.CodeGenTypeEnum;

public class CodeParserExecutor {
    private static HtmlCodeParser htmlCodeParser = new HtmlCodeParser();
    private static MultiCodeParser multiCodeParser = new MultiCodeParser();
    public static Object parse(String codeContent, CodeGenTypeEnum codeGenTypeEnum){
        return switch (codeGenTypeEnum) {
            case HTML -> htmlCodeParser.parse(codeContent);
            case MULTI_FILE -> multiCodeParser.parse(codeContent);
            default -> throw new RuntimeException("不支持的代码生成类型");
        };
    }
}

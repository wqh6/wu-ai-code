package com.example.wuaicode.core.saver;

import com.example.wuaicode.ai.model.HtmlCodeResult;
import com.example.wuaicode.ai.model.MultiFileCodeResult;
import com.example.wuaicode.model.enums.CodeGenTypeEnum;

import java.io.File;

public class CodeFileSaverExecutor {
    private static HtmlCodeFileSaverTemplate htmlCodeFileSaverTemplate = new HtmlCodeFileSaverTemplate();
    private static MultiCodeFileSaverTemplate multiCodeFileSaverTemplate = new MultiCodeFileSaverTemplate();
    public static File saveCode(Object codeContent,CodeGenTypeEnum codeGenTypeEnum,Long appId) {
        return switch (codeGenTypeEnum) {
            case HTML -> htmlCodeFileSaverTemplate.saveCode((HtmlCodeResult) codeContent, appId);
            case MULTI_FILE -> multiCodeFileSaverTemplate.saveCode((MultiFileCodeResult) codeContent, appId);
            default -> null;
        };
    }
}

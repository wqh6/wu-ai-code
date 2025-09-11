package com.example.wuaicode.core.saver;

import cn.hutool.core.io.FileUtil;
import com.example.wuaicode.ai.model.MultiFileCodeResult;
import com.example.wuaicode.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class MultiCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult>{
    @Override
    public CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    public void saveFiles(MultiFileCodeResult codeContent, String mkdir) {
        if(codeContent == null){
            return;
        }
        if(codeContent.getHtmlCode() != null){
            writeToFile(mkdir, "index.html",codeContent.getHtmlCode());
        }
        if(codeContent.getCssCode() != null){
            writeToFile(mkdir, "style.css", codeContent.getCssCode());
        }
        if(codeContent.getJsCode() != null){
            writeToFile(mkdir, "script.js", codeContent.getJsCode());
        }
    }
    /**
     * 写入单个文件
     */
    private static void writeToFile(String dirPath, String filename, String content) {
        String filePath = dirPath + File.separator + filename;
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
    }
}

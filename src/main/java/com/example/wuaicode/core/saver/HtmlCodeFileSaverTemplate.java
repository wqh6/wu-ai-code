package com.example.wuaicode.core.saver;

import cn.hutool.core.io.FileUtil;
import com.example.wuaicode.ai.model.HtmlCodeResult;
import com.example.wuaicode.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult>{
    @Override
    public CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    public void saveFiles(HtmlCodeResult codeContent, String mkdir) {
        writeToFile(mkdir, "index.html", codeContent.getHtmlCode());
    }
    /**
     * 写入单个文件
     */
    private static void writeToFile(String dirPath, String filename, String content) {
        String filePath = dirPath + File.separator + filename;
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
    }
}

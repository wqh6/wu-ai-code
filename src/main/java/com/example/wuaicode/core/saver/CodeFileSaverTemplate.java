package com.example.wuaicode.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.example.wuaicode.constant.AppConstant;
import com.example.wuaicode.core.parser.CodeParserExecutor;
import com.example.wuaicode.exception.BusinessException;
import com.example.wuaicode.exception.ErrorCode;
import com.example.wuaicode.model.enums.CodeGenTypeEnum;

import java.io.File;

public abstract class CodeFileSaverTemplate<T> {
    // 文件保存根目录
    protected static final String FILE_SAVE_ROOT_DIR = AppConstant.CODE_OUTPUT_ROOT_DIR;
    /**
     * 模板方法：保存代码的标准流程（使用 appId）
     *
     * @param result 代码结果对象
     * @param appId  应用 ID
     * @return 保存的目录
     */
    public final File saveCode(T result, Long appId) {
        // 1. 验证输入
        validateCodeResult(result);
        // 2. 构建基于 appId 的目录
        String baseDirPath = buildUniqueDir(appId);
        // 3. 保存文件（具体实现由子类提供）
        saveFiles(result, baseDirPath);
        // 4. 返回目录文件对象
        return new File(baseDirPath);
    }

    /**
     * 构建基于 appId 的目录路径
     *
     * @param appId 应用 ID
     * @return 目录路径
     */
    protected final String buildUniqueDir(Long appId) {
        if (appId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        }
        String codeType = getCodeType().getValue();
        String uniqueDirName = StrUtil.format("{}_{}", codeType, appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }
    public abstract CodeGenTypeEnum getCodeType();
    public abstract void saveFiles(T result, String baseDirPath);
    private void validateCodeResult(T result) {
        if (result == null) {
            throw new RuntimeException("生成代码失败");
        }
    }
}

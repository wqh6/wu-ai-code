package com.example.wuaicode.core;

import com.example.wuaicode.controller.AppController;
import com.example.wuaicode.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

@SpringBootTest
public class AiCodeGeneratorFacadeTest {
    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;
    @Resource
    private AppController appController;
//    @Test
//    public void testGenerateAndSaverCode() {
//        File file = aiCodeGeneratorFacade.generateAndSaverCode("生成一个HTML页面，内容是“Hello World”", CodeGenTypeEnum.HTML);
//        System.out.println(file.getAbsolutePath());
//    }
//    @Test
//    public void testGenerateAndSaverCodeMulti() {
//        File file = aiCodeGeneratorFacade.generateAndSaverCode("生成登录页面", CodeGenTypeEnum.MULTI_FILE);
//        System.out.println(file.getAbsolutePath());
//    }

    @Test
    public void testGenerateAndSaverCodeStream() {
        Flux<String> codeResult = aiCodeGeneratorFacade.generateAndSaverCode("生成贪吃蛇游戏", CodeGenTypeEnum.MULTI_FILE,5775L);
        List<String> block = codeResult.collectList().block();
        Assertions.assertNotNull(block);
        String join = String.join(",", block);
        System.out.println(join);
    }

    @Test
    public void testAppControllerTest() {
        appController.chatToGenCode(5775L, "生成一个HTML页面，内容是“Hello World”", null);
    }
    @Test
    void generateVueProjectCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaverCode(
                "简单的任务记录网站，总代码量不超过 200 行",
                CodeGenTypeEnum.VUE_PROJECT, 1L);
        // 阻塞等待所有数据收集完成
        List<String> result = codeStream.collectList().block();
        // 验证结果
        Assertions.assertNotNull(result);
        String completeContent = String.join("", result);
        Assertions.assertNotNull(completeContent);
    }


}

package com.example.wuaicode.ai;

import com.example.wuaicode.ai.model.HtmlCodeResult;
import com.example.wuaicode.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest

public class AiCodeGeneratorServiceTest {
    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;
    @Test
    public void testGenerateCodeHtml() {
        HtmlCodeResult s = aiCodeGeneratorService.generateCodeHtml("生成一个HTML页面，内容是：这是一个HTML页面,20行代码之类");
        Assertions.assertNotNull(s);
    }
    @Test
    public void testGenerateCodeMulti() {
        MultiFileCodeResult s = aiCodeGeneratorService.generateCodeMultiFile("生成登录页面20行代码之类");
        Assertions.assertNotNull(s);
    }
    @Test
    public void testGenerateCodeMultiStream() {
        Flux<String> s = aiCodeGeneratorService.generateCodeMultiFileStream("生成登录页面20行代码之类");
        Assertions.assertNotNull(s);
    }
}

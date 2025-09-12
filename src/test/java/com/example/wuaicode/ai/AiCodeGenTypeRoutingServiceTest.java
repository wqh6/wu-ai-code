package com.example.wuaicode.ai;

import com.example.wuaicode.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiCodeGenTypeRoutingServiceTest {
    @Resource
    private AiCodeGenTypeRoutingService aiCodeGenTypeRoutingService;
    @Test
    public void aiCodeGenTypeRoutingServiceTest() {
        CodeGenTypeEnum codeGenTypeEnum = aiCodeGenTypeRoutingService.chatRouting("生成一个简单的登录页面");
        System.out.println(codeGenTypeEnum.getValue());
        CodeGenTypeEnum codeGenTypeEnum1 = aiCodeGenTypeRoutingService.chatRouting("生成一个精美的注册页面");
        System.out.println(codeGenTypeEnum1.getValue());
        CodeGenTypeEnum codeGenTypeEnum2 = aiCodeGenTypeRoutingService.chatRouting("生成一个电商网站");
        System.out.println(codeGenTypeEnum2.getValue());
    }

}

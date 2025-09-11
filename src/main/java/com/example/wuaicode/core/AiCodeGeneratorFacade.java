package com.example.wuaicode.core;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.example.wuaicode.ai.AiCodeGeneratorService;
import com.example.wuaicode.ai.AiCodeGeneratorServiceFactory;
import com.example.wuaicode.ai.model.HtmlCodeResult;
import com.example.wuaicode.ai.model.MultiFileCodeResult;
import com.example.wuaicode.ai.model.message.AiResponseMessage;
import com.example.wuaicode.ai.model.message.ToolExecutedMessage;
import com.example.wuaicode.ai.model.message.ToolRequestMessage;
import com.example.wuaicode.core.parser.CodeParserExecutor;
import com.example.wuaicode.core.saver.CodeFileSaverExecutor;
import com.example.wuaicode.exception.BusinessException;
import com.example.wuaicode.exception.ErrorCode;
import com.example.wuaicode.model.enums.CodeGenTypeEnum;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.File;
@Slf4j
@Component
public class AiCodeGeneratorFacade {
    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;
    public Flux<String> generateAndSaverCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum,Long appId) {
        if(codeGenTypeEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "生成类型不能为空");
        }
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return switch (codeGenTypeEnum){
            case HTML -> {
                Flux<String> codeStream  = aiCodeGeneratorService.generateCodeHtmlStream(userMessage);
                yield generateAndSaverCodeStream(codeStream, CodeGenTypeEnum.HTML,appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateCodeMultiFileStream(userMessage);
                yield generateAndSaverCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE,appId);
            }
            case VUE_PROJECT -> {
                TokenStream tokenStream = aiCodeGeneratorService.generateVueProjectCodeStream(appId, userMessage);
                yield processTokenStream(tokenStream);
            }
            default -> {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "生成类型错误");
            }
        };
    }

    /**
     * 将 TokenStream 转换为 Flux<String>，并传递工具调用信息
     *
     * @param tokenStream TokenStream 对象
     * @return Flux<String> 流式响应
     */
    private Flux<String> processTokenStream(TokenStream tokenStream) {
        return Flux.create(sink -> {
            tokenStream.onPartialResponse((String partialResponse) -> {
                        AiResponseMessage aiResponseMessage = new AiResponseMessage(partialResponse);
                        sink.next(JSONUtil.toJsonStr(aiResponseMessage));
                    })
                    .onPartialToolExecutionRequest((index, toolExecutionRequest) -> {
                        ToolRequestMessage toolRequestMessage = new ToolRequestMessage(toolExecutionRequest);
                        sink.next(JSONUtil.toJsonStr(toolRequestMessage));
                    })
                    .onToolExecuted((ToolExecution toolExecution) -> {
                        ToolExecutedMessage toolExecutedMessage = new ToolExecutedMessage(toolExecution);
                        sink.next(JSONUtil.toJsonStr(toolExecutedMessage));
                    })
                    .onCompleteResponse((ChatResponse response) -> {
                        sink.complete();
                    })
                    .onError((Throwable error) -> {
                        error.printStackTrace();
                        sink.error(error);
                    })
                    .start();
        });
    }


    /**
     * HTML和MULTI_FILE代码生成和保存
     * @param codeResult
     * @param codeGenTypeEnum
     * @param appId
     * @return
     */
    private Flux<String> generateAndSaverCodeStream(Flux<String> codeResult, CodeGenTypeEnum codeGenTypeEnum,Long appId) {
        StringBuilder codeBuilder=new StringBuilder();
        return codeResult.doOnNext(chunk->{
            codeBuilder.append(chunk);
        }).doOnComplete(()->{
            String completeCode = codeBuilder.toString();
            switch (codeGenTypeEnum){
                case HTML -> {
                    Object htmlCodeResult = CodeParserExecutor.parse(completeCode, CodeGenTypeEnum.HTML);
                    File saveDir = CodeFileSaverExecutor.saveCode(htmlCodeResult, CodeGenTypeEnum.HTML,appId);
                    log.info("保存成功，目录为：{}", saveDir.getAbsolutePath());
                }
                case MULTI_FILE -> {
                    Object multiFileCodeResult = CodeParserExecutor.parse(completeCode, CodeGenTypeEnum.MULTI_FILE);
                    File saveDir = CodeFileSaverExecutor.saveCode(multiFileCodeResult, CodeGenTypeEnum.MULTI_FILE,appId);
                    log.info("保存成功，目录为：{}", saveDir.getAbsolutePath());
                }
            }
        });
    }
//    private Flux<String> generateAndSaverCodeHtml(String userMessage) {
//        Flux<String> result = aiCodeGeneratorService.generateCodeHtmlStream(userMessage);
//        StringBuilder codeBuilder=new StringBuilder();
//        return result.doOnNext(chunk->{
//            codeBuilder.append(chunk);
//        }).doOnComplete(()->{
//            try{
//                String completeCode=codeBuilder.toString();
//                Object resultCode = CodeParserExecutor.parse(completeCode, CodeGenTypeEnum.HTML);
//                File file = CodeFileSaverExecutor.saveCode(resultCode, CodeGenTypeEnum.HTML);
//            }catch (Exception e){
//                log.error("保存失败: {}",e.getMessage());
//            }
//        });
//    }
//    private Flux<String> generateAndSaverCodeMultiFile(String userMessage) {
//        Flux<String> result= aiCodeGeneratorService.generateCodeMultiFileStream(userMessage);
//        StringBuilder codeBuilder=new StringBuilder();
//        return result.doOnNext(chunk->{
//            codeBuilder.append( chunk);
//        }).doOnComplete(()->{
//            try{
//                String completeCode=codeBuilder.toString();
//                MultiFileCodeResult multiFileCodeResult = CodeParser.parseMultiFileCode(completeCode);
//                File file = CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
//            }catch (Exception e){
//                log.error("保存失败: {}",e.getMessage());
//            }
//        });
//    }
}

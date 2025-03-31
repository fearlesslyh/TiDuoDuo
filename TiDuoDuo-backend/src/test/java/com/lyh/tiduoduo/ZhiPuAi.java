package com.lyh.tiduoduo;

import ch.qos.logback.core.net.server.Client;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/31 15:44
 */
@SpringBootTest
public class ZhiPuAi {
    @Resource
    private ClientV4 clientV4;
    //密钥已删除
    @Test
    public void test() {
        //创建一个ClientV4对象
        //创建一个List对象，用于存储ChatMessage
        List<ChatMessage> messages = new ArrayList<>();
        //创建一个ChatMessage对象，设置角色为USER，内容为“作为一名xx，请为智谱开放平台创作一个吸引人的slogan”
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "作为一名后端开发高手，请为智谱开放平台创作一个吸引人的slogan");
        //将ChatMessage对象添加到List中
        messages.add(chatMessage);
//        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());

        //创建一个ChatCompletionRequest对象，设置模型为Constants.ModelChatGLM4，是否流式输出为Boolean.FALSE，调用方法为Constants.invokeMethod，消息为messages
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        //调用client的invokeModelApi方法，传入chatCompletionRequest，获取ModelApiResponse对象
        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        //打印ModelApiResponse对象
        System.out.println(invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent());
    }
}

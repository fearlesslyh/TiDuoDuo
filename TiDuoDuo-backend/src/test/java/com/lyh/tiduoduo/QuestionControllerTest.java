package com.lyh.tiduoduo;

import com.lyh.TiDuoDuo.controller.QuestionController;
import com.lyh.TiDuoDuo.model.dto.question.AiGenerateQuestionRequest;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/4/2 21:55
 */
public class QuestionControllerTest {
    @Resource
    private QuestionController questionController;


//    void aiGenerateQuestionSSEVIPTest() throws InterruptedException {
//        AiGenerateQuestionRequest request = new AiGenerateQuestionRequest();
//        request.setAppId(3L);
//        request.setQuestionNumber(10);
//        request.setOptionNumber(2);
//
//        questionController.aiGenerateQuestionSSE(request, false);
//        questionController.aiGenerateQuestionSSE(request, false);
//        questionController.aiGenerateQuestionSSE(request, true);
//
//        Thread.sleep(1000000L);
//    }

}

package com.lyh.TiDuoDuo.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lyh.TiDuoDuo.model.dto.question.QuestionContent;
import com.lyh.TiDuoDuo.model.entity.App;
import com.lyh.TiDuoDuo.model.entity.Question;
import com.lyh.TiDuoDuo.model.entity.ScoringResult;
import com.lyh.TiDuoDuo.model.entity.UserAnswer;
import com.lyh.TiDuoDuo.model.vo.QuestionVO;
import com.lyh.TiDuoDuo.service.AppService;
import com.lyh.TiDuoDuo.service.QuestionService;
import com.lyh.TiDuoDuo.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/25 21:36
 */

/**
 * 自定义测评类应用评分策略
 */
public class CustomTestScoringStrategy implements ScoringStrategy {
    @Resource
    private QuestionService questionService;

    @Resource
    private AppService appService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        //1.根据 id 查询到题目和题目结果信息
        Long appId = app.getId();
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class)
                        .eq(Question::getAppId, appId));

        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, appId));

        //2.统计用户每个选择对应的属性个数，如 I = 10 个，E = 5 个
        Map<String, Integer> optionCount = new HashMap<>();

        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContent> questionContent = questionVO.getQuestionContent();

        for (QuestionContent questionDto : questionContent) {
            for (String answer : choices) {
                for (QuestionContent.Option option : questionDto.getOptions()) {
                    if (option.getKey().equals(answer)) {
                        String result = option.getResult();

                        if (!optionCount.containsKey(result)) {
                            optionCount.put(result, 0);
                        }

                        optionCount.put(result, optionCount.get(result) + 1);
                    }
                }
            }
        }
        //3.遍历每种评分结果，计算哪个结果的得分更高。
        ScoringResult maxScoreResult = scoringResultList.get(0);
        int maxScore = 0;

        for (ScoringResult result : scoringResultList) {
            List<String> resultProps = JSONUtil.toList(result.getResultProp(), String.class);
            int score = resultProps.stream()
                    .mapToInt(prop -> optionCount.getOrDefault(prop, 0))
                    .sum();

            if (score > maxScore) {
                maxScore = score;
                maxScoreResult = result;
            }
        }

        //4.构造返回值，填充答案对象的属性。
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(maxScoreResult.getId());
        userAnswer.setResultName(maxScoreResult.getResultName());
        userAnswer.setResultDesc(maxScoreResult.getResultDesc());
        userAnswer.setResultPicture(maxScoreResult.getResultPicture());

        return userAnswer;
    }
}

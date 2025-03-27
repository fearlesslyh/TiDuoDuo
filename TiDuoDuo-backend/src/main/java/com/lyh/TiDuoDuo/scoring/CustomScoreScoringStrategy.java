package com.lyh.TiDuoDuo.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lyh.TiDuoDuo.common.ErrorCode;
import com.lyh.TiDuoDuo.exception.BusinessException;
import com.lyh.TiDuoDuo.model.dto.question.QuestionContent;
import com.lyh.TiDuoDuo.model.entity.App;
import com.lyh.TiDuoDuo.model.entity.Question;
import com.lyh.TiDuoDuo.model.entity.ScoringResult;
import com.lyh.TiDuoDuo.model.entity.UserAnswer;
import com.lyh.TiDuoDuo.model.vo.QuestionVO;
import com.lyh.TiDuoDuo.service.QuestionService;
import com.lyh.TiDuoDuo.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/25 21:33
 */

/**
 * 自定义打分类应用评分策略
 */
@ScoringStrategyConfig(appType = 0, scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {
    @Resource
    private QuestionService questionService;
    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        //根据 id 查询到题目和题目结果信息（按分数降序排序）
        Long appId = app.getId();
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class)
                        .eq(Question::getAppId, appId));

        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, appId)
                        .orderByDesc(ScoringResult::getResultScoreRange));

        int totalScore = 0;

        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContent> questionContent = questionVO.getQuestionContent();

        // 校验数量
        if (questionContent.size() != choices.size()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目和用户答案数量不一致");
        }

        for (int i = 0; i < questionContent.size(); i++) {
            Map<String, Integer> resultMap = questionContent.get(i).getOptions().stream()
                    .collect(Collectors.toMap(QuestionContent.Option::getKey, QuestionContent.Option::getScore));
            Integer score = Optional.ofNullable(resultMap.get(choices.get(i))).orElse(0);
            totalScore += score;
        }

        // 3.遍历得分结果，找到第一个用户分数大于得分范围的结果，作为最终结果
        // 返回最大得分结果
        ScoringResult maxScoreResult = scoringResultList.get(0);
        for (ScoringResult result : scoringResultList) {
            if (totalScore >= result.getResultScoreRange()) {
                maxScoreResult = result;
                break;
            }
        }

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

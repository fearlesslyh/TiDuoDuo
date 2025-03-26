package com.lyh.TiDuoDuo.scoring;

import com.lyh.TiDuoDuo.common.ErrorCode;
import com.lyh.TiDuoDuo.exception.BusinessException;
import com.lyh.TiDuoDuo.model.entity.App;
import com.lyh.TiDuoDuo.model.entity.UserAnswer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/26 13:07
 */
@Service
public class ScoringStrategyExecutor {

    // 策略列表
    @Resource
    private List<ScoringStrategy> scoringStrategyList;


    /**
     * 评分
     *
     * @param choiceList
     * @param app
     * @return
     * @throws Exception
     */
    public UserAnswer doScore(List<String> choiceList, App app) throws Exception {
        // 获取应用类型
        Integer appType = app.getAppType();
        // 获取评分策略
        Integer appScoringStrategy = app.getScoringStrategy();
        // 如果应用类型或评分策略为空，则抛出异常
        if (appType == null || appScoringStrategy == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
        }
        // 根据注解获取策略
        for (ScoringStrategy strategy : scoringStrategyList) {
            // 如果策略类上有注解
            if (strategy.getClass().isAnnotationPresent(ScoringStrategyConfig.class)) {
                // 获取注解
                ScoringStrategyConfig scoringStrategyConfig = strategy.getClass().getAnnotation(ScoringStrategyConfig.class);
                // 如果注解中的应用类型和评分策略与传入的应用类型和评分策略匹配
                if (scoringStrategyConfig.appType() == appType && scoringStrategyConfig.scoringStrategy() == appScoringStrategy) {
                    // 执行评分策略
                    return strategy.doScore(choiceList, app);
                }
            }
        }
        // 如果没有找到匹配的策略，则抛出异常
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
    }
}

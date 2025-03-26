package com.lyh.TiDuoDuo.scoring;

import com.lyh.TiDuoDuo.common.ErrorCode;
import com.lyh.TiDuoDuo.exception.BusinessException;
import com.lyh.TiDuoDuo.model.entity.App;
import com.lyh.TiDuoDuo.model.entity.UserAnswer;
import com.lyh.TiDuoDuo.model.enums.AppEnum;
import com.lyh.TiDuoDuo.model.enums.ReviewStrategyEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/26 12:53
 */
@Service
@Deprecated
public class ScoringStrategyContext {

    @Resource
    private CustomScoreScoringStrategy customScoreScoringStrategy;

    @Resource
    private CustomTestScoringStrategy customTestScoringStrategy;

    /**
     * 评分
     *
     * @param choiceList
     * @param app
     * @return
     * @throws Exception
     */
    public UserAnswer doScore(List<String> choiceList, App app) throws Exception {
        // 根据应用类型获取应用枚举
        AppEnum appTypeEnum = AppEnum.getEnumByValue(app.getAppType());
        // 根据评分策略获取评分策略枚举
        ReviewStrategyEnum appScoringStrategyEnum = ReviewStrategyEnum.getEnumByValue(app.getScoringStrategy());
        // 如果应用枚举或评分策略枚举为空，则抛出异常
        if (appTypeEnum == null || appScoringStrategyEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
        }
        // 根据不同的应用类别和评分策略，选择对应的策略执行
        switch (appTypeEnum) {
            case SCORE:
                switch (appScoringStrategyEnum) {
                    case CUSTOMIZE:
                        // 如果应用类别为评分，评分策略为自定义，则执行自定义评分策略
                        return customScoreScoringStrategy.doScore(choiceList, app);
                    case AI:
                        // 如果应用类别为评分，评分策略为AI，则执行AI评分策略
                        break;
                }
                break;
            case TEST:
                switch (appScoringStrategyEnum) {
                    case CUSTOMIZE:
                        // 如果应用类别为测试，评分策略为自定义，则执行自定义测试评分策略
                        return customTestScoringStrategy.doScore(choiceList, app);
                    case AI:
                        // 如果应用类别为测试，评分策略为AI，则执行AI测试评分策略
                        break;
                }
                break;
        }
        // 如果没有匹配的策略，则抛出异常
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到匹配的策略");
    }
}

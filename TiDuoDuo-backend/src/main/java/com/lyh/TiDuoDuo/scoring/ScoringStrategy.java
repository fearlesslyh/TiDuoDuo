package com.lyh.TiDuoDuo.scoring;

import com.lyh.TiDuoDuo.model.entity.App;
import com.lyh.TiDuoDuo.model.entity.UserAnswer;

import java.util.List;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/25 21:31
 */
public interface ScoringStrategy {

    /**
     * 执行评分
     *
     * @param choices 用户选择的答案
     * @param app 应用
     * @return 用户答案
     * @throws Exception 异常
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}

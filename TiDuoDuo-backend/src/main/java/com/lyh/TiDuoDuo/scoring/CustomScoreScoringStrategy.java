package com.lyh.TiDuoDuo.scoring;

import com.lyh.TiDuoDuo.model.entity.App;
import com.lyh.TiDuoDuo.model.entity.UserAnswer;

import java.util.List;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/25 21:33
 */

/**
 * 自定义打分类应用评分策略
 */
public class CustomScoreScoringStrategy implements ScoringStrategy {
    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        return null;
    }
}

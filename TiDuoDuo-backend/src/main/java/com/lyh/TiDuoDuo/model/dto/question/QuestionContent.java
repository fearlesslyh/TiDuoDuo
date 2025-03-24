package com.lyh.TiDuoDuo.model.dto.question;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionContent {
    /**
     * 题目标题
     */
    private String title;
    /**
     * 题目选项
     */
    private List<Option> options;

    @Data
    @NoArgsConstructor
    // 添加无参构造方法
    @AllArgsConstructor
    // 添加全参构造方法
    @Builder
    // 添加建造者模式
    public static class Option {
        private String result;
        // 结果
        private int score;
        // 分数
        private String value;
        // 值
        private String key;
        // 键
    }
}

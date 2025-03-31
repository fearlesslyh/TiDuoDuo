package com.lyh.TiDuoDuo.model.dto.question;

import lombok.Data;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/31 19:49
 */
@Data
public class QuestionAnswer {
    /**
     * 题目
     */
    private String title;
    /**
     * 答案
     */
    private String answer;
}

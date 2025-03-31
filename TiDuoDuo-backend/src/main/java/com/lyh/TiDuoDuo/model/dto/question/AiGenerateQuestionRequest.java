package com.lyh.TiDuoDuo.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/31 17:15
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {
    // 应用ID
    private Long appId;
    // 题目数量
    int questionNumber = 10;
    // 选项数量
    int optionNumber = 2;
    // 序列化版本号
    private static final long serialVersionUID = 1L;
}

package com.lyh.TiDuoDuo.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建题目请求
 *
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>

 */
@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 题目内容（json格式）
     */
    private QuestionContent questionContent;

    /**
     * 应用 id
     */
    private Long appId;

    private static final long serialVersionUID = 1L;
}
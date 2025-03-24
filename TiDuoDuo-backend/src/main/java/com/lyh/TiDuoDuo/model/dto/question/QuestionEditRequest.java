package com.lyh.TiDuoDuo.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 编辑题目请求
 *
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>

 */
@Data
public class QuestionEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目内容（json格式）
     */
    private QuestionContent questionContent;

    private static final long serialVersionUID = 1L;
}
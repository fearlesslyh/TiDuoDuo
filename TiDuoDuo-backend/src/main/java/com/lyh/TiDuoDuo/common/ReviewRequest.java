package com.lyh.TiDuoDuo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/25 21:17
 */
@Data
public class ReviewRequest implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 审核状态: 0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;
    /**
     * 审核信息
     */
    private String reviewMessage;

    private static final long serialVersionUID = 1L;
}

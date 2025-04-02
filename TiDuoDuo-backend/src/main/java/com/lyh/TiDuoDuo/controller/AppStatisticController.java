package com.lyh.TiDuoDuo.controller;

import com.lyh.TiDuoDuo.common.BaseResponse;
import com.lyh.TiDuoDuo.common.ErrorCode;
import com.lyh.TiDuoDuo.common.ResultUtils;
import com.lyh.TiDuoDuo.exception.ThrowUtils;
import com.lyh.TiDuoDuo.mapper.UserAnswerMapper;
import com.lyh.TiDuoDuo.model.dto.statistic.AppAnswerCount;
import com.lyh.TiDuoDuo.model.dto.statistic.AppAnswerResultCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/4/2 22:24
 */
@RestController
@RequestMapping("/app/statistic")
@Slf4j
public class AppStatisticController {
    @Resource
    private UserAnswerMapper userAnswerMapper;

    @GetMapping("/answer_result_count")
    public BaseResponse<List<AppAnswerResultCount>> getAppAnswerResultCount(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userAnswerMapper.doAppAnswerResultCount(appId));
    }

    @GetMapping("/answer_count")
    public BaseResponse<List<AppAnswerCount>> getAppAnswerCount() {
        return ResultUtils.success(userAnswerMapper.doAppAnswerCount());
    }
}

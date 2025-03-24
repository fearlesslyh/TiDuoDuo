package com.lyh.TiDuoDuo.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.TiDuoDuo.common.ErrorCode;
import com.lyh.TiDuoDuo.constant.CommonConstant;
import com.lyh.TiDuoDuo.exception.ThrowUtils;
import com.lyh.TiDuoDuo.model.dto.question.QuestionQueryRequest;
import com.lyh.TiDuoDuo.model.entity.App;
import com.lyh.TiDuoDuo.model.entity.Question;
import com.lyh.TiDuoDuo.model.entity.User;
import com.lyh.TiDuoDuo.model.vo.QuestionVO;
import com.lyh.TiDuoDuo.model.vo.UserVO;
import com.lyh.TiDuoDuo.service.AppService;
import com.lyh.TiDuoDuo.service.QuestionService;
import com.lyh.TiDuoDuo.mapper.QuestionMapper;
import com.lyh.TiDuoDuo.service.UserService;
import com.lyh.TiDuoDuo.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 题目服务实现
 *
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;

    /**
     * 校验数据
     *
     * @param question
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        ThrowUtils.throwIf(question == null, ErrorCode.PARAMS_ERROR);
        //  从对象中取值
        String questionContent = question.getQuestionContent();
        Long appId = question.getAppId();
        Long userId = question.getUserId();

        // 创建数据时，参数不能为空
        if (add) {
            //  补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(questionContent), ErrorCode.PARAMS_ERROR, "题目不能为空");
            ThrowUtils.throwIf(ObjectUtils.isEmpty(appId), ErrorCode.PARAMS_ERROR, "应用不能为空");
            ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户不能为空");
        }
        // 修改数据时，有参数则校验
        //  补充校验规则
        if (appId != null) {
            App app = appService.getById(appId);
            ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR, "应用不存在");
        }
    }

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        //  从对象中取值
        Long id = questionQueryRequest.getId();
        Long notId = questionQueryRequest.getNotId();
        Long appId = questionQueryRequest.getAppId();
        Long userId = questionQueryRequest.getUserId();
        String content = questionQueryRequest.getContent();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();


        //  补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appId), "appId", appId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        // 对象转封装类
        QuestionVO questionVO = QuestionVO.objToVo(question);

        //  可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUser(userVO);
        // endregion

        return questionVO;
    }

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        // 获取题目列表
        List<Question> questionList = questionPage.getRecords();
        // 创建题目封装分页对象
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        // 如果题目列表为空，直接返回题目封装分页对象
        if (CollUtil.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            // 将题目对象转换为题目封装对象
            return QuestionVO.objToVo(question);
        }).collect(Collectors.toList());

        //  可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        // 获取题目列表中的用户ID集合
        Set<Long> userIdSet = questionList.stream()
                .map(Question::getUserId)
                .collect(Collectors.toSet());
        // 根据用户ID集合查询用户列表，并按照用户ID分组
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionVOList.forEach(questionVO -> {
            // 获取题目封装对象的用户ID
            Long userId = questionVO.getUserId();
            // 获取用户对象
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            // 将用户对象转换为用户封装对象，并填充到题目封装对象中
            questionVO.setUser(userService.getUserVO(user));
        });
        // endregion

        // 设置题目封装分页对象的记录列表
        questionVOPage.setRecords(questionVOList);
        // 返回题目封装分页对象
        return questionVOPage;
    }

}





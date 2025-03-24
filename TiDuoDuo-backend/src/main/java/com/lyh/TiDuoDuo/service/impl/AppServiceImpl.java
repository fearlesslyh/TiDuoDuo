package com.lyh.TiDuoDuo.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.TiDuoDuo.common.ErrorCode;
import com.lyh.TiDuoDuo.constant.CommonConstant;
import com.lyh.TiDuoDuo.exception.ThrowUtils;
import com.lyh.TiDuoDuo.mapper.AppMapper;
import com.lyh.TiDuoDuo.model.dto.app.AppQueryRequest;
import com.lyh.TiDuoDuo.model.entity.App;
import com.lyh.TiDuoDuo.model.entity.User;
import com.lyh.TiDuoDuo.model.enums.AppEnum;
import com.lyh.TiDuoDuo.model.enums.ReviewStatusEnum;
import com.lyh.TiDuoDuo.model.enums.ReviewStrategyEnum;
import com.lyh.TiDuoDuo.model.vo.AppVO;
import com.lyh.TiDuoDuo.model.vo.UserVO;
import com.lyh.TiDuoDuo.service.AppService;
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

import static org.bouncycastle.asn1.x500.style.RFC4519Style.title;

/**
 * 应用服务实现
 *
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 */
@Service
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param app
     * @param add 对创建的数据进行校验
     */
    @Override
    public void validApp(App app, boolean add) {
        // 校验app是否为空
        ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR);
        //  从对象中取值，校验
        String appName = app.getAppName();
        String appDesc = app.getAppDesc();
        Integer appType = app.getAppType();
        Integer scoringStrategy = app.getScoringStrategy();
        Integer reviewStatus = app.getReviewStatus();

        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            // 校验应用名是否为空
            ThrowUtils.throwIf(StringUtils.isBlank(appName), ErrorCode.PARAMS_ERROR, "应用名不能为空");
            // 校验应用描述是否为空
            ThrowUtils.throwIf(StringUtils.isBlank(appDesc), ErrorCode.PARAMS_ERROR, "应用描述不能为空");
            // 校验应用类型是否为空
            AppEnum appEnum = AppEnum.getEnumByValue(appType);
            ThrowUtils.throwIf(appEnum == null, ErrorCode.PARAMS_ERROR, "应用类型不能为空");
            // 校验审核状态是否为空
            ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getEnumByValue(reviewStatus);
            ThrowUtils.throwIf(reviewStatusEnum == null, ErrorCode.PARAMS_ERROR, "审核状态不能为空");
            // 校验评分策略是否为空
            ReviewStrategyEnum reviewStrategyEnum = ReviewStrategyEnum.getEnumByValue(scoringStrategy);
            ThrowUtils.throwIf(reviewStrategyEnum == null, ErrorCode.PARAMS_ERROR, "评分策略不能为空");
        }
        // 修改数据时，有参数则校验
        //  补充校验规则
        // 校验应用名称长度是否小于30
        if (StringUtils.isNotBlank(appName)) {
            ThrowUtils.throwIf(appName.length() < 30, ErrorCode.PARAMS_ERROR, "应用名称要小于30");
        }
        // 校验应用描述长度是否小于100
        if (StringUtils.isNotBlank(appDesc)) {
            ThrowUtils.throwIf(appDesc.length() < 100, ErrorCode.PARAMS_ERROR, "应用描述要小于100");
        }
    }

    /**
     * 获取查询条件
     *
     * @param appQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<App> getQueryWrapper(AppQueryRequest appQueryRequest) {
        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        if (appQueryRequest == null) {
            return queryWrapper;
        }
        //  从对象中取值
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String appDesc = appQueryRequest.getAppDesc();
        String appIcon = appQueryRequest.getAppIcon();
        Integer appType = appQueryRequest.getAppType();
        Integer scoringStrategy = appQueryRequest.getScoringStrategy();
        Integer reviewStatus = appQueryRequest.getReviewStatus();
        String reviewMessage = appQueryRequest.getReviewMessage();
        Long reviewerId = appQueryRequest.getReviewerId();
        Long userId = appQueryRequest.getUserId();
        Long notId = appQueryRequest.getNotId();
        String searchText = appQueryRequest.getSearchText();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();

        //  补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("appName", searchText).or().like("appDesc", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(appName), "appName", appName);
        queryWrapper.like(StringUtils.isNotBlank(appDesc), "appDesc", appDesc);
        queryWrapper.like(StringUtils.isNotBlank(reviewMessage), "reviewMessage", reviewMessage);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(StringUtils.isNotBlank(appIcon), "appIcon", appIcon);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appType), "appType", appType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(scoringStrategy), "scoringStrategy", scoringStrategy);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewStatus), "reviewStatus", reviewStatus);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewerId), "reviewerId", reviewerId);

        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取应用封装
     *
     * @param app
     * @param request
     * @return
     */
    @Override
    public AppVO getAppVO(App app, HttpServletRequest request) {
        // 对象转封装类
        AppVO appVO = AppVO.objToVo(app);

        //  可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = app.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        appVO.setUser(userVO);
        // endregion

        return appVO;
    }

    /**
     * 分页获取应用封装
     *
     * @param appPage
     * @param request
     * @return
     */
    @Override
    public Page<AppVO> getAppVOPage(Page<App> appPage, HttpServletRequest request) {
        // 获取分页后的应用列表
        List<App> appList = appPage.getRecords();
        // 创建一个新的分页对象，用于存储封装后的应用列表
        Page<AppVO> appVOPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
        // 如果应用列表为空，则直接返回封装后的分页对象
        if(CollUtil.isEmpty(appList)){
            return appVOPage;
        }
        // 将应用列表转换为封装后的应用列表
        List<AppVO> appVOList = appList.stream()
                .map(app -> {
                    return AppVO.objToVo(app);
                })
                .collect(Collectors.toList());
        // 获取应用列表中的用户ID集合
        Set<Long> userIdSet = appList.stream()
                .map(App::getUserId)
                .collect(Collectors.toSet());
        // 根据用户ID集合获取用户列表，并按照用户ID进行分组
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 遍历封装后的应用列表，为每个应用设置用户信息
        appVOList.forEach(appVO -> {
            Long userId = appVO.getUserId();
            User user=null;
            // 如果用户ID集合中包含该用户ID，则获取该用户
            if(userIdUserListMap.containsKey(userId)){
                userIdUserListMap.get(userId).get(0);
            }
            appVO.setUser(userService.getUserVO(user));
        });

        appVOPage.setRecords(appVOList);

        return appVOPage;
    }
}

package com.lyh.TiDuoDuo.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyh.TiDuoDuo.annotation.AuthCheck;
import com.lyh.TiDuoDuo.common.BaseResponse;
import com.lyh.TiDuoDuo.common.DeleteRequest;
import com.lyh.TiDuoDuo.common.ErrorCode;
import com.lyh.TiDuoDuo.common.ResultUtils;
import com.lyh.TiDuoDuo.constant.UserConstant;
import com.lyh.TiDuoDuo.exception.BusinessException;
import com.lyh.TiDuoDuo.exception.ThrowUtils;
import com.lyh.TiDuoDuo.manager.AiManager;
import com.lyh.TiDuoDuo.model.dto.question.*;
import com.lyh.TiDuoDuo.model.entity.App;
import com.lyh.TiDuoDuo.model.entity.Question;
import com.lyh.TiDuoDuo.model.entity.User;
import com.lyh.TiDuoDuo.model.enums.AppEnum;
import com.lyh.TiDuoDuo.model.vo.QuestionVO;
import com.lyh.TiDuoDuo.service.AppService;
import com.lyh.TiDuoDuo.service.QuestionService;
import com.lyh.TiDuoDuo.service.UserService;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 题目接口
 *
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;

    @Resource
    private AiManager aiManager;

    // region 增删改查

    /**
     * 创建题目
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<QuestionContent> questionContentDto = questionAddRequest.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContentDto));
        // 数据校验
        questionService.validQuestion(question, true);
        //  填充默认值
        User loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    /**
     * 删除题目
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新题目（仅管理员可用）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //  在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<QuestionContent> questionContentDto = questionUpdateRequest.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContentDto));
        // 数据校验
        questionService.validQuestion(question, false);
        // 判断是否存在
        long id = questionUpdateRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取题目（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }

    /**
     * 分页获取题目列表（仅管理员可用）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取题目列表（封装类）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取当前登录用户创建的题目列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 编辑题目（给用户使用）
     *
     * @param questionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //  在此处将实体类和 DTO 进行转换
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<QuestionContent> questionContentDto = questionEditRequest.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContentDto));
        // 数据校验
        questionService.validQuestion(question, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = questionEditRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/ai_generate")
    public BaseResponse<List<QuestionContent>> aiGenerateQuestion(@RequestBody AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        //判断参数是否为空
        ThrowUtils.throwIf(aiGenerateQuestionRequest == null, ErrorCode.PARAMS_ERROR);
        //获取参数
        Long appId = aiGenerateQuestionRequest.getAppId();
        int questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        int optionNumber = aiGenerateQuestionRequest.getOptionNumber();
        //根据appId获取App对象
        App app = appService.getById(appId);
        //判断App对象是否为空
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        //获取生成题目系统消息
        String userMessage = getGenerateQuestionSystemMessage(app, questionNumber, optionNumber);
        //调用aiManager的doSyncUnstableRequest方法，获取结果
        String result = aiManager.doSyncUnstableRequest(userMessage, GENERATE_QUESTION_SYSTEM_MESSAGE);
        //获取结果中的json字符串
        int start = result.indexOf("[");
        int end = result.indexOf("]");
        String json = result.substring(start, end + 1);
        //将json字符串转换为List<QuestionContent>对象
        List<QuestionContent> questionContentList = JSONUtil.toList(json, QuestionContent.class);
        //返回成功结果
        return ResultUtils.success(questionContentList);
    }

    @GetMapping("/ai_generate/sse")
    public SseEmitter aiGenerateQuestionSSE(AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        //判断参数是否为空
        ThrowUtils.throwIf(aiGenerateQuestionRequest == null, ErrorCode.PARAMS_ERROR);
        //获取参数
        Long appId = aiGenerateQuestionRequest.getAppId();
        int questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
        int optionNumber = aiGenerateQuestionRequest.getOptionNumber();
        //根据appId获取App对象
        App app = appService.getById(appId);
        //判断App对象是否为空
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        //获取生成题目系统消息, 封装prompt
        String userMessage = getGenerateQuestionSystemMessage(app, questionNumber, optionNumber);
        //建立sseemitter连接对象
        SseEmitter sseEmitter = new SseEmitter();
        //调用aiManager的doStreamRequest方法，获取结果
        Flowable<ModelData> modelDataFlowable = aiManager.doStreamRequest(GENERATE_QUESTION_SYSTEM_MESSAGE, userMessage, null);
        //订阅结果
        AtomicInteger count = new AtomicInteger(0);
        // 创建一个StringBuilder对象
        StringBuilder stringBuilder = new StringBuilder();
        // 将modelDataFlowable转换到IO线程上
        modelDataFlowable
                // 将modelDataFlowable转换到IO线程上
                .observeOn(Schedulers.io())
                // 将modelDataFlowable中的modelData转换为choices中的第一个delta的内容
                .map(modelData ->
                        modelData.getChoices().get(0).getDelta().getContent()
                )
                // 将message中的所有空格替换为空字符串
                .map(message -> message.replaceAll("\\s", ""))
                // 过滤掉空字符串
                .filter(StrUtil::isNotBlank)
                // 将message转换为字符列表
                .flatMap(message -> {
                    List<Character> characterList = new ArrayList<>();
                    // 遍历message中的每个字符
                    for (char c : message.toCharArray()) {
                        // 将字符添加到characterList中
                        characterList.add(c);
                    }
                    // 将characterList转换为Flowable对象
                    return Flowable.fromIterable(characterList);
                })
                .doOnNext(c -> {
                    //判断字符是否为'{'
                    if (c == '{') {
                        count.addAndGet(1);
                    }
                    //判断字符是否为'}'
                    if (c == '}') {
                        count.addAndGet(-1);
                        //判断count是否为0
                        if (count.get() == 0) {
                            //发送结果
                            sseEmitter.send(JSONUtil.toJsonStr(stringBuilder.toString()));
                            //清空StringBuilder
                            stringBuilder.setLength(0);
                        }
                    }
                    //判断count是否大于0
                    if (count.get() > 0) {
                        //将字符添加到StringBuilder中
                        stringBuilder.append(c);
                    }

                })
                // 当发生错误时，记录错误日志
                .doOnError((e) -> log.error("sseEmitter error", e))
                // 当完成时，完成sseEmitter
                .doOnComplete(sseEmitter::complete)
                // 订阅
                .subscribe();
        // 返回sseEmitter
        return sseEmitter;
    }

    // endregion

    //region AI 答题部分
    private static final String GENERATE_QUESTION_SYSTEM_MESSAGE = "你是一位严谨的出题专家，我会给你如下信息：\n" +
            "```\n" +
            "应用名称，\n" +
            "【【【应用描述】】】，\n" +
            "应用类别，\n" +
            "要生成的题目数，\n" +
            "每个题目的选项数\n" +
            "```\n" +
            "\n" +
            "请你根据上述信息，按照以下步骤来出题：\n" +
            "1. 要求：题目和选项尽可能地短，题目不要包含序号，每题的选项数以我提供的为主，题目不能重复\n" +
            "2. 严格按照下面的 json 格式输出题目和选项\n" +
            "```\n" +
            "[{\"options\":[{\"value\":\"选项内容\",\"key\":\"A\"},{\"value\":\"\",\"key\":\"B\"}],\"title\":\"题目标题\"}]\n" +
            "```\n" +
            "title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如 A、B、C、D）以此类推，value 是选项内容\n" +
            "3. 检查题目是否包含序号，若包含序号则去除序号\n" +
            "4. 返回的题目列表格式必须为 JSON 数组";

    // 根据传入的app、问题编号和选项编号生成系统消息
    private String getGenerateQuestionSystemMessage(App app, int questionNumber, int optionNumber) {
        // 创建一个StringBuilder对象，用于拼接消息
        StringBuilder userMessage = new StringBuilder();
        // 拼接应用名称
        userMessage.append(app.getAppName()).append("\n");
        // 拼接应用描述
        userMessage.append(app.getAppDesc()).append("\n");
        // 根据应用类型获取对应的枚举值，并拼接应用类型
        userMessage.append(AppEnum.getEnumByValue(app.getAppType()).getText()).append("类").append("\n");
        // 拼接问题编号
        userMessage.append(questionNumber).append("\n");
        // 拼接选项编号
        userMessage.append(optionNumber).append("\n");
        // 返回拼接好的消息
        return userMessage.toString();
    }
    //endregion
}

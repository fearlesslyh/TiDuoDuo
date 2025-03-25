package com.lyh.TiDuoDuo.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lyh.TiDuoDuo.model.dto.question.QuestionContent;
import com.lyh.TiDuoDuo.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目视图
 *
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 */
@Data
public class QuestionVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 题目内容（json格式）
     */
    private List<QuestionContent> questionContent;

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 应用描述
     */
    private String appDesc;

    /**
     * 应用图标
     */
    private String appIcon;

    /**
     * 应用类型（0-得分类，1-测评类）
     */
    private Integer appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 审核人 id
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<QuestionContent> questionContent = questionVO.getQuestionContent();
        question.setQuestionContent(JSONUtil.toJsonStr(questionContent));
        return question;
    }

    /**
     * 对象转封装类
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        // 判断传入的对象是否为空
        if (question == null) {
            // 如果为空，则返回null
            return null;
        }
        // 创建一个新的封装类对象
        QuestionVO questionVO = new QuestionVO();
        // 使用BeanUtils工具类将传入的对象的属性值复制到新的封装类对象中
        BeanUtils.copyProperties(question, questionVO);
        String questionContent = question.getQuestionContent();
        if(questionContent != null){
            // 将传入的对象的questionContent属性值转换为QuestionContent类对象，并设置到新的封装类对象中
            questionVO.setQuestionContent(JSONUtil.toList(questionContent, QuestionContent.class));
        }
        // 返回新的封装类对象
        return questionVO;
    }
}

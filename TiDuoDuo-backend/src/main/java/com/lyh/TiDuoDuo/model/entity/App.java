package com.lyh.TiDuoDuo.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 应用
 * @TableName app
 */
@TableName(value ="app")
public class App {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * id
     */
    public Long getId() {
        return id;
    }

    /**
     * id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 应用名
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 应用名
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 应用描述
     */
    public String getAppDesc() {
        return appDesc;
    }

    /**
     * 应用描述
     */
    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    /**
     * 应用图标
     */
    public String getAppIcon() {
        return appIcon;
    }

    /**
     * 应用图标
     */
    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    /**
     * 应用类型（0-得分类，1-测评类）
     */
    public Integer getAppType() {
        return appType;
    }

    /**
     * 应用类型（0-得分类，1-测评类）
     */
    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    /**
     * 评分策略（0-自定义，1-AI）
     */
    public Integer getScoringStrategy() {
        return scoringStrategy;
    }

    /**
     * 评分策略（0-自定义，1-AI）
     */
    public void setScoringStrategy(Integer scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
    }

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    public Integer getReviewStatus() {
        return reviewStatus;
    }

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    /**
     * 审核信息
     */
    public String getReviewMessage() {
        return reviewMessage;
    }

    /**
     * 审核信息
     */
    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    /**
     * 审核人 id
     */
    public Long getReviewerId() {
        return reviewerId;
    }

    /**
     * 审核人 id
     */
    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    /**
     * 审核时间
     */
    public Date getReviewTime() {
        return reviewTime;
    }

    /**
     * 审核时间
     */
    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    /**
     * 创建用户 id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 创建用户 id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 是否删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 是否删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        App other = (App) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAppName() == null ? other.getAppName() == null : this.getAppName().equals(other.getAppName()))
            && (this.getAppDesc() == null ? other.getAppDesc() == null : this.getAppDesc().equals(other.getAppDesc()))
            && (this.getAppIcon() == null ? other.getAppIcon() == null : this.getAppIcon().equals(other.getAppIcon()))
            && (this.getAppType() == null ? other.getAppType() == null : this.getAppType().equals(other.getAppType()))
            && (this.getScoringStrategy() == null ? other.getScoringStrategy() == null : this.getScoringStrategy().equals(other.getScoringStrategy()))
            && (this.getReviewStatus() == null ? other.getReviewStatus() == null : this.getReviewStatus().equals(other.getReviewStatus()))
            && (this.getReviewMessage() == null ? other.getReviewMessage() == null : this.getReviewMessage().equals(other.getReviewMessage()))
            && (this.getReviewerId() == null ? other.getReviewerId() == null : this.getReviewerId().equals(other.getReviewerId()))
            && (this.getReviewTime() == null ? other.getReviewTime() == null : this.getReviewTime().equals(other.getReviewTime()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAppName() == null) ? 0 : getAppName().hashCode());
        result = prime * result + ((getAppDesc() == null) ? 0 : getAppDesc().hashCode());
        result = prime * result + ((getAppIcon() == null) ? 0 : getAppIcon().hashCode());
        result = prime * result + ((getAppType() == null) ? 0 : getAppType().hashCode());
        result = prime * result + ((getScoringStrategy() == null) ? 0 : getScoringStrategy().hashCode());
        result = prime * result + ((getReviewStatus() == null) ? 0 : getReviewStatus().hashCode());
        result = prime * result + ((getReviewMessage() == null) ? 0 : getReviewMessage().hashCode());
        result = prime * result + ((getReviewerId() == null) ? 0 : getReviewerId().hashCode());
        result = prime * result + ((getReviewTime() == null) ? 0 : getReviewTime().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", appName=").append(appName);
        sb.append(", appDesc=").append(appDesc);
        sb.append(", appIcon=").append(appIcon);
        sb.append(", appType=").append(appType);
        sb.append(", scoringStrategy=").append(scoringStrategy);
        sb.append(", reviewStatus=").append(reviewStatus);
        sb.append(", reviewMessage=").append(reviewMessage);
        sb.append(", reviewerId=").append(reviewerId);
        sb.append(", reviewTime=").append(reviewTime);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append("]");
        return sb.toString();
    }
}
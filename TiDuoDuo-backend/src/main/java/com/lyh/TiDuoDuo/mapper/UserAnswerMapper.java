package com.lyh.TiDuoDuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyh.TiDuoDuo.model.dto.statistic.AppAnswerCount;
import com.lyh.TiDuoDuo.model.dto.statistic.AppAnswerResultCount;
import com.lyh.TiDuoDuo.model.entity.UserAnswer;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author 梁懿豪
 * @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
 * @createDate 2025-03-22 22:44:48
 * @Entity generator.domain.UserAnswer
 */
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {

    //group by appId，将 user_answer 内的数据按照 appId 分组
    //统计分组内不同 userId 的数量，得到 appId 和 answerCount 的对应数据
    //根据 answerCount 数据进行排序
    //MyBatis 会将 SQL 结果构建成方法定义的返回类型
    @Select("select appId, count(userId) as answerCount from user_answer " +
            "group by appId order by answerCount desc")
    List<AppAnswerCount> doAppAnswerCount();

    //    where appId = #{appId}，通过 appid 过滤得到 user_answer 中的答题记录数据
//    group by appId，将 user_answer 内的数据按照 appId 分组
//    统计分组内不同 userId 的数量，得到 appId 和 answerCount 的对应数据
//    根据 answerCount 数据进行排序
//    MyBatis 会将 SQL 结果构建成方法定义的返回类型
    @Select("select resultName, count(resultName) as resultCount from user_answer " +
            "where appId = #{appId} group by resultName order by resultCount desc")
    List<AppAnswerResultCount> doAppAnswerResultCount(Long appId);
}





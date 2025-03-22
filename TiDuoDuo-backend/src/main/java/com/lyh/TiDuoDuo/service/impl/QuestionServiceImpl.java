package com.lyh.TiDuoDuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.TiDuoDuo.model.entity.Question;
import com.lyh.TiDuoDuo.service.QuestionService;
import com.lyh.TiDuoDuo.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author RAOYAO
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2025-03-22 22:44:48
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}





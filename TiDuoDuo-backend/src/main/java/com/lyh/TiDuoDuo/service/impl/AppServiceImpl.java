package com.lyh.TiDuoDuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.TiDuoDuo.model.entity.App;
import com.lyh.TiDuoDuo.service.AppService;
import com.lyh.TiDuoDuo.mapper.AppMapper;
import org.springframework.stereotype.Service;

/**
* @author 梁懿豪
* @description 针对表【app(应用)】的数据库操作Service实现
* @createDate 2025-03-22 22:44:48
*/
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>
    implements AppService{

}





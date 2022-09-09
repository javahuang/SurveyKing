package cn.surveyking.server.impl;

import cn.surveyking.server.domain.model.UserBook;
import cn.surveyking.server.mapper.UserBookMapper;
import cn.surveyking.server.service.BaseService;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class UserBookServiceImpl extends BaseService<UserBookMapper, UserBook> implements IService<UserBook> {

}

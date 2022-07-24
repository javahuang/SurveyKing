package cn.surveyking.server.impl;

import cn.surveyking.server.domain.model.CommDictItem;
import cn.surveyking.server.mapper.CommDictItemMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.DictItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author javahuang
 * @date 2022/7/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DictItemServiceImpl extends BaseService<CommDictItemMapper, CommDictItem> implements DictItemService {

}

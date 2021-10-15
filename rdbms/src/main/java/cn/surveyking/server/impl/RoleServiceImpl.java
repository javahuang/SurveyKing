package cn.surveyking.server.impl;

import cn.surveyking.server.domain.model.Role;
import cn.surveyking.server.mapper.RoleMapper;
import cn.surveyking.server.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends BaseService<RoleMapper, Role> {

}

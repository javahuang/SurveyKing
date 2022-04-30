package cn.surveyking.server.impl;

import cn.surveyking.server.domain.model.RepoTemplate;
import cn.surveyking.server.mapper.RepoTemplateMapper;
import cn.surveyking.server.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author javahuang
 * @date 2022/4/29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RepoTemplateServiceImpl extends BaseService<RepoTemplateMapper, RepoTemplate> {

}

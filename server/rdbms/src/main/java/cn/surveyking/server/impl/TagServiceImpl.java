package cn.surveyking.server.impl;

import cn.surveyking.server.core.constant.TagCategoryEnum;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.model.Tag;
import cn.surveyking.server.mapper.TagMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.TagService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author javahuang
 * @date 2022/4/27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagServiceImpl extends BaseService<TagMapper, Tag> implements TagService {

	@Override
	public void batchAddTag(String entityId, TagCategoryEnum category, String[] tagArr) {
		deleteTagByEntryId(entityId);
		if (tagArr == null || tagArr.length == 0) {
			return;
		}
		List<Tag> tagList = new ArrayList<>();
		Arrays.stream(tagArr).forEach(tagName -> {
			Tag tag = new Tag();
			tag.setName(tagName);
			tag.setEntityId(entityId);
			tag.setCategory(category.name());
			tag.setCreateAt(new Date());
			tag.setCreateBy(SecurityContextUtils.getUserId());
			tagList.add(tag);
		});
		if (tagList.size() > 0) {
			saveBatch(tagList);
		}
	}

	@Override
	public void deleteTagByEntryId(String entityId) {
		remove(Wrappers.<Tag>lambdaQuery().eq(Tag::getEntityId, entityId));
	}

}

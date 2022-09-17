package cn.surveyking.server.impl;

import cn.surveyking.server.core.constant.TagCategoryEnum;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.SelectTagRequest;
import cn.surveyking.server.domain.model.Tag;
import cn.surveyking.server.mapper.TagMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.TagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

	@Override
	public Set<String> selectTag(SelectTagRequest request) {
		QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("DISTINCT name").eq("create_by", SecurityContextUtils.getUserId())
				.like(request.getName() != null, "name", request.getName()).last("limit 20");
		Set<String> tags = new HashSet<>();
		list(queryWrapper).stream().filter(x -> x != null).forEach(x -> {
			tags.addAll(Arrays.asList(x.getName()));
		});
		return tags;
	}

}

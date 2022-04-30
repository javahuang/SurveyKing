package cn.surveyking.server.service;

import cn.surveyking.server.core.constant.TagCategoryEnum;

/**
 * @author javahuang
 * @date 2022/4/28
 */
public interface TagService {

	void batchAddTag(String entityId, TagCategoryEnum category, String[] tagArr);

	void deleteTagByEntryId(String entityId);

}

package cn.surveyking.server.impl;

import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.model.Template;
import cn.surveyking.server.domain.model.UserBook;
import cn.surveyking.server.mapper.UserBookMapper;
import cn.surveyking.server.service.BaseService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
*
*/
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserBookServiceImpl extends BaseService<UserBookMapper, UserBook> implements IService<UserBook> {

	private final TemplateServiceImpl templateService;

	/**
	 * 我的错题
	 */
	public static final Integer BOOK_TYPE_WRONG = 1;

	/**
	 * 我的收藏
	 */
	public static final Integer BOOK_TYPE_FAVORITE = 2;

	/**
	 * 保存错题
	 * @param questionScore
	 */
	@Async
	public void saveWrongQuestion(LinkedHashMap<String, Double> questionScore) {
		if (!SecurityContextUtils.isAuthenticated()) {
			return;
		}
		List<String> templateIds = questionScore.entrySet().stream()
				.filter(x -> x.getValue() != null && x.getValue() == 0).map(x -> x.getKey())
				.collect(Collectors.toList());
		List<UserBook> existUserBooks = list(Wrappers.<UserBook>lambdaQuery().in(UserBook::getTemplateId, templateIds)
				.eq(UserBook::getType, BOOK_TYPE_WRONG).eq(UserBook::getCreateBy, SecurityContextUtils.getUserId()));
		existUserBooks.stream().forEach(x -> x.setWrongTimes(x.getWrongTimes() + 1));
		if (existUserBooks.size() > 0) {
			updateBatchById(existUserBooks);
		}

		List<String> existTemplateIds = existUserBooks.stream().map(x -> x.getTemplateId())
				.collect(Collectors.toList());

		List<String> notExistTemplateIds = templateIds.stream().filter(x -> !existTemplateIds.contains(x))
				.collect(Collectors.toList());
		if (notExistTemplateIds.size() > 0) {
			List<Template> templates = templateService.listByIds(notExistTemplateIds);
			saveBatch(templates.stream().map(x -> {
				UserBook userBook = new UserBook();
				userBook.setWrongTimes(1);
				userBook.setTemplateId(x.getId());
				userBook.setName(x.getName());
				userBook.setType(BOOK_TYPE_WRONG);
				return userBook;
			}).collect(Collectors.toList()));
		}
	}

}

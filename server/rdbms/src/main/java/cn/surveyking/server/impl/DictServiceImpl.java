package cn.surveyking.server.impl;

import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.exception.InternalServerError;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.CommDictItemViewMapper;
import cn.surveyking.server.domain.mapper.CommDictViewMapper;
import cn.surveyking.server.domain.model.CommDict;
import cn.surveyking.server.domain.model.CommDictItem;
import cn.surveyking.server.mapper.CommDictMapper;
import cn.surveyking.server.service.BaseService;
import cn.surveyking.server.service.DictService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank;

/**
 * @author javahuang
 * @date 2022/7/19
 */
@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class DictServiceImpl extends BaseService<CommDictMapper, CommDict> implements DictService {

	private final CommDictViewMapper dictViewMapper;

	private final CommDictItemViewMapper dictItemViewMapper;

	private final DictItemServiceImpl dictItemService;

	@Override
	public PaginationResponse<CommDictView> listDict(CommDictQuery query) {
		Page<CommDict> page = pageByQuery(query,
				Wrappers.<CommDict>lambdaQuery().like(isNotBlank(query.getName()), CommDict::getName, query.getName()));
		PaginationResponse<CommDictView> result = new PaginationResponse<>(page.getTotal(),
				dictViewMapper.toView(page.getRecords()));
		return result;
	}

	@Override
	public void addDict(CommDictRequest request) {
		save(dictViewMapper.fromRequest(request));
	}

	@Override
	public void updateDict(CommDictRequest request) {
		updateById(dictViewMapper.fromRequest(request));
	}

	@Override
	public void deleteDict(String id) {
		CommDict commDict = getById(id);
		removeById(id);
		dictItemService.remove(Wrappers.<CommDictItem>lambdaUpdate().eq(CommDictItem::getDictCode, commDict.getCode()));
	}

	@Override
	public PaginationResponse<CommDictItemView> listDictItem(CommDictItemQuery query) {
		Page<CommDictItem> page = dictItemService.pageByQuery(query, Wrappers.<CommDictItem>lambdaQuery()
				.like(isNotBlank(query.getDictCode()), CommDictItem::getDictCode, query.getDictCode()));
		PaginationResponse<CommDictItemView> result = new PaginationResponse<>(page.getTotal(),
				dictItemViewMapper.toView(page.getRecords()));
		return result;
	}

	@Override
	public void saveOrUpdateDictItem(CommDictItemRequest request) {
		dictItemService.saveOrUpdate(dictItemViewMapper.fromRequest(request));
	}

	@Override
	public void deleteDictItem(String id) {
		dictItemService.removeById(id);
	}

	@Override
	@SneakyThrows
	public void importDictItem(CommDictItemRequest request) {
		List<CommDictItem> itemList = new ArrayList<>();
		try (InputStream is = request.getFile().getInputStream(); ReadableWorkbook wb = new ReadableWorkbook(is)) {
			wb.getSheets().forEach(sheet -> {
				try (Stream<Row> rows = sheet.openStream()) {

					rows.forEach(r -> {
						if (r.getRowNum() == 1) {
							return;
						}
						CommDictItem item = new CommDictItem();
						item.setDictCode(request.getDictCode());
						item.setItemName(r.getCellText(0));
						item.setItemValue(r.getCellText(1));
						item.setParentItemValue(r.getCellText(2));
						item.setItemLevel(r.getCellAsNumber(3).orElse(new BigDecimal(1)).intValue());
						item.setItemOrder(r.getCellAsNumber(4).orElse(new BigDecimal(1)).intValue());
						item.setCreateAt(new Date());
						item.setCreateBy(SecurityContextUtils.getUserId());
						itemList.add(item);
						if (itemList.size() == 1000) {
							dictItemService.saveBatch(itemList);
							itemList.clear();
						}
					});
				}
				catch (IOException e) {
					throw new InternalServerError("模板解析失败");
				}
			});
		}
	}

}

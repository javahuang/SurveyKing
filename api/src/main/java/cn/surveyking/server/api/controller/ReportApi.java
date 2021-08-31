package cn.surveyking.server.api.controller;

import cn.surveyking.server.api.service.ReportService;
import cn.surveyking.server.api.domain.dto.ReportData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportApi {

	private final ReportService reportService;

	@GetMapping("/{shortId}")
	public ReportData getData(@PathVariable String shortId) {
		return reportService.getData(shortId);
	}

}

package cn.surveyking.server.api;

import cn.surveyking.server.domain.dto.ReportData;
import cn.surveyking.server.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

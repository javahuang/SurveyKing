package cn.surveyking.server.api;

import cn.surveyking.server.domain.dto.ReportData;
import cn.surveyking.server.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@RestController
@RequestMapping("${api.prefix}/report")
@RequiredArgsConstructor
public class ReportApi {

	private final ReportService reportService;

	@GetMapping("/{shortId}")
	@PreAuthorize("hasAuthority('project:report')")
	public ReportData getData(@PathVariable String shortId) {
		return reportService.getData(shortId);
	}

}

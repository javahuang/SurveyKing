package cn.surveyking.server.api.service;

import cn.surveyking.server.api.domain.dto.AnswerQuery;
import cn.surveyking.server.api.domain.dto.DownloadData;
import cn.surveyking.server.api.domain.model.Answer;
import cn.surveyking.server.api.domain.model.AnswerMetaInfo;
import cn.surveyking.server.core.exception.ServiceException;
import cn.surveyking.server.core.uitls.ExcelExporter;
import cn.surveyking.server.core.uitls.IPUtils;
import cn.surveyking.server.core.uitls.UserAgentUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author javahuang
 * @date 2021/8/3
 */
public interface AnswerService {

	IPage<Answer> listAnswer(AnswerQuery filter);

	Answer getAnswer(AnswerQuery filter);

	void saveAnswer(Answer answer, HttpServletRequest request);

	void updateAnswer(Answer answer);

	void deleteAnswer(String[] ids);

	default AnswerMetaInfo.ClientInfo parseClientInfo(HttpServletRequest request) {
		String userAgentStr = request.getHeader("User-Agent");
		AnswerMetaInfo.ClientInfo clientInfo = UserAgentUtils.parseAgent(userAgentStr);
		clientInfo.setRemoteIp(IPUtils.getClientIpAddress(request));
		return clientInfo;
	}

	default ResponseEntity<Resource> download(String shortId) {
		DownloadData download = getDownloadData(shortId);
		download.setFileName(download.getFileName());
		ExcelExporter excelExporter = new ExcelExporter();
		excelExporter.createSheet(download.getFileName());
		excelExporter.createHeader(download.getHeaderNames());
		excelExporter.createRow(download.getRows());
		try {
			String fileName = URLEncoder.encode(download.getFileName() + ".xlsx", "UTF-8");
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(new InputStreamResource(excelExporter.export()));
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ServiceException("下载失败");
		}

	}

	DownloadData getDownloadData(String shortId);

}

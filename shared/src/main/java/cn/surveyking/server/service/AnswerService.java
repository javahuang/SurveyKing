package cn.surveyking.server.service;

import cn.surveyking.server.core.exception.InternalServerError;
import cn.surveyking.server.core.common.PaginationResponse;
import cn.surveyking.server.core.uitls.IPUtils;
import cn.surveyking.server.core.uitls.UserAgentUtils;
import cn.surveyking.server.domain.dto.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author javahuang
 * @date 2021/8/3
 */
public interface AnswerService {

	PaginationResponse<AnswerView> listAnswer(AnswerQuery filter);

	AnswerView getAnswer(AnswerQuery filter);

	void saveAnswer(AnswerRequest answer, HttpServletRequest request);

	void updateAnswer(AnswerRequest answer);

	void deleteAnswer(String[] ids);

	DownloadData downloadAttachment(DownloadQuery query);

	DownloadData downloadSurvey(String shortId);

	default AnswerMetaInfo.ClientInfo parseClientInfo(HttpServletRequest request) {
		String userAgentStr = request.getHeader("User-Agent");
		AnswerMetaInfo.ClientInfo clientInfo = UserAgentUtils.parseAgent(userAgentStr);
		clientInfo.setRemoteIp(IPUtils.getClientIpAddress(request));
		return clientInfo;
	}

	default ResponseEntity<Resource> download(DownloadQuery query) {
		DownloadData download;
		// 下载问卷答案
		if (query.getType() == DownloadQuery.DownloadType.SURVEY_ANSWER) {
			download = downloadSurvey(query.getShortId());
		}
		// 下载附件
		else if (query.getType() == DownloadQuery.DownloadType.ANSWER_ATTACHMENT) {
			download = downloadAttachment(query);
		}
		else {
			throw new InternalServerError("未知下载类型");
		}
		try {
			String fileName = URLEncoder.encode(download.getFileName(), "UTF-8");
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
					.contentType(download.getMediaType()).body(download.getResource());
		}
		catch (UnsupportedEncodingException e) {
			throw new InternalServerError("下载失败", e);
		}

	}

}

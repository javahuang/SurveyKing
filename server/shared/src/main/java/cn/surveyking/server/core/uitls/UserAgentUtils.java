package cn.surveyking.server.core.uitls;

import cn.surveyking.server.domain.dto.AnswerMetaInfo;
import com.blueconic.browscap.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author javahuang
 * @date 2021/8/8
 */
@Slf4j
@Component
public class UserAgentUtils {

	static UserAgentParser parser;

	/**
	 * 应用启动之后，初始化 parser，该操作耗时长（避免首次答案保存耗时长）。
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@EventListener(ApplicationReadyEvent.class)
	public static UserAgentParser getUserAgent() throws IOException, ParseException {
		if (parser == null) {
			parser = new UserAgentService().loadParser(Arrays.asList(BrowsCapField.BROWSER, BrowsCapField.BROWSER_TYPE,
					BrowsCapField.BROWSER_MAJOR_VERSION, BrowsCapField.DEVICE_TYPE, BrowsCapField.PLATFORM,
					BrowsCapField.PLATFORM_VERSION, BrowsCapField.RENDERING_ENGINE_VERSION,
					BrowsCapField.RENDERING_ENGINE_NAME, BrowsCapField.PLATFORM_MAKER,
					BrowsCapField.RENDERING_ENGINE_MAKER, BrowsCapField.IS_MOBILE_DEVICE));
		}
		return parser;
	}

	public static AnswerMetaInfo.ClientInfo parseAgent(String userAgent) {
		try {
			AnswerMetaInfo.ClientInfo clientInfo = new AnswerMetaInfo.ClientInfo();
			clientInfo.setAgent(userAgent);
			Capabilities capabilities = getUserAgent().parse(userAgent);
			clientInfo.setBrowser(capabilities.getBrowser());
			clientInfo.setPlatform(capabilities.getPlatform());
			clientInfo.setPlatformVersion(capabilities.getPlatformVersion());
			clientInfo.setBrowserVersion(capabilities.getBrowserMajorVersion());
			clientInfo.setDeviceType(capabilities.getDeviceType());
			return clientInfo;
		}
		catch (IOException | ParseException e) {
			e.printStackTrace();
			log.error("userAgent 解析失败 {}", e.getCause());
			return new AnswerMetaInfo.ClientInfo();
		}
	}

}

package cn.surveyking.server.core.uitls;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@Slf4j
public class JSONUtil {

	/**
	 * Transfer object to JSON string
	 * @param object
	 * @return
	 */
	public static String toJSONString(Object object) {
		String result = null;
		ObjectMapper objectMapper = new ObjectMapper();
		// set config of JSON
		// can use single quote
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		try {
			result = objectMapper.writeValueAsString(object);
		}
		catch (JsonProcessingException e) {
			log.error("Generate JSON String error!" + e.getMessage());
			e.printStackTrace();
		}
		return result;

	}

}

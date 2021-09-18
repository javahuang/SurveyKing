package cn.surveyking.server.api.domain.handler;

import cn.surveyking.server.api.domain.model.Answer;
import cn.surveyking.server.core.mybatis.ListTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/17
 */
public class AttachmentListTypeHandler extends ListTypeHandler<List<Answer.Attachment>> {

	private TypeReference typeReference = new TypeReference<List<Answer.Attachment>>() {
	};

	/**
	 * 覆盖方法是为了加快速度，父方法每次都需要反射实例得到JavaType存在性能损耗
	 * @param json
	 * @return
	 */
	@Override
	protected Object parse(String json) {
		try {
			return objectMapper.readValue(json, typeReference);
		}
		catch (IOException var3) {
			throw new RuntimeException(var3);
		}
	}

}
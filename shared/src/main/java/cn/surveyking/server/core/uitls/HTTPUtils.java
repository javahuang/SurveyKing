package cn.surveyking.server.core.uitls;

import lombok.SneakyThrows;

import java.net.URLEncoder;

/**
 * 参考 https://segmentfault.com/a/1190000023601065
 *
 * @author javahuang
 * @date 2022/2/8
 */
public final class HTTPUtils {

	/**
	 * 获取下载文件名，避免中文文件名乱码
	 * @param fileName
	 * @return
	 */
	@SneakyThrows
	public static String getContentDispositionValue(String fileName) {
		// 对真实文件名进行百分号编码
		String percentEncodedFileName = URLEncoder.encode(fileName, "utf-8").replaceAll("\\+", "%20");

		// 组装contentDisposition的值
		StringBuilder contentDispositionValue = new StringBuilder();
		contentDispositionValue.append("attachment; filename=").append(percentEncodedFileName);
		// .append("filename*=").append("utf-8''").append(percentEncodedFileName);
		return contentDispositionValue.toString();
	}

}

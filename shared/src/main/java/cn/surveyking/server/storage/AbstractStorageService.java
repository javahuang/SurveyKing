package cn.surveyking.server.storage;

import lombok.Data;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author javahuang
 * @date 2021/9/8
 */
@Data
public abstract class AbstractStorageService implements StorageService {

	protected StorageProperties storageConfig;

	private int thumbImageWidth;

	private int thumbImageHeight;

	public AbstractStorageService(StorageProperties storageConfig) {
		this.storageConfig = storageConfig;
		this.thumbImageWidth = storageConfig.getThumbImage().getWidth();
		this.thumbImageHeight = storageConfig.getThumbImage().getHeight();
		this.init();
	}

	protected String getThumbPrefixName() {
		StringBuilder buffer = new StringBuilder();
		return buffer.append("_").append(thumbImageWidth).append("x").append(thumbImageHeight).toString();
	}

	protected String getThumbImagePath(String fileName) {
		StringBuilder buff = new StringBuilder(fileName);
		int index = buff.lastIndexOf(".");
		buff.insert(index, getThumbPrefixName());
		return buff.toString();
	}

	protected ByteArrayInputStream generateThumbImageByDefault(InputStream inputStream) throws IOException {
		// 在内存当中生成缩略图
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Thumbnails.of(inputStream).size(thumbImageWidth, thumbImageHeight).toOutputStream(out);
		return new ByteArrayInputStream(out.toByteArray());
	}

}

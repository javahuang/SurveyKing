package cn.surveyking.server.storage;

import cn.surveyking.server.core.constant.ErrorCode;
import cn.surveyking.server.core.exception.ErrorCodeException;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author javahuang
 * @date 2021/9/6
 */
public class LocalStorageService extends AbstractStorageService {

	private Path rootLocation;

	public LocalStorageService(StorageProperties configuration) {
		super(configuration);
	}

	@Override
	public void init() {
		try {
			this.rootLocation = Paths.get(getStorageConfig().getLocal().getRootPath());
			Files.createDirectories(rootLocation);
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void uploadFile(InputStream file, String path) {
		try {
			Files.createDirectories(this.rootLocation.resolve(path).getParent());
			Files.copy(file, this.rootLocation.resolve(path), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new ErrorCodeException(ErrorCode.FileUploadError);
		}
	}

	@Override
	public byte[] download(String filePath) {
		try {
			return Files.readAllBytes(rootLocation.resolve(filePath));
		}
		catch (IOException e) {
			throw new ErrorCodeException(ErrorCode.FileNotExists);
		}
	}

	@Override
	public InputStream downloadAsStream(String filePath) {
		try {
			return Files.newInputStream(rootLocation.resolve(filePath));
		}
		catch (IOException e) {
			throw new ErrorCodeException(ErrorCode.FileNotExists);
		}
	}

}

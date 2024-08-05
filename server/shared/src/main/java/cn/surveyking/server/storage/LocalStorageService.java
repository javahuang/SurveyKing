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
		Path destinationFile = resolvePath(path);

		try (InputStream inputStream = file) {
			Files.createDirectories(destinationFile.getParent());
			Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ErrorCodeException(ErrorCode.FileUploadError);
		}
	}

	private Path resolvePath(String path) {
		Path normalizedPath = Paths.get(path).normalize();
		Path destinationPath = this.rootLocation.resolve(normalizedPath);

		// Ensure the destination path is within the root location
		if (!destinationPath.startsWith(this.rootLocation)) {
			throw new ErrorCodeException(ErrorCode.FileUploadError);
		}

		return destinationPath;
	}

	@Override
	public byte[] download(String filePath) {
		Path resolvedPath = resolvePath(filePath);

		try {
			return Files.readAllBytes(resolvedPath);
		} catch (IOException e) {
			e.printStackTrace();
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

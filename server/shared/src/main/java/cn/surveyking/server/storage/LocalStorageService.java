package cn.surveyking.server.storage;

import cn.surveyking.server.core.constant.ErrorCode;
import cn.surveyking.server.core.exception.ErrorCodeException;
import cn.surveyking.server.core.exception.InternalServerError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author javahuang
 * @date 2021/9/6
 */
public class LocalStorageService extends AbstractStorageService {

	private Path rootLocation;

	AtomicLong seq;

	public LocalStorageService(StorageProperties configuration) {
		super(configuration);
	}

	@Override
	public void init() {
		try {
			this.rootLocation = Paths.get(getStorageConfig().getLocal().getRootPath());
			Files.createDirectories(rootLocation);
			seq = new AtomicLong(new Date().getTime());
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public StorePath uploadFile(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		String filePath = seq.incrementAndGet() + "_" + originalFilename;
		try {
			saveToLocal(filePath, file.getInputStream());
			return new StorePath(filePath, filePath);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new InternalServerError("文件上传失败", e);
		}
	}

	@Override
	public StorePath uploadImage(MultipartFile file) {
		try {
			StorePath storePath = uploadFile(file);
			String thumbFilePath = getThumbImagePath(storePath.getFilePath());
			storePath.setThumbFilePath(thumbFilePath);
			saveToLocal(thumbFilePath, generateThumbImageByDefault(file.getInputStream()));
			return storePath;
		}
		catch (IOException e) {
			throw new ErrorCodeException(ErrorCode.FileUploadError);
		}
	}

	private void saveToLocal(String filePath, InputStream inputStream) {
		try {
			Files.copy(inputStream, this.rootLocation.resolve(filePath), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e) {
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

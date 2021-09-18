package cn.surveyking.server.storage;

import cn.surveyking.server.core.exception.ServiceException;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
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

	Sequence sequence;

	public LocalStorageService(StorageProperties configuration) {
		super(configuration);
	}

	@Override
	public void init() {
		try {
			this.rootLocation = Paths.get(getStorageConfig().getLocal().getRootPath());
			Files.createDirectories(rootLocation);
			this.sequence = new Sequence(InetAddress.getLocalHost());
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
		String filePath = sequence.nextId() + "_" + originalFilename;
		try {
			saveToLocal(filePath, file.getInputStream());
			return new StorePath(filePath, filePath);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("文件上传失败", e);
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
			e.printStackTrace();
			throw new ServiceException("图片上传失败", e);
		}
	}

	private void saveToLocal(String filePath, InputStream inputStream) {
		try {
			Files.copy(inputStream, this.rootLocation.resolve(filePath), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("文件上传失败", e);
		}
	}

	@Override
	public byte[] download(String filePath) {
		try {
			return Files.readAllBytes(rootLocation.resolve(filePath));
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("下载失败", e);
		}
	}

	@Override
	public InputStream downloadAsStream(String filePath) {
		try {
			return Files.newInputStream(rootLocation.resolve(filePath));
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("下载失败", e);
		}
	}

}

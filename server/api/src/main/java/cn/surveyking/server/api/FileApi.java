package cn.surveyking.server.api;

import cn.surveyking.server.domain.dto.FileQuery;
import cn.surveyking.server.domain.dto.FileView;
import cn.surveyking.server.domain.dto.UploadFileRequest;
import cn.surveyking.server.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/10
 */
@RestController
@RequestMapping("${api.prefix}/file")
@AllArgsConstructor
public class FileApi {

	private final FileService fileService;

	/**
	 * 获取文件
	 * @param query
	 * @return
	 */
	@GetMapping
	public ResponseEntity<Resource> getFile(FileQuery query) {
		return fileService.loadFile(query);
	}

	/**
	 * 获取文件列表
	 * @param query
	 * @return
	 */
	@GetMapping("/list")
	public List<FileView> listFiles(FileQuery query) {
		return fileService.listFiles(query);
	}

	/**
	 * 添加文件
	 * @param request
	 * @return
	 */
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('file:import')")
	public FileView upload(UploadFileRequest request) {
		return fileService.upload(request);
	}

	/**
	 * 删除文件
	 * @param request
	 */
	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('file:delete')")
	public void deleteImage(@RequestBody UploadFileRequest request) {
		fileService.deleteFile(request.getId());
	}

	/**
	 * 下载导入 Excel 模板
	 * @param name 导入模板名称
	 * @return
	 */
	@GetMapping("/downloadTemplate")
	public ResponseEntity<Resource> downloadTemplate(String name) {
		return fileService.downloadTemplate(name);
	}

}

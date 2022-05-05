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

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/10
 */
@RestController
@RequestMapping("${api.prefix}/files")
@AllArgsConstructor
public class FileApi {

	private final FileService fileService;

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('file:detail')")
	public ResponseEntity<Resource> getFile(@NotEmpty @PathVariable String id, FileQuery query) {
		query.setId(id);
		return fileService.loadFile(query);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('file:list')")
	public List<FileView> listImages(FileQuery query) {
		return fileService.listFiles(query);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('file:import')")
	public FileView upload(UploadFileRequest request) {
		return fileService.upload(request);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('file:delete')")
	public void deleteImage(@PathVariable() String id) {
		fileService.deleteFile(id);
	}

}

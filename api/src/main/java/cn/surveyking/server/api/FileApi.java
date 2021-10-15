package cn.surveyking.server.api;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.domain.dto.FileView;
import cn.surveyking.server.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

	@GetMapping("/{attachmentId}")
	@PreAuthorize("hasAuthority('file:detail')")
	public ResponseEntity<Resource> preview(@NotEmpty @PathVariable String attachmentId) {
		Resource file = fileService.loadAsResource(attachmentId);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('file:list')")
	public List<FileView> listImages(AppConsts.StorageType storageType) {
		return fileService.listImages(storageType);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('file:import')")
	public FileView upload(@RequestParam("file") MultipartFile file, AppConsts.StorageType storageType) {
		return fileService.upload(file, storageType);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('file:delete')")
	public void deleteImage(@PathVariable() String id) {
		fileService.deleteImage(id);
	}

}

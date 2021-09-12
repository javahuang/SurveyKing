package cn.surveyking.server.api.controller;

import cn.surveyking.server.api.domain.dto.FileView;
import cn.surveyking.server.api.service.FileService;
import cn.surveyking.server.core.constant.AppConsts;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/10
 */
@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
public class FileApi {

	private final FileService fileService;

	@GetMapping("/{attachmentId}")
	public ResponseEntity<Resource> preview(@PathVariable String attachmentId) {
		Resource file = fileService.loadAsResource(attachmentId);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
	}

	@GetMapping
	public List<FileView> listImages(AppConsts.StorageType storageType) {
		return fileService.listImages(storageType);
	}

	@PostMapping
	public FileView upload(@RequestParam("file") MultipartFile file, AppConsts.StorageType storageType) {
		return fileService.upload(file, storageType);
	}

	@DeleteMapping("/{id}")
	public void deleteImage(@PathVariable() String id) {
		fileService.deleteImage(id);
	}

}

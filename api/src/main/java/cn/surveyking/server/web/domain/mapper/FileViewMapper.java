package cn.surveyking.server.web.domain.mapper;

import cn.surveyking.server.web.domain.dto.FileView;
import cn.surveyking.server.web.domain.model.File;
import cn.surveyking.server.storage.StorePath;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/8
 */
@Mapper(componentModel = "spring")
public interface FileViewMapper {

	FileViewMapper INSTANCE = Mappers.getMapper(FileViewMapper.class);

	@Mapping(source = "id", target = "attachmentId")
	@Mapping(source = "originalName", target = "fileName")
    FileView toFileView(File file);

	@Mapping(source = "id", target = "attachmentId")
	@Mapping(source = "originalName", target = "fileName")
	List<FileView> toFileView(List<File> fileList);

	File toFile(StorePath storePath, String originalName, int storageType);

}

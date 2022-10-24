package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.core.base.mapper.BaseModelMapper;
import cn.surveyking.server.domain.dto.FileView;
import cn.surveyking.server.domain.model.File;
import cn.surveyking.server.storage.StorePath;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/8
 */
@Mapper
public interface FileViewMapper extends BaseModelMapper<Void, FileView, File> {

}

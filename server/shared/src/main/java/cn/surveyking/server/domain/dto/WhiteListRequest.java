package cn.surveyking.server.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class WhiteListRequest {

    private List<String> selected = new ArrayList<>();

    private MultipartFile file;
}

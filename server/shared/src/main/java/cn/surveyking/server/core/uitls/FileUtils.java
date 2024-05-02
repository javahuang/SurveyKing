package cn.surveyking.server.core.uitls;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author javahuang
 * @date 2023/11/20
 */
public class FileUtils {

    public static final String[] ALLOWED_EXTENSIONS = {
            // 图像文件
            "jpg", "jpeg", "png", "gif", "bmp", "svg",

            // 文档和文本文件
            "pdf", "txt", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "odt", "ods",

            // 音频文件
            "mp3", "wav", "aac", "ogg", "webm",

            // 视频文件
            "mp4", "mov", "wmv", "avi", "mkv",

            // 其他安全文件类型
            "csv", "json", "xml"
    };

    /**
     * 将文件大小从字节转换为兆字节（MB）。
     * @param bytes 文件大小，以字节为单位。
     * @return 文件大小，以兆字节（MB）为单位，保留三位小数。
     */
    public static Double bytesToMb(long bytes) {
        BigDecimal sizeInBytes = new BigDecimal(bytes);
        BigDecimal bytesInAMb = new BigDecimal(1024 * 1024);
        return sizeInBytes.divide(bytesInAMb, 3, RoundingMode.HALF_UP).doubleValue();
    }

}

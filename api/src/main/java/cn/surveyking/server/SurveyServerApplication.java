package cn.surveyking.server;

import cn.surveyking.server.core.uitls.DatabaseInitHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SurveyServerApplication {

	public static void main(String[] args) {
		// 快速执行数据库初始化操作
		if (args.length > 0 && "i".equalsIgnoreCase(args[0])) {
			DatabaseInitHelper.init();
		}
		SpringApplication.run(SurveyServerApplication.class, args);
	}

}
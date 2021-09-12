package cn.surveyking.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SurveyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveyServerApplication.class, args);
	}

}
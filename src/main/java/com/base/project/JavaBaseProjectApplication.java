package com.base.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.base.project.infrastructure.config.JavaBaseProjectApplicationProperties;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableFeignClients
@SpringBootApplication
@EnableConfigurationProperties(JavaBaseProjectApplicationProperties.class)
public class JavaBaseProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaBaseProjectApplication.class, args);
	}
}

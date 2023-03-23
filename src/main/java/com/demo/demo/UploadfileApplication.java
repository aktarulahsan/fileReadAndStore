package com.demo.demo;

import com.demo.demo.util.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class UploadfileApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(UploadfileApplication.class, args);
	}

}

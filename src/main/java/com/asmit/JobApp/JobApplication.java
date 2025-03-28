package com.asmit.JobApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class JobApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobApplication.class, args);
	}

}

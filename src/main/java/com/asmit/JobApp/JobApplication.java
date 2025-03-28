package com.asmit.JobApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableAspectJAutoProxy

public class JobApplication
{
	public static void main(String[] args)
	{
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(JobApplication.class, args);
	}
}

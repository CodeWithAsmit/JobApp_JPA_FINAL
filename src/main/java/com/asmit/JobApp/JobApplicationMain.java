package com.asmit.JobApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableAspectJAutoProxy
public class JobApplicationMain
{
    public static void main(String[] args)
    {
        try
        {
            Dotenv dotenv = Dotenv.configure().filename(".env").ignoreIfMalformed().ignoreIfMissing().load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        }
        catch (Exception e)
        {
            System.err.println("Failed to load .env file: " + e.getMessage());
        }
        SpringApplication.run(JobApplicationMain.class, args);
    }
}
package com.asmit.JobApp.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendEmailHanlder
{
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendEmail(String to, String subject, String message)
    {
        try
        {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailSender.send(mailMessage);
            System.out.println("Message Send Successfully!");
        }
        catch(MailException e)
        {
            System.out.println("Failed to send email to: " + to + ". Error: " + e.getMessage());
        }
    }
}
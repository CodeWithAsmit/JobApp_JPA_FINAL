package com.asmit.JobApp.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class SendEmailHanlder
{
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String htmlContent)
    {
        try
        {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("Message sent successfully to " + to);
        }
        catch (MailException | MessagingException e)
        {
            System.out.println("Failed to send email to: " + to + ". Error: " + e.getMessage());
        }
    }
}
package com.example.senEmail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;

@Service
public class EmailTemplate {
    @Value("${excel.file.resume}")
    private String excelFilePath;

    private final JavaMailSender mailSender;

    public EmailTemplate(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailWithAttachment(String toEmail) {

        try {
            MimeMessage message = mailSender.createMimeMessage();

            // true = multipart (required for attachment)
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

//            helper.setFrom("yourpersonalgmail@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Application for java developer");

            helper.setText(
                    "<p>Hi,</p>" +

                            "<p>I hope you are doing well.</p>" +

                            "<p>" +
                            "I am interested in the <b>Java Developer</b> opportunity at your organization. " +
                            "I have <b>2.8+ years of experience</b> in " +
                            "<b>Java (8/11/17), Spring Boot, REST APIs, Microservices, JPA/Hibernate, MySQL</b>, " +
                            "and <b>AWS (EC2, S3, Lambda)</b>." +
                            "</p>" +

                            "<p>" +
                            "<b>Current CTC:</b> 4 LPA<br>" +
                            "<b>Expected CTC:</b> Open to discussion<br>" +
                            "<b>Notice Period:</b> Immediate to 15 Days" +
                            "</p>" +

                            "<p>" +
                            "I believe my skills and experience align well with your requirements. " +
                            "Please find my <b>resume attached</b> for your review." +
                            "</p>" +

                            "<p>Looking forward to your response.</p>" +

                            "<p>" +
                            "Warm regards,<br>" +
                            "<b>Satendra Chauhan</b><br>" +
                            "📞 7309191275" +
                            "</p>",
                    true   // 👈 IMPORTANT: true = HTML content
            );


            // 📎 ATTACH FILE
            FileSystemResource file =
                    new FileSystemResource(new File(excelFilePath));

            helper.addAttachment(file.getFilename(), file);

            mailSender.send(message);

            System.out.println("Email sent with attachment to " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

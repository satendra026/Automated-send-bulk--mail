//package com.example.senEmail;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.javamail.MimeMessageHelper;
//
//import java.io.File;
//
//@Service
//public class EmailTemplate {
//    @Value("${excel.file.resume}")
//    private String excelFilePath;
//
//    private final JavaMailSender mailSender;
//
//    public EmailTemplate(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    public void sendEmailWithAttachment(String toEmail) {
//
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//
//            // true = multipart (required for attachment)
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
////            helper.setFrom("yourpersonalgmail@gmail.com");
//            helper.setTo(toEmail);
//            helper.setSubject("Application for java developer");
//
//            helper.setText(
//                    "<p>Hi,</p>" +
//
//                            "<p>I hope you are doing well.</p>" +
//
//                            "<p>" +
//                            "I am interested in the <b>Java Developer</b> opportunity at your organization. " +
//                            "I have <b>2.8+ years of experience</b> in " +
//                            "<b>Java (8/11/17), Spring Boot, REST APIs, Microservices, JPA/Hibernate, MySQL</b>, " +
//                            "and <b>AWS (EC2, S3, Lambda)</b>." +
//                            "</p>" +
//
//                            "<p>" +
//                            "<b>Current CTC:</b> 4 LPA<br>" +
//                            "<b>Expected CTC:</b> Open to discussion<br>" +
//                            "<b>Notice Period:</b> Immediate to 15 Days" +
//                            "</p>" +
//
//                            "<p>" +
//                            "I believe my skills and experience align well with your requirements. " +
//                            "Please find my <b>resume attached</b> for your review." +
//                            "</p>" +
//
//                            "<p>Looking forward to your response.</p>" +
//
//                            "<p>" +
//                            "Warm regards,<br>" +
//                            "<b>Satendra Chauhan</b><br>" +
//                            "📞 7309191275" +
//                            "</p>",
//                    true   // 👈 IMPORTANT: true = HTML content
//            );
//
//
//            // 📎 ATTACH FILE
//            FileSystemResource file =
//                    new FileSystemResource(new File(excelFilePath));
//
//            helper.addAttachment(file.getFilename(), file);
//
//            mailSender.send(message);
//
//            System.out.println("Email sent with attachment to " + toEmail);
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//}




package com.example.senEmail;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailTemplate {

    private static final Logger logger =
            LoggerFactory.getLogger(EmailTemplate.class);

    @Value("${excel.file.resume}")
    private String excelFilePath;

    @Value("${spring.mail.username}")
    private String fromMail;

    private final JavaMailSender mailSender;

    // Cache attachment (avoid disk read every time)
    private FileSystemResource resumeFile;

    public EmailTemplate(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostConstruct
    public void init() {
        resumeFile = new FileSystemResource(new File(excelFilePath));
        logger.info("Resume loaded once from disk");
    }

    // ================= ASYNC METHOD =================
    @Async
    public void sendEmailWithAttachment(String toEmail) {

        long start = System.nanoTime();

        try {
            long t1 = System.nanoTime();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            long t2 = System.nanoTime();

            helper.setFrom(fromMail);
            helper.setTo(toEmail);
            helper.setSubject("Application for Java Developer");

            helper.setText(buildHtmlBody(), true);

            long t3 = System.nanoTime();

            helper.addAttachment(resumeFile.getFilename(), resumeFile);

            long t4 = System.nanoTime();

            mailSender.send(message);

            long end = System.nanoTime();

//            logger.info("Mail sent to {}", toEmail);
//            logger.info("Create message: {} ms", (t2 - t1) / 1_000_000);
//            logger.info("Prepare body: {} ms", (t3 - t2) / 1_000_000);
//            logger.info("Attach file: {} ms", (t4 - t3) / 1_000_000);
//            logger.info("SMTP send time: {} ms", (end - t4) / 1_000_000);
//            logger.info("TOTAL execution time: {} ms",
//                    (end - start) / 1_000_000);

        } catch (MessagingException e) {
            logger.error("Failed to send mail to {}", toEmail, e);
        }
    }

    // ================= HTML BODY =================
    private String buildHtmlBody() {
        return """
                <p>Hi,</p>
                <p>I hope you are doing well.</p>

                <p>
                I am interested in the <b>Java Developer</b> opportunity  at your organization.
                I have <b>3+ years</b> experience in
                <b>Java(8/11/17), Spring Boot, REST APIs, Microservices, JPA/Hibernate, MySQL</b>
                and <b>AWS (EC2, S3, Lambda)</b>.
                </p>

                <p>
                <b>Current CTC:</b> 4 LPA<br>
                <b>Expected CTC:</b> Open to discussion<br>
                <b>Notice Period:</b> Immediate – 15 Days
                </p>

                <p>I believe my skills and experience align well with your requirements. Please find my <b>resume attached</b>.</p>
                
                <p>Looking forward to your response.</p>

                <p>
                    Warm regards,<br>
                    <b>Satendra Chauhan</b><br>
                    📞 <a href="tel:+917309191275">7309191275</a><br>
                    ✉️ <a href="satendrachauhan176@gmail.com">satendrachauhan176@gmail.com</a>
                </p>
                """;
    }
}

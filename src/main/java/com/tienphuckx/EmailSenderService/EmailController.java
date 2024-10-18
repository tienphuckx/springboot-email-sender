package com.tienphuckx.EmailSenderService;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
public class EmailController {
    @Autowired
    private final JavaMailSender mailSender;

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RequestMapping("/send-email")
    public String sendEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("tienphuckx@gmail.com");
            message.setTo("tienphuckx@gmail.com");
            message.setSubject("Phuc test simple text email");
            message.setText("This is a body test message");
            mailSender.send(message);
            return "Email sent successfully";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping("/send-email-with-attachment")
    public String sendEmailWithAttachment() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("tienphuckx@gmail.com");
            helper.setTo("tienphuckx@gmail.com");
            helper.setSubject("Phuc test send email with attachment");
            helper.setText("This is a body of send email with attachment");

            helper.addAttachment("phuc_logo.jpg",
                    new File("C:\\Users\\PC\\Desktop\\avatas\\phuc_logo.jpg"));

            helper.addAttachment("esp32_board.jpg",
                    new File("C:\\Users\\PC\\Desktop\\avatas\\esp32_board.jpg"));

            helper.addAttachment("introduce.txt",
                    new File("C:\\Users\\PC\\Desktop\\avatas\\introduce.txt"));

            // [LINUX]
            /* helper.addInline("phuc_logo.jpg",
                    new File("/home/tienphuckx/dev/dev-resources/phuc_logo.jpg"));
             */

            mailSender.send(message);
            return "Email sent successfully";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping("/send-email-with-html")
    public String sendEmailWithHTML() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("tienphuckx@gmail.com");
            helper.setTo("tienphuckx@gmail.com");
            helper.setSubject("Phuc test send email with HTML and embedded file");

            try (var inputHtml = Objects.requireNonNull(EmailController.class
                    .getResourceAsStream("/templates/welcome-email-template.html"))) {
                helper.setText(
                        new String(inputHtml.readAllBytes(), StandardCharsets.UTF_8), true
                );
            }

            /*
            [WINDOW] add inline file
            helper.addInline("phuc_logo.jpg",
                    new File("C:\\Users\\PC\\Desktop\\avatas\\phuc_logo.jpg")); */

            // [LINUX] add inline file
            helper.addInline("phuc_logo.jpg",
                    new File("/home/tienphuckx/dev/dev-resources/phuc_logo.jpg"));


            mailSender.send(message);
            return "Email sent successfully";
        }catch (Exception e){
            return e.getMessage();
        }
    }
}

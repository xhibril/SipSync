package com.sipsync.sipsync.service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired private JavaMailSender mailSender;
    @Value("${app.base-url}") private String baseUrl;


    @Async
    public void sendVerificationEmail(String email, String token){
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String link = baseUrl + "/email/check-token?token=" + encodedToken;
        try {


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Verify your email");

            String html = """
                    <div style="font-family: Arial; line-height:1.6;">
                        <h2>Verify your email</h2>
                        <p>Click the button below to verify your account:</p>
                        <a href="%s"
                           style="
                             display:inline-block;
                             padding:12px 20px;
                             background: #667085;
                             color:white;
                             text-decoration:none;
                             border-radius:6px;
                             font-weight:bold;">
                           Verify Email
                        </a>
                        <p style="margin-top:20px;font-size:12px;color:#666;">
                            If you didn’t request this, ignore this email.
                        </p>
                    </div>
                    """.formatted(link);

            helper.setText(html, true);
            mailSender.send(message);
            } catch(Exception e){
                e.printStackTrace();
            }
        }




    // send verification to their email
    @Async
    public void sendVerificationCode(String email, String code) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Your verification code");

            String html = """
            <div style="font-family: Arial; line-height:1.6; max-width:480px;">
                <h2 style="margin-bottom:10px;">Verify your account</h2>
                <p>Use the following verification code:</p>

                <div style="
                    margin:20px 0;
                    padding:14px;
                    background:#f2f4f7;
                    border-radius:6px;
                    font-size:24px;
                    font-weight:bold;
                    letter-spacing:4px;
                    text-align:center;
                    color:#667085;">
                    %s
                </div>

                <p style="font-size:13px;color:#666;">
                    This code expires in 10 minutes.
                </p>

                <p style="font-size:12px;color:#999;">
                    If you didn’t request this, you can safely ignore this email.
                </p>
            </div>
            """.formatted(code);

            helper.setText(html, true);
            mailSender.send(message);
          } catch(Exception e) {
            e.printStackTrace();
        }
        }
    }



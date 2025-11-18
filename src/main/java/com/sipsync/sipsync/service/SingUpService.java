package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.repository.SignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;


@Service
public class SingUpService {

    @Autowired SignUpRepository repo;
     @Autowired JavaMailSender mailSender;


    public void addUser(String email, String password){

        User user = new User(email, password);
        repo.save(user);


            // send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("voidedbin@gmail.com");
        message.setSubject("Hi");
        message.setText("Test email");
        mailSender.send(message);



    }
}



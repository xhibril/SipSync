package com.sipsync.sipsync.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityHashService {

    private final PasswordEncoder encoder;
    public SecurityHashService(PasswordEncoder encoder){
        this.encoder = encoder;
    }

    public String hashPassword(String password){
        return  encoder.encode(password);
    }

    public boolean compare(String rawPassword, String hashPassword){
        return encoder.matches(rawPassword, hashPassword);
    }
}

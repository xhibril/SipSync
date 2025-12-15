package com.sipsync.sipsync.model;

import jakarta.persistence.*;

import java.time.Instant;

public class PasswordResetDTO {

        private String email;
        private String code;



    private String password;



        public void setEmail(String email){ this.email = email; }
        public String getEmail(){ return email; }

        public void setCode(String code){ this.code = code; }
        public String getCode(){ return code; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}

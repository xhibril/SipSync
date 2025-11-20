package com.sipsync.sipsync.service;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class Cookies {


    public String getTokenByCookie(HttpServletRequest request){
        // Get all the cookies sent by the browser
        Cookie[] cookies = request.getCookies();

        if(cookies != null){
            for(Cookie cookie : cookies){
                if("authToken".equals(cookie.getName())){
                    // Get the value of that cookie
                    String token = cookie.getValue();
                    return token;
                }
            }
        }
        return "Cookie not found";
    }





}


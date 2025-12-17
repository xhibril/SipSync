package com.sipsync.sipsync.service;
import com.sipsync.sipsync.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired UserRepository userRepo;


    public Long getUserIdByEmail(String email){
        return  userRepo.findIdByEmail(email);
    }

}








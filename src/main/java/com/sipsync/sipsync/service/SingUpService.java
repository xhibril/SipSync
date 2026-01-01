package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SingUpService {
    private final AuthService authService;
    private final UserRepository userRepo;

    private final SecurityHashService hashService;
    public SingUpService(SecurityHashService hashService,
                         AuthService authService,
                         UserRepository userRepo)
    {
        this.hashService = hashService;
        this.authService = authService;
        this.userRepo = userRepo;
    }

    // add user and return it
    public Boolean addUser(String email, String password) {
        // check if user it not already registered
        if (!(userRepo.existsByEmail(email))) {

            String hashPassword = hashService.hashPassword(password);
            User user = new User(email, hashPassword, null);   // null is last streak update date

            // save the saved user for id
            User savedUser = userRepo.save(user);

            // generate token and send email
            authService.generateAuthTokenAfterSignup(savedUser.getId(), savedUser.getEmail());
            return true;
        } else {
            return false;
        }
    }

}



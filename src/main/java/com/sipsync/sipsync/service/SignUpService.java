package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    private final AuthService authService;
    private final UserRepository userRepo;

    private final SecurityHashService hashService;
    public SignUpService(SecurityHashService hashService,
                         AuthService authService,
                         UserRepository userRepo)
    {
        this.hashService = hashService;
        this.authService = authService;
        this.userRepo = userRepo;
    }

    public Boolean addUser(String email, String password) {
        // check if user it not already registered
        if (!(userRepo.existsByEmail(email))) {

            String hashPassword = hashService.hashPassword(password);
            User user = new User(email, hashPassword, null);   // null is last streak update date

            User savedUser = userRepo.save(user);

            // generate token and send email
            authService.generateAuthTokenAfterSignup(savedUser.getId(), savedUser.getEmail());
            return true;
        } else {
            return false;
        }
    }

}



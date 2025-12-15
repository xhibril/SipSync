package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.PasswordResetErrorResponse;
import com.sipsync.sipsync.model.PasswordResetRequest;
import com.sipsync.sipsync.repository.PasswordResetRepository;
import com.sipsync.sipsync.repository.UserRepository;
import io.jsonwebtoken.security.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired UserRepository userRepo;
    @Autowired PasswordResetRepository passwordResetRepo;

    private static final SecureRandom generateCode = new SecureRandom();

    // generate code and save it along with other info
    public Boolean generateVerificationCode(String email){
        //check if user email exists
       Optional<String> isEmailPresent = userRepo.findEmailByEmail(email);

       if(isEmailPresent.isPresent()){
           // 6 digits
           int code = generateCode.nextInt(1_000_000);
           String formattedCode = String.format("%06d", code);

           PasswordResetRequest reset = new PasswordResetRequest();
           reset.setCode(formattedCode);
           reset.setEmail(email);
           reset.setAttemptsRemaining(5);

           // expires in 10 minutes
           reset.setCodeExpiration(Instant.now().plusSeconds(10 * 60));
           passwordResetRepo.save(reset);
           return true;
       }
       return false;
    }


    // compare verification codes
    public ResponseEntity<PasswordResetErrorResponse> compareVerificationCode(String code, String email){

        Optional<PasswordResetRequest> getRow = passwordResetRepo.findByEmail(email);
        if(getRow.isPresent()){
            PasswordResetErrorResponse error; // for error handling

            PasswordResetRequest reset = getRow.get();
            String savedCode = reset.getCode();
            Integer remainingAttempts = reset.getAttemptsRemaining();
            Instant expiration = reset.getCodeExpiration();
            Instant current = Instant.now();

            // check if user has any remaining attempts
            if(remainingAttempts <= 0){
                error = new PasswordResetErrorResponse("No attempts remaining. Please try again", 0);
                return ResponseEntity.badRequest().body(error);
            }

            // check if code is expired
            if(current.isAfter(expiration)){
                error = new PasswordResetErrorResponse("Code has expired. Please try again.", null);
                return ResponseEntity.badRequest().body(error);
            }

            // check if code user entered matches with the one stored in db
            if(!(code.equals(savedCode))){
                remainingAttempts--;
                passwordResetRepo.updateAttemptsRemaining(remainingAttempts, email);

                error = new PasswordResetErrorResponse("Code is not correct. Please try again",  remainingAttempts);
                return ResponseEntity.badRequest().body(error);
            }

            // save reset token that allows user to reset their pass for the next 10 mins
            String resetToken = UUID.randomUUID().toString();
            Instant resetTokenExpiration = Instant.now().plusSeconds(10 * 60);
            passwordResetRepo.addResetToken(resetToken, resetTokenExpiration);

        }
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<PasswordResetErrorResponse> changePassword(String email, String code, String newPassword){

        Optional<PasswordResetRequest> getRow = passwordResetRepo.findByEmail(email);
        if(getRow.isPresent()){
            PasswordResetErrorResponse error; // for error handling

            PasswordResetRequest reset = getRow.get();

            String resetToken = reset.getResetToken();
            Instant expiration = reset.resetTokenExpiration;
            Instant current = Instant.now();

            if (current.isAfter(expiration) || resetToken.equals(null)){
                error = new PasswordResetErrorResponse("Password reset expired. Please try again", null);
                return ResponseEntity.badRequest().body(error);
            }

            // change password
            userRepo.changePassword(newPassword, email);
        }

        return ResponseEntity.ok().build();
    }
}

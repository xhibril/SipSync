package com.sipsync.sipsync.service;
import com.sipsync.sipsync.dto.auth.PasswordResetErrorResponse;
import com.sipsync.sipsync.model.PasswordReset;
import com.sipsync.sipsync.repository.PasswordResetRepository;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired UserRepository userRepo;
    @Autowired PasswordResetRepository passwordResetRepo;
    @Autowired
    JavaMailSender mailSender;

    private static final SecureRandom generateCode = new SecureRandom();

    // generate code and save it along with other info
    @Transactional
    public ResponseEntity<PasswordResetErrorResponse> generateVerificationCode(String email){
        //check if user email exists
       Optional<String> isEmailPresent = userRepo.findEmailByEmail(email);

       if(isEmailPresent.isPresent()){

           // delete prev requests if they are found
           hasExistingPasswordResetRequest(email);

           // 6 digits
           int code = generateCode.nextInt(1_000_000);
           String formattedCode = String.format("%06d", code);

           PasswordReset reset = new PasswordReset();
           reset.setCode(formattedCode);
           reset.setEmail(email);
           reset.setAttemptsRemaining(5);

           // expires in 10 minutes
           reset.setCodeExpiration(Instant.now().plusSeconds(10 * 60));
           passwordResetRepo.save(reset);

           // send the verification code
           sendVerificationCode(email, formattedCode);
           return ResponseEntity.ok().build();
       }
        PasswordResetErrorResponse error; // for error handling
        error = new PasswordResetErrorResponse("This email address is not registered", null);
         return ResponseEntity.badRequest().body(error);
    }

    private void sendVerificationCode(String email, String code){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setText("Your verification code is: " + code);
        message.setSubject("Email verification code");
        mailSender.send(message);
    }


    // check if user has existing password reset requests, if so delete before adding new one
    private void hasExistingPasswordResetRequest(String email){
        if(passwordResetRepo.existsByEmail(email)){
            passwordResetRepo.deleteByEmail(email);
        }
    }




    // compare verification codes
    public ResponseEntity<PasswordResetErrorResponse> compareVerificationCode(String code, String email){

        Optional<PasswordReset> getRow = passwordResetRepo.findByEmail(email);
        if(getRow.isPresent()){
            PasswordResetErrorResponse error; // for error handling

            PasswordReset reset = getRow.get();
            String savedCode = reset.getCode();
            Integer remainingAttempts = reset.getAttemptsRemaining();
            Instant expiration = reset.getCodeExpiration();
            Instant current = Instant.now();

            // check if user has any remaining attempts
            if(remainingAttempts <= 0){
                error = new PasswordResetErrorResponse("Verification attempts exceeded. Please request a new code.", 0);
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

                error = new PasswordResetErrorResponse("Invalid verification code",  remainingAttempts);
                return ResponseEntity.badRequest().body(error);
            }

            // save reset token that allows user to reset their pass for the next 10 mins
            String resetToken = UUID.randomUUID().toString();
            Instant resetTokenExpiration = Instant.now().plusSeconds(10*60);
            passwordResetRepo.addResetToken(resetToken, resetTokenExpiration, email);

        }
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<PasswordResetErrorResponse> changePassword(String email, String code, String newPassword){

        Optional<PasswordReset> getRow = passwordResetRepo.findByEmail(email);
        if(getRow.isPresent()){
            PasswordResetErrorResponse error; // for error handling

            PasswordReset reset = getRow.get();

            String resetToken = reset.getResetToken();
            Instant expiration = reset.resetTokenExpiration;
            Instant current = Instant.now();

            if (current.isAfter(expiration) || resetToken == null){
                error = new PasswordResetErrorResponse("Password reset expired. Please try again", null);
                return ResponseEntity.badRequest().body(error);
            }
            // change password
            userRepo.changePassword(newPassword, email);

            // delete password reset request row
            passwordResetRepo.deleteByEmail(email);
        }

        return ResponseEntity.ok().build();
    }
}

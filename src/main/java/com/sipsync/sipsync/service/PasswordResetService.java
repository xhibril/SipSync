package com.sipsync.sipsync.service;
import com.sipsync.sipsync.dto.auth.PasswordResetResponse;
import com.sipsync.sipsync.model.PasswordReset;
import com.sipsync.sipsync.repository.PasswordResetRepository;
import com.sipsync.sipsync.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
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
    @Autowired JavaMailSender mailSender;

    private final SecurityHashService hashService;
    public PasswordResetService(SecurityHashService hashService){
        this.hashService = hashService;
    }

    private static final SecureRandom secureRandom = new SecureRandom();

    // generate code and save it along with other info
    @Transactional
    public ResponseEntity<PasswordResetResponse> requestPasswordReset(String email) {
        //check if user email exists
        Optional<String> isEmailPresent = userRepo.findEmailByEmail(email);

        if (isEmailPresent.isPresent()) {
            if(passwordResetRepo.existsByEmail(email)){
                // delete prev requests if they are found
                 deletePasswordResetRequest(email);
            }

            // generate new code and send it to email
            String formattedCode = generateVerificationCode(email);
            sendVerificationCode(email, formattedCode);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body(errorHandling("This email address is not registered"));
    }



    // generate verification code
    public String generateVerificationCode(String email) {
        // 6 digits
        int code = secureRandom.nextInt(1_000_000);
        String formattedCode = String.format("%06d", code);

        // hash the code and store it
        PasswordReset reset = new PasswordReset();
        String hashCode = hashService.hashPassword(formattedCode);

        reset.setCode(hashCode);
        reset.setEmail(email);
        reset.setAttemptsRemaining(5);

        // expires in 10 minutes
        reset.setCodeExpiration(Instant.now().plusSeconds(10 * 60));
        passwordResetRepo.save(reset);

        return formattedCode;
    }


    // send verification to their email
    @Async
    protected void sendVerificationCode(String email, String code) {

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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional
    // compare verification codes
    public ResponseEntity<PasswordResetResponse> compareVerificationCode(String userEnteredCode, String email) {
        // check if user has request a password reset request
        Optional<PasswordReset> getRow = passwordResetRepo.findByEmail(email);

        if (getRow.isPresent()) {

            PasswordReset reset = getRow.get();
            String savedCode = reset.getCode();
            Integer remainingAttempts = reset.getAttemptsRemaining();
            Instant expiration = reset.getCodeExpiration();
            Instant current = Instant.now();

            // check if user has any remaining attempts
            if (remainingAttempts <= 0) {
                return ResponseEntity.badRequest().body(errorHandling(
                        "Verification attempts exceeded. Please request a new code."));
            }

            // check if code is expired
            if (current.isAfter(expiration)) {
                return ResponseEntity.badRequest().body(errorHandling(
                        "Code has expired. Please try again."));
            }

            if (!(compareCode(remainingAttempts, savedCode, userEnteredCode, email))) {
                return ResponseEntity.badRequest().body(errorHandling(
                        "Code is incorrect. Please try again."));
            }

            // delete request as verification code job is done
            clearVerificationCode(email);

            // allows password to be changed for next 10 mins
            String resetToken = saveResetToken(email);

            PasswordResetResponse token = new PasswordResetResponse();
            token.setResetToken(resetToken);

            return ResponseEntity.ok().body(token);
        }
        return ResponseEntity.badRequest().body(errorHandling(
                "You have not requested a password reset request"));
    }

    // compare codes
    public boolean compareCode(Integer remainingAttempts, String savedCode, String rawCode, String email) {
        if (!(hashService.compare(rawCode, savedCode))){
            remainingAttempts--;
            passwordResetRepo.updateAttemptsRemaining(remainingAttempts, email);
            return false;
        }
        return true;
    }

    // change password
    @Transactional
    public ResponseEntity<PasswordResetResponse> changePassword(String email, String newPassword, String clientResetToken) {
        Optional<PasswordReset> getRow = passwordResetRepo.findByEmail(email);
        if (getRow.isPresent()) {

            PasswordReset reset = getRow.get();
            String storedResetToken = reset.getResetToken();
            Instant expiration = reset.resetTokenExpiration;
            Instant current = Instant.now();

            // check if reset token has expired
            if (current.isAfter(expiration) || storedResetToken == null) {
                return ResponseEntity.badRequest().body(errorHandling(
                        "Password reset expired. Please try again"));
            }

            if(!compareResetTokens(clientResetToken, storedResetToken)){
                return ResponseEntity.badRequest().body(errorHandling(
                        "Could not reset your password. Please try again"));
            }

            String hashPassword = hashService.hashPassword(newPassword);

            userRepo.changePassword(hashPassword, email);   // change password
            deletePasswordResetRequest(email);      // delete password reset request row
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body(errorHandling(
                "Invalid or expired password reset request"));
    }


    private String saveResetToken(String email) {
        String resetToken = UUID.randomUUID().toString();
        Instant resetTokenExpiration = Instant.now().plusSeconds(10 * 60);
        passwordResetRepo.addResetToken(resetToken, resetTokenExpiration, email);
        return resetToken;
    }

    private boolean compareResetTokens(String clientResetToken, String storedResetToken){
        return clientResetToken.equals(storedResetToken);
    }

    // delete old password request
    private void deletePasswordResetRequest(String email){
        passwordResetRepo.deleteByEmail(email);
    }


    private void clearVerificationCode(String email){
        passwordResetRepo.clearVerificationCode(email);
    }

    // error handling for responses
    private PasswordResetResponse errorHandling(String error) {
        PasswordResetResponse res = new PasswordResetResponse();
        res.setMessage(error);
        return res;
    }
}

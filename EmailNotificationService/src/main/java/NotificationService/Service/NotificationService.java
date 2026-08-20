package NotificationService.Service;

import NotificationService.Dtos.OtpRequest;
import NotificationService.Dtos.RegistrationSuccessRequest;
import NotificationService.Dtos.VerifyOtpRequest;
import NotificationService.Entity.OtpEntity;
import NotificationService.Repository.OtpRepository;
import NotificationService.Util.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private OtpGenerator otpGenerator;


    // Generate OTP
    public String generateOtp(OtpRequest request){

        String otp = otpGenerator.generateOtp();

        OtpEntity entity = new OtpEntity();

        entity.setEmail(request.getEmail());
        entity.setOtp(otp);
        entity.setVerified(false);
        entity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(entity);

        emailService.sendOtp(request.getEmail(), otp);

        return "OTP Sent Successfully";
    }


    // Verify OTP
    public String verifyOtp(VerifyOtpRequest request){

        Optional<OtpEntity> optional =
                otpRepository.findTopByEmailOrderByIdDesc(request.getEmail());


        if(optional.isEmpty()){
            return "Email Not Found";
        }

        OtpEntity entity = optional.get();

        if(entity.isVerified()){
            return "OTP Already Verified";
        }

        if(LocalDateTime.now().isAfter(entity.getExpiryTime())){
            return "OTP Expired";
        }

        if(!entity.getOtp().equals(request.getOtp())){
            return "Invalid OTP";
        }

        entity.setVerified(true);

        otpRepository.save(entity);

        return "OTP Verified Successfully";
    }

    // Registration Success Mail
    public String registrationSuccess(
            RegistrationSuccessRequest request){

        return emailService.registrationSuccessMail(request);

    }

    // Generate Password Reset OTP
    public String generateResetOtp(OtpRequest request){

        String otp = otpGenerator.generateOtp();

        OtpEntity entity = new OtpEntity();

        entity.setEmail(request.getEmail());
        entity.setOtp(otp);
        entity.setVerified(false);
        entity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(entity);

        emailService.sendPasswordResetOtp(request.getEmail(), otp);

        return "OTP Sent Successfully";
    }

    public String passwordResetSuccess(RegistrationSuccessRequest request) {
        return emailService.sendPasswordResetSuccessMail(request.getEmail(), request.getUsername());
    }

}

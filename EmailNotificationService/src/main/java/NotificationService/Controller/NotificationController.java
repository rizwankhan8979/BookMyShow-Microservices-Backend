package NotificationService.Controller;


import NotificationService.Dtos.OtpRequest;
import NotificationService.Dtos.RegistrationSuccessRequest;
import NotificationService.Dtos.TicketNotificationRequestDto;
import NotificationService.Dtos.VerifyOtpRequest;
import NotificationService.Service.EmailService;
import NotificationService.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    // SEND OTP
    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody OtpRequest request){

        return notificationService.generateOtp(request);

    }

    // VERIFY OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody VerifyOtpRequest request){
        return notificationService.verifyOtp(request);

    }

    @PostMapping("/ticket-confirmation")
    public String ticketConfirmation(@RequestBody TicketNotificationRequestDto request) {
        return emailService.sendTicketConfirmationMail(request);
    }


    // REGISTRATION SUCCESS MAIL
    @PostMapping("/registration-success")
    public String registrationSuccess(
            @RequestBody RegistrationSuccessRequest request){

        return notificationService.registrationSuccess(request);

    }

    // SEND RESET OTP
    @PostMapping("/send-reset-otp")
    public String sendResetOtp(@RequestBody OtpRequest request){
        return notificationService.generateResetOtp(request);
    }

    // PASSWORD RESET SUCCESS MAIL
    @PostMapping("/password-reset-success")
    public String passwordResetSuccess(@RequestBody RegistrationSuccessRequest request) {
        return notificationService.passwordResetSuccess(request);
    }

}
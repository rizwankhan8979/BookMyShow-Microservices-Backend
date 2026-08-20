package movieBookingapplication.Feign_Interfaces;

import movieBookingapplication.Dtos.NotfyDto.OtpRequestDto;
import movieBookingapplication.Dtos.NotfyDto.RegistrationSuccessRequestDto;
import movieBookingapplication.Dtos.NotfyDto.TicketNotificationRequestDto;
import movieBookingapplication.Dtos.NotfyDto.VerifyOtpRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "EMAILNOTIFICATIONSERVICE")
public interface NotificationClient {

    @PostMapping("/notification/send-otp")
    String sendOtp(@RequestBody OtpRequestDto request);


    @PostMapping("/notification/verify-otp")
    String verifyOtp(@RequestBody VerifyOtpRequestDto request);


    @PostMapping("/notification/registration-success")
    String registrationSuccess(@RequestBody RegistrationSuccessRequestDto request);

    @PostMapping("/notification/ticket-confirmation")
    String sendTicketConfirmation(@RequestBody TicketNotificationRequestDto request);

    @PostMapping("/notification/send-reset-otp")
    String sendResetOtp(@RequestBody OtpRequestDto request);

    @PostMapping("/notification/password-reset-success")
    String passwordResetSuccess(@RequestBody RegistrationSuccessRequestDto request);

}
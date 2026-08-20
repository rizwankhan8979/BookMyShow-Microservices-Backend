package movieBookingapplication.Controller;

import movieBookingapplication.Dtos.RequestDtos.UserLoginRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.UserLoginResponseDto;
import movieBookingapplication.Dtos.RequestDtos.UserRegisterDto;
import movieBookingapplication.Dtos.ResponseDtos.UserResponseDto;
import movieBookingapplication.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        return ResponseEntity.ok(authenticationService.sendOtp(email));
    }


    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/registernormaluser")
    public ResponseEntity<UserResponseDto> NormalUsers(@RequestBody UserRegisterDto userRegisterDto){
        UserResponseDto res=authenticationService.
                registerNormalUser(userRegisterDto);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/registeradminuser")
    public ResponseEntity<UserResponseDto> AdminUsers(@RequestBody UserRegisterDto userRegisterDto){
        UserResponseDto res=authenticationService.
                registerAdminlUser(userRegisterDto);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto){
        UserLoginResponseDto res=authenticationService.
                loginUser(userLoginRequestDto);
        return ResponseEntity.ok(res);

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(authenticationService.forgotPassword(email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody movieBookingapplication.Dtos.RequestDtos.ResetPasswordDto resetPasswordDto) {
        return ResponseEntity.ok(authenticationService.resetPassword(resetPasswordDto));
    }

}

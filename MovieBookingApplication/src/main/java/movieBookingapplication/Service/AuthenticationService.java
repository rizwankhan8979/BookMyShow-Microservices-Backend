package movieBookingapplication.Service;

import movieBookingapplication.AuthenticationPackege.JwtService;
import movieBookingapplication.Dtos.NotfyDto.OtpRequestDto;
import movieBookingapplication.Dtos.NotfyDto.RegistrationSuccessRequestDto;
import movieBookingapplication.Dtos.NotfyDto.VerifyOtpRequestDto;
import movieBookingapplication.Dtos.RequestDtos.UserLoginRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.UserLoginResponseDto;
import movieBookingapplication.Dtos.RequestDtos.UserRegisterDto;
import movieBookingapplication.Dtos.ResponseDtos.UserResponseDto;
import movieBookingapplication.Entity.User;
import movieBookingapplication.ExceptionHandler.RequiredFiledsExcetion;
import movieBookingapplication.ExceptionHandler.ResourceAlreadyPresentException;
import movieBookingapplication.ExceptionHandler.ResourceNotFoundException;
import movieBookingapplication.Feign_Interfaces.NotificationClient;
import movieBookingapplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationService {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private NotificationClient notificationClient; // Autowire Feign Client(fropm teh feing clit)


    @Value("${admin.secret.key}")
    private String adminSecretKey;




    //yahan  user se response kop create karet hain
    public UserResponseDto mapToResponse(User user){
        UserResponseDto res=new UserResponseDto();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setRoles(user.getRoles());
        return res;
    }


    // 1. Send OTP Method (Direct Call)
    public String sendOtp(String email) {
        return notificationClient.sendOtp(new OtpRequestDto(email));
    }

    public UserResponseDto registerNormalUser(UserRegisterDto userRegisterDto) {

        // A. Check if OTP is provided
        if (userRegisterDto.getOtp() == null || userRegisterDto.getOtp().isEmpty()) {
            throw new RequiredFiledsExcetion("OTP is Required!");
        }

        // B. EmailNotificationService se OTP VERIFY Karwayein
        VerifyOtpRequestDto verifyDto = new VerifyOtpRequestDto(
                userRegisterDto.getEmail(),
                userRegisterDto.getOtp()
        );
        String otpStatus = notificationClient.verifyOtp(verifyDto);


        // Agar OTP Verified NAHI mila, toh yahan hi error phenk do (DATABASE ME SAVE NAHI HOGA)
        if (!"OTP Verified Successfully".equalsIgnoreCase(otpStatus)) {
            throw new RequiredFiledsExcetion("Registration Failed: " + otpStatus);
        }





        //CHECK USER NAME ALREADY REGISTER OR NOT
        Optional<User> optionalUser=
                userRepository.findByUsername(userRegisterDto.getUsername());
        if(optionalUser.isPresent()){
            throw new ResourceAlreadyPresentException("User Is Already Registered..");
        }
        Set<String> roles=new HashSet<>();
        roles.add("ROLE_USER");

        User user=new User();//get user to the set valuse

        user.setUsername(userRegisterDto.getUsername());
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setRoles(roles);
        User savedUser=userRepository.save(user);

        // E. Save hone ke baad Welcome Mail bhej dein (Async to prevent HTTP delay/timeout)
        java.util.concurrent.CompletableFuture.runAsync(() -> {
            try {
                RegistrationSuccessRequestDto successMail = new RegistrationSuccessRequestDto(
                        savedUser.getUsername(),
                        savedUser.getEmail()
                );
                notificationClient.registrationSuccess(successMail);
            } catch (Exception e) {
                System.err.println("Failed to send Welcome Email: " + e.getMessage());
            }
        });

        return mapToResponse(savedUser);

    }



    public UserResponseDto registerAdminlUser(UserRegisterDto userRegisterDto) {

        if (userRegisterDto.getAdminSecret() == null ||
                !userRegisterDto.getAdminSecret().equals(adminSecretKey)) {

            throw new RequiredFiledsExcetion("Unauthorized Access: Invalid or Missing Admin Secret Key!");
        }



        // B. Check OTP Null/Empty toh nahi hai
        if (userRegisterDto.getOtp() == null || userRegisterDto.getOtp().trim().isEmpty()) {
            throw new RequiredFiledsExcetion("OTP is Required to complete admin registration!");
        }

        // C. EmailNotificationService se OTP VERIFY Karwayein
        VerifyOtpRequestDto verifyDto = new VerifyOtpRequestDto(
                userRegisterDto.getEmail(),
                userRegisterDto.getOtp()
        );
        String otpStatus = notificationClient.verifyOtp(verifyDto);


        // Agar OTP Verified NAHI mila, toh DB me save NAHI hoga
        if (!"OTP Verified Successfully".equalsIgnoreCase(otpStatus)) {
            throw new RequiredFiledsExcetion("Admin Registration Failed: " + otpStatus);
        }


        Optional<User> res=userRepository.findByUsername(userRegisterDto.getUsername());
        if(res.isPresent()){
            throw new ResourceAlreadyPresentException("User Is Already Registered..");
        }

        Set<String> roles=new HashSet<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER");

        User user=new User();//thisis the actual user data

        user.setUsername(userRegisterDto.getUsername());
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setRoles(roles);
        User savedUser=userRepository.save(user);


        // F. Welcome Mail Bhej dein
        try {
            RegistrationSuccessRequestDto successMail = new RegistrationSuccessRequestDto(
                    savedUser.getUsername(),
                    savedUser.getEmail()
            );
            notificationClient.registrationSuccess(successMail);
        } catch (Exception e) {
            System.err.println("Failed to send Admin Welcome Email: " + e.getMessage());
        }


        return mapToResponse(savedUser);

    }



    public UserLoginResponseDto loginUser(UserLoginRequestDto userLoginRequestDto) {

        User user = userRepository.findByUsername(userLoginRequestDto.getUsername())
                .or(() -> userRepository.findByEmail(userLoginRequestDto.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequestDto.getUsername(),
                        userLoginRequestDto.getPassword()
                )
        );

        String token=jwtService.generateToken(user);
        return UserLoginResponseDto.builder()
                .jwtToken(token)
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return notificationClient.sendResetOtp(new OtpRequestDto(email));
    }

    public String resetPassword(movieBookingapplication.Dtos.RequestDtos.ResetPasswordDto resetDto) {
        VerifyOtpRequestDto verifyDto = new VerifyOtpRequestDto(
                resetDto.getEmail(),
                resetDto.getOtp()
        );
        String otpStatus = notificationClient.verifyOtp(verifyDto);
        if (!"OTP Verified Successfully".equalsIgnoreCase(otpStatus)) {
            throw new RequiredFiledsExcetion("Password Reset Failed: " + otpStatus);
        }

        User user = userRepository.findByEmail(resetDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + resetDto.getEmail()));
        user.setPassword(passwordEncoder.encode(resetDto.getPassword()));
        userRepository.save(user);

        // Send Password Reset Success Email (Async)
        java.util.concurrent.CompletableFuture.runAsync(() -> {
            try {
                RegistrationSuccessRequestDto successMail = new RegistrationSuccessRequestDto(
                        user.getUsername(),
                        user.getEmail()
                );
                notificationClient.passwordResetSuccess(successMail);
            } catch (Exception e) {
                System.err.println("Failed to send Password Reset Success Email: " + e.getMessage());
            }
        });

        return "Password Reset Successfully";
    }

}

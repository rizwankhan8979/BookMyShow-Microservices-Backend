package movieBookingapplication.Dtos.RequestDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    private String username;
    private String password;
    private String email;
    private String adminSecret;
    private String otp; // <--- ADD THIS FIELD

}


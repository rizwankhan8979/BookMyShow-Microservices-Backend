package movieBookingapplication.Dtos.NotfyDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtpRequestDto {
    private String email;
    private String otp;
}

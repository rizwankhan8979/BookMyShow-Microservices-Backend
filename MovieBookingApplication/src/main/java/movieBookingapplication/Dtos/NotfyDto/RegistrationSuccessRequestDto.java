package movieBookingapplication.Dtos.NotfyDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationSuccessRequestDto {
    private String username;
    private String email;
}

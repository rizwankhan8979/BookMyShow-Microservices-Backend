package movieBookingapplication.Dtos.ResponseDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDto {

    private String jwtToken;
    private String username;
    private Set<String> roles;

}


package movieBookingapplication.Dtos.ResponseDtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.Set;

@Data
@JsonPropertyOrder({"id", "username", "email", "roles"})
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private Set<String> roles;

}

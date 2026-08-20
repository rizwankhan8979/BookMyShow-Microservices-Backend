package movieBookingapplication.Entity;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Data
@Table(name="users")
@Getter
@Setter
@JsonPropertyOrder({"id", "username", "email", "password", "roles"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

}

package NotificationService.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_table")
@Data
@NoArgsConstructor
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String otp;

    private LocalDateTime expiryTime;

    private boolean verified;

    public OtpEntity(Long id, String email, String otp,
                     LocalDateTime expiryTime, boolean verified) {
        this.id = id;
        this.email = email;
        this.otp = otp;
        this.expiryTime = expiryTime;
        this.verified = verified;
    }


}

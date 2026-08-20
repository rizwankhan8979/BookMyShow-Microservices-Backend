package NotificationService.Dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketNotificationRequestDto {
    private String email;
    private String username;
    private Long bookingId;
    private String movieTitle;
    private String theaterName;
    private String screenName;
    private String showTime;
    private List<String> seats;
    private Double amount;
    private String paymentId;
}

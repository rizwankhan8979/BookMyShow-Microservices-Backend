package movieBookingapplication.Dtos.PaymentDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderRequestDto {
    private String name;
    private String email;
    private String phoneNumber;
    private String movieTitle; // Ya bookingInfo (Movie Title / Booking ID)
    private Double amount;
    // Getters and Setters
}

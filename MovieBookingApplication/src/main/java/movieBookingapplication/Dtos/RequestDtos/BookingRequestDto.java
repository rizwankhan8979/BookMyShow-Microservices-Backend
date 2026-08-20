package movieBookingapplication.Dtos.RequestDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import movieBookingapplication.Enum.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {

    @JsonProperty("numberOfSeats")
    private Integer numberOfSeats;

    private LocalDateTime bookingTime;
    private Double price;
    private BookingStatus bookingStatus;
    private List<String> seatsNumbers;
    private Long showId;

}


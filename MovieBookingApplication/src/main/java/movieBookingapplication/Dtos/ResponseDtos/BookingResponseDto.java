package movieBookingapplication.Dtos.ResponseDtos;

import lombok.Data;
import movieBookingapplication.Enum.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponseDto {

    private Long id;
    private Integer numberOfSeats;
    private LocalDateTime bookingTime;
    private Double price;
    private BookingStatus bookingStatus;
    private List<String> seatsNumbers;
    // YEH LINE ADD KAREIN:
    private String razorpayOrderId;

    // User Details
    private Long userId;
    private String username;

    // Show Details
    private Long showId;
    private LocalDateTime showTime;

    // Movie Details
    private Long movieId;
    private String movieTitle;

    // Screen Details
    private Long screenId;
    private String screenName;

}

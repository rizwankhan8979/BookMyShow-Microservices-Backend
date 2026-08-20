package movieBookingapplication.Dtos.ResponseDtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowResponseDto {
    private Long id;
    private LocalDateTime showTime;
    private Double price;
    private Long movieId;
    private String movieTitle;
    private Long screenId;
    private String screenName;
    private Long theaterId;
    private String theaterName;
    private String theaterAddress;

}

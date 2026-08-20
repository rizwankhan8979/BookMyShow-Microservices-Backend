package movieBookingapplication.Dtos.ResponseDtos;

import lombok.Data;
import movieBookingapplication.Enum.SeatType;

@Data
public class SeatResponseDto {

    private Long id;
    private String seatNumber;
    private SeatType seatType;
    private Double price;
    private Long screenId;
    private String screenName;

}

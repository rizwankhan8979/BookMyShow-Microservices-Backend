package movieBookingapplication.Dtos.ResponseDtos;

import lombok.Data;
import movieBookingapplication.Enum.SeatType;
import movieBookingapplication.Enum.ShowSeatStatus;

@Data
public class ShowSeatResponse {

    private Long id;
    private String seatNumber;
    private Double price;
    private ShowSeatStatus status;
    private Long showId;
    private Long seatId;
    private String screenName;
    private SeatType seatType;

}

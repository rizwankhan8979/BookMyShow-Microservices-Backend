package movieBookingapplication.Dtos.RequestDtos;

import lombok.Data;
import movieBookingapplication.Enum.SeatType;

@Data
public class SeatRequestDto {

    private String seatNumber;
    private SeatType seatType;
    private Double price;
    private Long screenId;
}
package movieBookingapplication.Dtos.RequestDtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowRequestDto {

    private LocalDateTime showTime;
    private Double price;
    private Long movieId;
    private Long screenId;

}

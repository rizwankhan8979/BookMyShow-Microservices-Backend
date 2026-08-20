package movieBookingapplication.Dtos.ResponseDtos;

import lombok.Data;

@Data
public class ScreenResponseDto {

    private Long id;
    private String screenName;
    private Integer capacity;
    private Long theaterId;
    private String theaterName;

}

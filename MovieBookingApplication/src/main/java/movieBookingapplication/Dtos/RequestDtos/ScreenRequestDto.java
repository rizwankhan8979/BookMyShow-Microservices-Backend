package movieBookingapplication.Dtos.RequestDtos;

import lombok.Data;

@Data
public class ScreenRequestDto {

    private String screenName; // "Screen 1"
    private Integer capacity;   // 150
    private Long theaterId;    // 1 (Kis Theater me add karni hai)
}
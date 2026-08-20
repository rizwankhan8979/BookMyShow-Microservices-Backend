package movieBookingapplication.Dtos.ResponseDtos;

import lombok.Data;

import java.util.List;

@Data
public class TheaterResponseDto {

    private long id;
    private String theatername;
    private String location;
    private String address;
    private List<ScreenResponseDto> screens;

}

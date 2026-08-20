package movieBookingapplication.Dtos.ResponseDtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieResponseDto {

    private Long id;

    private String title;
    private String description;
    private String genre;
    private String language;
    private String durationMins;
    private LocalDateTime releaseDate;
    private String posterUrl;

}

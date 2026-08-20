package movieBookingapplication.Dtos.RequestDtos;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequestDto {

    private String title;
    private String description;
    private String genre;
    private String language;
    private String durationMins;
    private LocalDateTime releaseDate;
    private String posterUrl;

}

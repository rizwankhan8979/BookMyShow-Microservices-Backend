package movieBookingapplication.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name="movies")
@JsonPropertyOrder({"id", "title", "description", "genre", "language", "durationMins", "releaseDate", "posterUrl"})
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String genre;
    private String language;
    private String durationMins;
    private LocalDateTime releaseDate;
    private String posterUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Show> show;

}

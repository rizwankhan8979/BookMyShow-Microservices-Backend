package movieBookingapplication.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="screens")

public class Screen {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String screenName;
    private Integer capacity; //seat capacity in the Screen


    //THEATER KE SATH CONNECTION
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="theater_id", nullable = false)
    private Theater theater;

    @JsonIgnore//yahan per screen jo wo seat se add ho rahi ahia
    @OneToMany(mappedBy = "screen", fetch = FetchType.LAZY)
    private List<Seat> seats;

    @JsonIgnore
    @OneToMany(mappedBy = "screen", fetch = FetchType.LAZY)
    private List<Show> shows;


}
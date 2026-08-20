package movieBookingapplication.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name="shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime showTime;
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="screen_id", nullable = false)
    private Screen screen; //Show conncet with the screen

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="movie_id", nullable = false)
    private Movie movie;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "show", fetch = FetchType.LAZY)
    private List<Booking> bookings;



}

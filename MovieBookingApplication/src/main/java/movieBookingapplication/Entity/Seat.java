package movieBookingapplication.Entity;

import jakarta.persistence.*;
import lombok.Data;
import movieBookingapplication.Enum.SeatType;

@Entity
@Data
@Table(name="seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seatNumber;
    @Enumerated(EnumType.STRING)
    private SeatType seatType;
    private Double price;

    //Many Seats -> One Screen
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="screen_id", nullable = false)
    private Screen screen;

}
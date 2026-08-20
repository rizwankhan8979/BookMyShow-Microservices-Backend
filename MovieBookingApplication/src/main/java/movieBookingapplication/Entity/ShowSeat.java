package movieBookingapplication.Entity;

import jakarta.persistence.*;
import lombok.Data;
import movieBookingapplication.Enum.ShowSeatStatus;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="show_seats")
public class ShowSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber; // "A1"
    private Double price;      // 500.0

    @Enumerated(EnumType.STRING)
    private ShowSeatStatus status; // AVAILABLE, LOCKED, BOOKED

    private LocalDateTime lockTimestamp; // 10 minute expiry check ke liye

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="show_id")
    private Show show;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="seat_id")
    private Seat seat;
}
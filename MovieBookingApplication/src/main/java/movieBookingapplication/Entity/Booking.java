package movieBookingapplication.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import movieBookingapplication.Enum.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numberOfSeats;
    private LocalDateTime bookingTime;
    private Double price;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    // Payment Details (Frontend ke Razorpay Checkout ke liye)
    private String razorpayOrderId;
    private String razorpayPaymentId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="bookings_seats_numbers")
    private List<String> seatsNumbers;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="show_id", nullable = false)
    private Show show;


}

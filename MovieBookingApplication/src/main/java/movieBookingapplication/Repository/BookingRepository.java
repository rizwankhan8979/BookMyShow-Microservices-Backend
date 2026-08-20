package movieBookingapplication.Repository;

import movieBookingapplication.Entity.Booking;
import movieBookingapplication.Enum.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long id);
    List<Booking> findByShowId(Long id);
    List<Booking> findByBookingStatus(BookingStatus bookingStatus);
}

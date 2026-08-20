package movieBookingapplication.Repository;

import movieBookingapplication.Entity.ShowSeat;
import movieBookingapplication.Enum.ShowSeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShowId(Long showId);

    List<ShowSeat> findByShowIdAndStatus(Long showId, ShowSeatStatus status);

    List<ShowSeat> findByShowIdAndSeatNumberIn(Long showId, List<String> seatNumbers);
}

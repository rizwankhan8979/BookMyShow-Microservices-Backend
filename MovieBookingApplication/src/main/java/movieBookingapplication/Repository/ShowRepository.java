package movieBookingapplication.Repository;

import movieBookingapplication.Entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {

    Optional<List<Show>> findByMovieId(Long id);
    Optional<List<Show>> findByScreenId(Long id);
    Optional<List<Show>> findByScreenTheaterId(Long theaterId);
}

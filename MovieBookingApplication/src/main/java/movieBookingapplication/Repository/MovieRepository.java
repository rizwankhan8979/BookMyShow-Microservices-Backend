package movieBookingapplication.Repository;

import movieBookingapplication.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByGenreContainingIgnoreCase(String genre);
    List<Movie> findByLanguageContainingIgnoreCase(String language);
    List<Movie> findByTitleContainingIgnoreCase(String title);
}

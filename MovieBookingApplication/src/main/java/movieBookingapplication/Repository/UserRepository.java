package movieBookingapplication.Repository;

import movieBookingapplication.Entity.User;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String  username);
    Optional<User> findByEmail(String email);
}

package mp.BookingSystem.repository;

import mp.BookingSystem.model.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByToken(String token);
}

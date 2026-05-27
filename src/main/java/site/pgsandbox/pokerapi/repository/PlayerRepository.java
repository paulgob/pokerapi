package site.pgsandbox.pokerapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.pgsandbox.pokerapi.model.player.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsername(String username);
}

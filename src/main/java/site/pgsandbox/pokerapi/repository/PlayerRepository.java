package site.pgsandbox.pokerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pgsandbox.pokerapi.model.player.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {}

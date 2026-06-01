package site.pgsandbox.pokerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pgsandbox.pokerapi.model.game.Game;

public interface GameRepository extends JpaRepository<Game, Long> {}

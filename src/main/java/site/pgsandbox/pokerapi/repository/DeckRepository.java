package site.pgsandbox.pokerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pgsandbox.pokerapi.model.card.Deck;

public interface DeckRepository extends JpaRepository<Deck, Long> {}

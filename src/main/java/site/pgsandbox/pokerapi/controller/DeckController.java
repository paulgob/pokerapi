package site.pgsandbox.pokerapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.pgsandbox.pokerapi.exception.card.*;
import site.pgsandbox.pokerapi.model.card.Card;
import site.pgsandbox.pokerapi.model.card.Deck;
import site.pgsandbox.pokerapi.service.DeckService;

@RestController
public class DeckController {

    private final DeckService service;

    DeckController(DeckService service) {
        this.service = service;
    }

    @PostMapping("/deck")
    public Deck newDeck() {
        return service.createADeck();
    }

    @GetMapping("/deck/{id}/shuffle")
    public Deck deckShuffled(@PathVariable Long id) {
        return service.shuffle(id);
    }

    @GetMapping("/deck/{id}/get-a-card")
    public Card getACardFromDeck(@PathVariable Long id) {
        return service.getACard(id);
    }
}

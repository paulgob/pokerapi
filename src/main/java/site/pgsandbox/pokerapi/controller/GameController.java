package site.pgsandbox.pokerapi.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import site.pgsandbox.pokerapi.model.game.Game;
import site.pgsandbox.pokerapi.model.game.Status;
import site.pgsandbox.pokerapi.service.GameService;

@RestController
public class GameController {

    private final GameService service;

    GameController(GameService service) {
        this.service = service;
    }

    @PostMapping("/game")
    public Game newGame(Long tableId, Long deckId) {
        return service.createAGame(tableId, deckId);
    }

    @GetMapping("/games")
    public List<Game> allGames() {
        return service.getAllGames();
    }

    @GetMapping("/game/{id}")
    public Game findGame(@PathVariable Long id) {
        return service.getGameById(id);
    }

    @PutMapping("/game/{id}")
    public Game updateGame(@PathVariable Long id, Status status) {
        return service.updateGame(id, status);
    }

    @PutMapping("/game/{id}/start")
    public Game startGame(@PathVariable Long id) {
        return service.startGame(id);
    }

    @DeleteMapping("/game/{id}")
    public void deleteGame(@PathVariable Long id) {
        service.deleteGame(id);
    }
}

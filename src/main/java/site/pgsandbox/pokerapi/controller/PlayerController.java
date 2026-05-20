package site.pgsandbox.pokerapi.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import site.pgsandbox.pokerapi.model.player.Player;
import site.pgsandbox.pokerapi.service.PlayerService;

@RestController
public class PlayerController {

    private final PlayerService service;

    PlayerController(PlayerService service) {
        this.service = service;
    }

    @PostMapping("/player")
    public Player newPlayer(String username, int chips) {
        return service.createAPlayer(username, chips);
    }

    @GetMapping("/players")
    public List<Player> allPlayer() {
        return service.getAllPlayer();
    }

    @GetMapping("/player/{id}")
    public Player findPlayer(@PathVariable Long id) {
        return service.getPlayerById(id);
    }

    @PutMapping("/player/{id}")
    public Player updateplayer(
        @PathVariable Long id,
        String username,
        int chips
    ) {
        return service.updatePlayer(id, username, chips);
    }

    @DeleteMapping("/player/{id}")
    public void deletePlayer(@PathVariable Long id) {
        service.deletePlayer(id);
    }
}

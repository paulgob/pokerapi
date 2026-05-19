package site.pgsandbox.pokerapi.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
}

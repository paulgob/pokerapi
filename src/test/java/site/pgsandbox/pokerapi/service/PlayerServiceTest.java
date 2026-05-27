package site.pgsandbox.pokerapi.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pgsandbox.pokerapi.exception.player.DuplicateUsernameException;

@SpringBootTest
public class PlayerServiceTest {

    @Autowired
    private PlayerService service;

    @Test
    void UsernameMustBeUnique() {
        service.createAPlayer("Test", 100);

        assertThrows(DuplicateUsernameException.class, () ->
            service.createAPlayer("Test", 100)
        );
    }
}

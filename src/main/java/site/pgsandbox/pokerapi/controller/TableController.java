package site.pgsandbox.pokerapi.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import site.pgsandbox.pokerapi.model.table.Table;
import site.pgsandbox.pokerapi.service.TableService;

@RestController
public class TableController {

    private final TableService service;

    TableController(TableService service) {
        this.service = service;
    }

    @PostMapping("/Table")
    public Table newTable(int maxPlayer) {
        return service.createATable(maxPlayer);
    }

    @GetMapping("/Tables")
    public List<Table> allTable() {
        return service.getAllTables();
    }

    @GetMapping("/Table/{id}")
    public Table findTable(@PathVariable Long id) {
        return service.getTableById(id);
    }

    @PutMapping("/Table/{id}")
    public Table updateTable(@PathVariable Long id, int maxPlayer) {
        return service.updateTable(id, maxPlayer);
    }

    @DeleteMapping("/Table/{id}")
    public void deleteTable(@PathVariable Long id) {
        service.deleteTable(id);
    }
}

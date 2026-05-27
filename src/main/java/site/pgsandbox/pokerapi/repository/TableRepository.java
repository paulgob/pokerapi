package site.pgsandbox.pokerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pgsandbox.pokerapi.model.table.Table;

public interface TableRepository extends JpaRepository<Table, Long> {}

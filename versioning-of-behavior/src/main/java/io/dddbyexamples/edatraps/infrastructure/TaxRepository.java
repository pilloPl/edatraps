package io.dddbyexamples.edatraps.infrastructure;

import io.dddbyexamples.edatraps.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRepository extends JpaRepository<Tax, Long> {

    Tax findBySessionId(String sessionId);
}

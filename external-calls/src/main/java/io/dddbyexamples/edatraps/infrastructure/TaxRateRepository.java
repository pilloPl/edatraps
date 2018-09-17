package io.dddbyexamples.edatraps.infrastructure;

import io.dddbyexamples.edatraps.model.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {

    Optional<TaxRate> findByCommodity(String commodity);
}

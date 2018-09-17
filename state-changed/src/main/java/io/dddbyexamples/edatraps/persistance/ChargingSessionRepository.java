package io.dddbyexamples.edatraps.persistance;

import io.dddbyexamples.edatraps.model.ChargingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargingSessionRepository extends JpaRepository<ChargingSession, String> {
}

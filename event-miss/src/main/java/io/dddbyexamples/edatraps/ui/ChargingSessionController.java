package io.dddbyexamples.edatraps.ui;

import io.dddbyexamples.edatraps.infrastructure.ChargingSessionRepository;
import io.dddbyexamples.edatraps.model.ChargingSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.Instant;

@RestController
class ChargingSessionController {

    private final ChargingSessionRepository chargingSessionRepository;

    ChargingSessionController(ChargingSessionRepository chargingSessionRepository) {
        this.chargingSessionRepository = chargingSessionRepository;
    }

    @PostMapping("/starts")
    public ResponseEntity startSession(@RequestBody StartTransaction startTransaction) {
        chargingSessionRepository.save(new ChargingSession(startTransaction.getId(), Instant.now()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ends")
    @Transactional
    public ResponseEntity stopSession(@RequestBody StopTransaction stopTransaction) {
        try {
            ChargingSession session = chargingSessionRepository.findById(stopTransaction.getId()).orElseThrow(() -> new IllegalStateException("Session was never started!"));
            session.finishedAt(Instant.now());
        } catch (Exception e) {
            ChargingSession session = new ChargingSession(stopTransaction.getId(), null);
            //chargingSessionRepository.findById(stopTransaction.getId()).orElseThrow(() -> new IllegalStateException("Session was never started!"));
            session.finishedAt(Instant.now());
            chargingSessionRepository.save(session);
        }

        return ResponseEntity.ok().build();
    }


}



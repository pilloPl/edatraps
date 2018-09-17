package io.dddbyexamples.edatraps.ui;

import io.dddbyexamples.edatraps.infrastructure.ChargingSessionRepository;
import io.dddbyexamples.edatraps.infrastructure.DomainEventPublisher;
import io.dddbyexamples.edatraps.model.ChargingSession;
import io.dddbyexamples.edatraps.model.ChargingSessionFinished;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

import java.math.BigDecimal;

import static java.time.Instant.now;

@RestController
class ChargingSessionController {

    private final DomainEventPublisher eventPublisher;
    private final ChargingSessionRepository chargingSessionRepository;

    ChargingSessionController(DomainEventPublisher eventPublisher, ChargingSessionRepository chargingSessionRepository) {
        this.eventPublisher = eventPublisher;
        this.chargingSessionRepository = chargingSessionRepository;
    }

    @PostMapping("/starts")
    public ResponseEntity startSession(@RequestBody StartTransaction startTransaction) {
        chargingSessionRepository.save(new ChargingSession(startTransaction.getId(), now()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ends")
    @Transactional
    public ResponseEntity stopSession(@RequestBody StopTransaction stopTransaction) {
        ChargingSession session = chargingSessionRepository.findById(stopTransaction.getId())
                .orElseThrow(() -> new IllegalStateException("Session was never started!"));
        session.finishedAt(now());
        eventPublisher.saveAndPublishAfterCommit(new ChargingSessionFinished(stopTransaction.getId(), now(), BigDecimal.TEN));
        return ResponseEntity.ok().build();
    }


}



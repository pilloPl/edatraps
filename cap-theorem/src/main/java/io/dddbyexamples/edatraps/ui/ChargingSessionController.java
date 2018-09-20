package io.dddbyexamples.edatraps.ui;

import io.dddbyexamples.edatraps.infrastructure.ChargingSessionRepository;
import io.dddbyexamples.edatraps.infrastructure.DomainEventPublisher;
import io.dddbyexamples.edatraps.model.ChargingSession;
import io.dddbyexamples.edatraps.model.ChargingSessionFinished;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.time.Instant.now;

@RestController
class ChargingSessionController {

    private final DomainEventPublisher eventPublisher;
    private final Source source;
    private final ChargingSessionRepository chargingSessionRepository;

    ChargingSessionController(DomainEventPublisher eventPublisher, Source source, ChargingSessionRepository chargingSessionRepository) {
        this.eventPublisher = eventPublisher;
        this.source = source;
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
        eventPublisher.save(new ChargingSessionFinished(stopTransaction.getId(), now(), BigDecimal.TEN));

        return ResponseEntity.ok().build();
    }

    private void sendEvent(ChargingSessionFinished event) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", event.getType());
        source.output().send(new GenericMessage<>(event, headers));
    }


}



package io.dddbyexamples.edatraps.ui;

import io.dddbyexamples.edatraps.infrastructure.ChargingSessionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
class ChargingSessionController {

    private final ChargingSessionRepository chargingSessionRepository;

    ChargingSessionController(ChargingSessionRepository chargingSessionRepository) {
        this.chargingSessionRepository = chargingSessionRepository;
    }

    @PostMapping("/starts")
    public ResponseEntity startSession(@RequestBody StartTransaction startTransaction) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ends")
    @Transactional
    public ResponseEntity stopSession(@RequestBody StopTransaction stopTransaction) {
        return ResponseEntity.ok().build();
    }


}



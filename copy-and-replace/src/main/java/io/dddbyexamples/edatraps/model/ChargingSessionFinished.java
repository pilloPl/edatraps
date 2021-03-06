package io.dddbyexamples.edatraps.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChargingSessionFinished implements DomainEvent {

    private String sessionId;
    private Instant when;
    private BigDecimal cost;

    @Override
    public String getType() {
        return "session-finished";
    }
}

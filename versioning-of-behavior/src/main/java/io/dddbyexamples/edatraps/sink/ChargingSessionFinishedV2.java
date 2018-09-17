package io.dddbyexamples.edatraps.sink;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChargingSessionFinishedV2 implements DomainEvent {

    private String sessionId;
    private Instant when;
    private BigDecimal cost;
    private BigDecimal tax;

    @Override
    public String getType() {
        return "session-finished-v2";
    }
}

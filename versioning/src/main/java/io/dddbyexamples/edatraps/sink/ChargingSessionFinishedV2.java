package io.dddbyexamples.edatraps.sink;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChargingSessionFinishedV2 implements DomainEvent {

    private String sessionId;
    private BigDecimal cost;
    private String customer;

    @Override
    public String getType() {
        return "session-finished-v2";
    }
}

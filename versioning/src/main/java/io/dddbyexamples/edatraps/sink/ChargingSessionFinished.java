package io.dddbyexamples.edatraps.sink;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChargingSessionFinished implements DomainEvent {

    private String sessionId;
    private BigDecimal cost;
    private String customerName = "we did not care";

    @Override
    public String getType() {
        return "session-finished";
    }
}

package io.dddbyexamples.edatraps.sink;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaxRateChanged implements DomainEvent {

    private Instant when;
    private BigDecimal newRate;
    private String commodity;

    @Override
    public String getType() {
        return "tax-rate-changed";
    }
}

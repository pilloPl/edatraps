package io.dddbyexamples.edatraps.sink;

import io.dddbyexamples.edatraps.infrastructure.TaxRepository;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TaxCalculator {

    private final TaxRepository taxRepository;

    public TaxCalculator(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }


    @StreamListener(target = Sink.INPUT)
    public void handle(ChargingSessionFinishedV2 event) {

    }

    private BigDecimal getRate() {
        return null;
    }


}

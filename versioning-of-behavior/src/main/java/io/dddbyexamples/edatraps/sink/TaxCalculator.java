package io.dddbyexamples.edatraps.sink;

import io.dddbyexamples.edatraps.infrastructure.TaxRepository;
import io.dddbyexamples.edatraps.model.Tax;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZoneId;

@Component
public class TaxCalculator {

    private final TaxRepository taxRepository;

    public TaxCalculator(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }


    @StreamListener(target = Sink.INPUT)
    public void handle(ChargingSessionFinishedV2 event) {
        BigDecimal rate = getRate(event);
        taxRepository.save(new Tax(event.getSessionId(), rate.multiply(event.getCost())));
    }

    private BigDecimal getRate(ChargingSessionFinishedV2 event) {
        return event.getTax();
    }


}

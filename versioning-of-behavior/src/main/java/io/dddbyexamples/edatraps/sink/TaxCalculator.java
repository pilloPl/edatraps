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

    @StreamListener(target = Sink.INPUT, condition = "headers['type'] == 'session-finished'")
    public void handle(ChargingSessionFinished event) {
        BigDecimal tax = getTax(event);
        taxRepository.save(new Tax(event.getSessionId(), tax.multiply(event.getCost())));

    }

    @StreamListener(target = Sink.INPUT, condition = "headers['type'] == 'session-finished-v2'")
    public void handleV2(ChargingSessionFinishedV2 event) {
        BigDecimal tax = event.getTax();
        taxRepository.save(new Tax(event.getSessionId(), tax.multiply(event.getCost())));
    }

    private BigDecimal getTax(ChargingSessionFinished event) {
        if (event.getWhen().atZone(ZoneId.systemDefault()).getYear() > 2017) {
            return new BigDecimal(0.19);
        } else {
            return new BigDecimal(0.18);
        }
    }
}

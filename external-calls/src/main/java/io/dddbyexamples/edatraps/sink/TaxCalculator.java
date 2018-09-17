package io.dddbyexamples.edatraps.sink;

import io.dddbyexamples.edatraps.infrastructure.TaxRateRepository;
import io.dddbyexamples.edatraps.infrastructure.TaxRepository;
import io.dddbyexamples.edatraps.model.Tax;
import io.dddbyexamples.edatraps.model.TaxRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class TaxCalculator {

    private final TaxRepository taxRepository;
    private final TaxRateRepository taxRateRepository;

    public TaxCalculator(TaxRepository taxRepository, TaxRateRepository taxRateRepository) {
        this.taxRepository = taxRepository;
        this.taxRateRepository = taxRateRepository;
    }

    @StreamListener(target = Sink.INPUT, condition = "headers['type'] == 'session-finished'")
    public void handle(ChargingSessionFinished event) {
        BigDecimal tax = getTax();
        taxRepository.save(new Tax(event.getSessionId(), tax.multiply(event.getCost())));
    }

    @StreamListener(target = Sink.INPUT, condition = "headers['type'] == 'tax-rate-changed'")
    public void handle(TaxRateChanged event) {
        TaxRate rate = taxRateRepository
                .findByCommodity(event.getCommodity())
                .orElseGet(() -> new TaxRate(event.getCommodity()));
        rate.changeTo(event.getNewRate());
        taxRateRepository.save(rate);
    }

    private BigDecimal getTax() {
       log.info("an expensive external call to a service that handles user requests");
       return taxRateRepository
               .findByCommodity("energy")
               .map(TaxRate::getRate)
               .orElse(BigDecimal.ZERO);
       //return BigDecimal.TEN;
    }
}

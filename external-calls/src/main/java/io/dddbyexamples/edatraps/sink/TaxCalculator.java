package io.dddbyexamples.edatraps.sink;

import io.dddbyexamples.edatraps.infrastructure.TaxRateRepository;
import io.dddbyexamples.edatraps.infrastructure.TaxRepository;
import io.dddbyexamples.edatraps.model.Tax;
import io.dddbyexamples.edatraps.model.TaxRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
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
    @Transactional
    public void handle(ChargingSessionFinished event) {
        BigDecimal rate = getTaxRate("energy");
        taxRepository.save(new Tax(event.getSessionId(), rate.multiply(event.getCost())));
    }

    @StreamListener(target = Sink.INPUT, condition = "headers['type'] == 'tax-rate-changed'")
    @Transactional
    public void handle(TaxRateChanged event) {
        TaxRate current = taxRateRepository.findByCommodity(event.getCommodity())
                .orElseGet(() -> createNewRateFor(event.getCommodity()));
        current.changeTo(event.getNewRate());
    }

    private TaxRate createNewRateFor(String commodity) {
        return taxRateRepository.save(new TaxRate(commodity));
    }


    private BigDecimal getTaxRate(String commodity) {
        return taxRateRepository.findByCommodity(commodity).get().getRate();
    }
}

package io.dddbyexamples.edatraps.sink;

import io.dddbyexamples.edatraps.infrastructure.TaxRateRepository;
import io.dddbyexamples.edatraps.infrastructure.TaxRepository;
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

    }



    private BigDecimal getTaxRate() {
       log.info("an expensive external call to a service that handles user requests");
       return BigDecimal.TEN;
    }
}

package io.dddbyexamples.edatraps.sink;

import io.dddbyexamples.edatraps.infrastructure.TaxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaxCalculator {

    private final TaxRepository taxRepository;

    public TaxCalculator(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }


    @StreamListener(target = Sink.INPUT)
    public void handle(ChargingSessionFinished event) {
        log.info(event.getCustomerName());
    }


}

package io.dddbyexamples.edatraps;

import io.dddbyexamples.edatraps.infrastructure.TaxRepository;
import io.dddbyexamples.edatraps.model.Tax;
import io.dddbyexamples.edatraps.sink.ChargingSessionFinished;
import io.dddbyexamples.edatraps.sink.ChargingSessionFinishedV2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TaxCalculatorTest {

    @Autowired
    private Sink sink;

    @Autowired
    private TaxRepository taxRepository;

    @Test
    public void shouldCorrectlySumTaxes() {
        //given
        sessionWasFinished("S1", Instant.now(), new BigDecimal(100), new BigDecimal(0.19));
        sessionWasFinished("S2", ZonedDateTime.now().plusYears(1).toInstant(), new BigDecimal(1000), new BigDecimal(0.18));


        //then
        thereIsTax("S1", new BigDecimal(19));
        thereIsTax("S2", new BigDecimal(180));

    }

    private void thereIsTax(String id, BigDecimal expectedTax) {
        Tax tax = taxRepository.findBySessionId(id);
        assertThat(tax.getValue()).isEqualByComparingTo(expectedTax);
    }

    private void sessionWasFinished(String id, Instant when, BigDecimal cost, BigDecimal taxRate) {
        ChargingSessionFinishedV2 event = new ChargingSessionFinishedV2(id, when, cost, taxRate);
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", event.getType());
        sink.input().send(new GenericMessage<>(event, headers));
    }


}

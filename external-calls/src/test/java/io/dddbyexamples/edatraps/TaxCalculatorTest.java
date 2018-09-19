package io.dddbyexamples.edatraps;

import io.dddbyexamples.edatraps.infrastructure.TaxRepository;
import io.dddbyexamples.edatraps.model.Tax;
import io.dddbyexamples.edatraps.sink.ChargingSessionFinished;
import io.dddbyexamples.edatraps.sink.TaxRateChanged;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static java.time.Instant.now;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaxCalculatorTest {

    @Autowired
    private Sink sink;

    @Autowired
    private TaxRepository taxRepository;

    @Test
    public void shouldCorrectlyCalculateTaxes() {


    }

    private void sessionWasFinished(String session, Instant when, BigDecimal cost) {
        ChargingSessionFinished event = new ChargingSessionFinished(session, when, cost);
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", event.getType());
        sink.input().send(new GenericMessage<>(event, headers));
    }



    private void thereIsTaxaxForSession(String session, BigDecimal taxBD) {
        Tax tax = taxRepository.findBySessionId(session);
        assertThat(tax.getValue()).isEqualByComparingTo(taxBD);
    }


}

package io.dddbyexamples.edatraps;

import io.dddbyexamples.edatraps.infrastructure.TaxRepository;
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
        energyTaxRateChanged(new BigDecimal(0.10), Instant.now().minusSeconds(3600));
        chargingSessionFinished("S1", new BigDecimal(100.0), Instant.now());
        energyTaxRateChanged(new BigDecimal(0.01), Instant.now());
        chargingSessionFinished("S3", new BigDecimal(100.0), Instant.now());

        //expect
        thereIsTaxForSession("S1", new BigDecimal(10));
        thereIsTaxForSession("S3", new BigDecimal(1));
    }


    private void thereIsTaxForSession(String session, BigDecimal value) {
        assertThat(taxRepository.findBySessionId(session).getValue()).isEqualByComparingTo(value);
    }

    private void chargingSessionFinished(String id, BigDecimal cost, Instant when) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "session-finished");
        //sink.input().send(new GenericMessage<>(
         //       new ChargingSessionFinished(id, when, cost), headers));
    }

    private void energyTaxRateChanged(BigDecimal rate, Instant when) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "tax-rate-changed");
        sink.input().send(new GenericMessage<>(
                new TaxRateChanged(when, rate, "energy"), headers));
    }


}

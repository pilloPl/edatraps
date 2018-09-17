package io.dddbyexamples.edatraps;

import io.dddbyexamples.edatraps.infrastructure.TaxRepository;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
        chargingSessionFinished("S1", new BigDecimal(100.0), Instant.now());
        chargingSessionFinished("S3", new BigDecimal(1000.0), LocalDate.of(2017, 10, 10).atStartOfDay(ZoneId.systemDefault()).toInstant());

        //expect
        thereIsTaxForSession("S1", new BigDecimal(19));
        thereIsTaxForSession("S3", new BigDecimal(180));
    }

    @Test
    public void shouldCorrectlySumTaxesV2() {
        //given
        chargingSessionFinishedV2("S1", new BigDecimal(100.0), new BigDecimal(0.19), Instant.now());
        chargingSessionFinishedV2("S3", new BigDecimal(1000.0), new BigDecimal(0.18), LocalDate.of(2017, 10, 10).atStartOfDay(ZoneId.systemDefault()).toInstant());

        //expect
        thereIsTaxForSession("S1", new BigDecimal(19));
        thereIsTaxForSession("S3", new BigDecimal(180));
    }

    private void thereIsTaxForSession(String session, BigDecimal value) {
        assertThat(taxRepository.findBySessionId(session).getValue()).isEqualByComparingTo(value);
    }

    private void chargingSessionFinished(String id, BigDecimal cost, Instant when) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "session-finished");
        sink.input().send(new GenericMessage<>(
                new ChargingSessionFinished(id, when, cost), headers));
    }

    private void chargingSessionFinishedV2(String id, BigDecimal cost, BigDecimal tax, Instant when) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "session-finished-v2");
        sink.input().send(new GenericMessage<>(
                new ChargingSessionFinishedV2(id, when, cost, tax), headers));
    }


}

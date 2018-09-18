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

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;

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



}

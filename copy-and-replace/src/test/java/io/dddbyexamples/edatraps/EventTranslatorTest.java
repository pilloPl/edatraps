package io.dddbyexamples.edatraps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dddbyexamples.edatraps.model.ChargingSessionFinished;
import io.dddbyexamples.edatraps.model.ChargingSessionFinishedV2;
import io.dddbyexamples.edatraps.model.DomainEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.DataInput;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class EventTranslatorTest {


    @Autowired Processor processor;

    @Autowired MessageCollector collector;

    BlockingQueue<Message<?>> eventsV2;

    @Autowired ObjectMapper objectMapper;


    @Before
    public void setup() {
        eventsV2 = collector.forChannel(processor.output());
    }

    @Test
    public void shouldCopyAndTranslateEvents() throws IOException {

        //when
        Instant now = Instant.now();
        oldEvent("S1", now, BigDecimal.TEN);

        //then
        newEvent("S1", now, BigDecimal.TEN, "we did not care");

    }

    private void newEvent(String id, Instant when, BigDecimal cost, String customer) throws IOException {
        ChargingSessionFinishedV2 expected = new ChargingSessionFinishedV2(id, when, cost, customer);
        ChargingSessionFinishedV2 actual = objectMapper.readValue(eventsV2.poll().getPayload().toString(), ChargingSessionFinishedV2.class);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    private void oldEvent(String id, Instant when, BigDecimal cost) {
        processor.input().send(new GenericMessage<>(new ChargingSessionFinished(id, when, cost)));
    }


}
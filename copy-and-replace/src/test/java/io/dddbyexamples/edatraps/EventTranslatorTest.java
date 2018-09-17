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

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventTranslatorTest {


    @Autowired Processor processor;

    @Autowired MessageCollector collector;

    BlockingQueue<Message<?>> eventsV2;

    @Autowired ObjectMapper objectMapper;

    @Autowired


    @Before
    public void setup() {
        eventsV2 = collector.forChannel(processor.output());
    }

    @Test
    public void shouldCopyAndTranslateEvents() throws IOException {
        //given
        Instant date = Instant.now();
        oldEvent(new ChargingSessionFinished("session", date, BigDecimal.TEN));

        //then
        thereIsNewEvent(new ChargingSessionFinishedV2("session", date, BigDecimal.TEN, "I dont care"));
    }

    private void thereIsNewEvent(ChargingSessionFinishedV2 chargingSessionFinishedV2) throws IOException {
        Message msg = (eventsV2.poll());
        assertThat(msg).isNotNull();
        assertThat(msg.getHeaders()).containsEntry("type", "session-finished-v2");

        ChargingSessionFinishedV2 event = objectMapper.readValue(msg.getPayload().toString(), ChargingSessionFinishedV2.class);
        assertThat(event.equals(chargingSessionFinishedV2)).isTrue();
    }

    private void oldEvent(DomainEvent event) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", event.getType());
        processor.input().send(new GenericMessage<>(event, headers));
    }

}
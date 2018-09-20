package io.dddbyexamples.edatraps;

import io.dddbyexamples.edatraps.model.ChargingSessionFinished;
import io.dddbyexamples.edatraps.model.ChargingSessionFinishedV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableBinding(Source.class)
@EnableScheduling
public class EventsProducer {

	private final Source source;

	public EventsProducer(Source source) {
		this.source = source;
	}

	public static void main(String[] args) {
		SpringApplication.run(EventsProducer.class, args);
	}

	@Scheduled(fixedRate = 500L)
	public void sessionClosed() {

		ChargingSessionFinishedV2 eventV2 = new ChargingSessionFinishedV2("session", Instant.now(), BigDecimal.TEN, "Pieter");
		Map<String, Object> headersV2 = new HashMap<>();
		headersV2.put("type", eventV2.getType());

		source.output().send(new GenericMessage<>(eventV2, headersV2));

	}
}

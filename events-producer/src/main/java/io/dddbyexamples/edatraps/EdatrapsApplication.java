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
public class EdatrapsApplication {

	private final Source source;

	public EdatrapsApplication(Source source) {
		this.source = source;
	}

	public static void main(String[] args) {
		SpringApplication.run(EdatrapsApplication.class, args);
	}

	@Scheduled(fixedRate = 500L)
	public void sessionClosed() {
		ChargingSessionFinished event = new ChargingSessionFinished("session", Instant.now(), BigDecimal.TEN);
		Map<String, Object> headers = new HashMap<>();
		headers.put("type", event.getType());

		source.output().send(new GenericMessage<>(event, headers));

	}
}

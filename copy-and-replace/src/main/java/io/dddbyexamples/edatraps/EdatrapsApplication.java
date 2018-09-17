package io.dddbyexamples.edatraps;

import io.dddbyexamples.edatraps.model.ChargingSessionFinished;
import io.dddbyexamples.edatraps.model.ChargingSessionFinishedV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableBinding(Processor.class)
@EnableScheduling
public class EdatrapsApplication {

	private final Processor processor;

	public EdatrapsApplication(Processor processor) {
		this.processor = processor;
	}

	public static void main(String[] args) {
		SpringApplication.run(EdatrapsApplication.class, args);
	}


	@StreamListener(value = Processor.INPUT)
	public void translate(Message<ChargingSessionFinished> msg) {
		System.out.println("bla");
		ChargingSessionFinished event = msg.getPayload();
		ChargingSessionFinishedV2 eventV2 = new ChargingSessionFinishedV2(event.getSessionId(), event.getWhen(), event.getCost(), "I dont care");

		Map<String, Object> headers = new HashMap<>(msg.getHeaders());
		headers.put("type", eventV2.getType());
		processor.output().send(new GenericMessage<>(eventV2, headers));
	}

//	@Scheduled(fixedRate = 2000L)
//	public void sessionClosed() {
//		ChargingSessionFinished event = new ChargingSessionFinished("session", Instant.now(), BigDecimal.TEN);
//		ChargingSessionFinishedV2 eventv2 = new ChargingSessionFinishedV2("session", Instant.now(), BigDecimal.TEN, "cus");
//
//		Map<String, Object> headers = new HashMap<>();
//		headers.put("type", event.getType());
//		source.output().send(new GenericMessage<>(event, headers));
//		headers.put("type", eventv2.getType());
//		source.output().send(new GenericMessage<>(eventv2, headers));
//	}


}

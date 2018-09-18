package io.dddbyexamples.edatraps;

import io.dddbyexamples.edatraps.model.ChargingSessionFinished;
import io.dddbyexamples.edatraps.model.ChargingSessionFinishedV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
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


	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public Message<ChargingSessionFinishedV2> translate(Message<ChargingSessionFinished> msg) {
		return null;
	}



}

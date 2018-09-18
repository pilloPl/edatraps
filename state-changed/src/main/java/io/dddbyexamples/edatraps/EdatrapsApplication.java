package io.dddbyexamples.edatraps;

import io.dddbyexamples.edatraps.persistance.ChargingSessionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;

@SpringBootApplication
@EnableBinding(Sink.class)
@EnableScheduling
public class EdatrapsApplication {

	private final ChargingSessionRepository chargingSessionRepository;

	public EdatrapsApplication(ChargingSessionRepository chargingSessionRepository) {
		this.chargingSessionRepository = chargingSessionRepository;
	}

	@Scheduled(fixedRate = 2000)
	@Transactional
	public void charginsSessions() {

	}


	public static void main(String[] args) {
		SpringApplication.run(EdatrapsApplication.class, args);
	}

}

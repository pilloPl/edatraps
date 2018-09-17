package io.dddbyexamples.edatraps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBinding(Source.class)
@EnableScheduling
public class EdatrapsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdatrapsApplication.class, args);
	}
}

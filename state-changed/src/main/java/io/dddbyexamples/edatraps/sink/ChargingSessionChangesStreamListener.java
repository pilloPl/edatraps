package io.dddbyexamples.edatraps.sink;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;


@Service
@Slf4j
class ChargingSessionChangesStreamListener {

	@StreamListener(Sink.INPUT)
	public void handle(Envelope message) {
	    log.info(message.toString());
	}


}

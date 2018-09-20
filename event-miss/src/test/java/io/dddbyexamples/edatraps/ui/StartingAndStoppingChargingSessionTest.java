package io.dddbyexamples.edatraps.ui;

import io.dddbyexamples.edatraps.infrastructure.ChargingSessionRepository;
import io.dddbyexamples.edatraps.model.ChargingSession;
import javafx.scene.paint.Stop;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class StartingAndStoppingChargingSessionTest {

    @Autowired
    private ChargingSessionRepository sessionRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldMarkChargingSessionAsFinished() {
        //given
        sessionWasStarted();

        //when
        sessionWasFinished();

        //then
        thereIsChargingSessionInDbWithStateFinished();

    }

    @Test
    public void shouldMarkChargingSessionAsFinishedWhenStartInfoIsNotThere() {

        //when
        sessionWasFinished();

        //then
        thereIsChargingSessionInDbWithStateFinished();

    }

    private void sessionWasFinished() {
        testRestTemplate.postForEntity("/ends", new StopTransaction("S1"), Void.class);
    }

    private void sessionWasStarted() {
        testRestTemplate.postForEntity("/starts", new StartTransaction("S1"), Void.class);
    }

    private void thereIsChargingSessionInDbWithStateFinished() {
        Optional<ChargingSession> session = sessionRepository.findById("S1");
        assertThat(session).isPresent();
        assertThat(session.get().isFinished()).isTrue();
    }


}

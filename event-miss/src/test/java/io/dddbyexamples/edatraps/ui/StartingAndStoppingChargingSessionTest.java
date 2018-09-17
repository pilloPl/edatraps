package io.dddbyexamples.edatraps.ui;

import io.dddbyexamples.edatraps.infrastructure.ChargingSessionRepository;
import io.dddbyexamples.edatraps.model.ChargingSession;
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
        chargerSendsInfoThatSessionWasStarted();

        //when
        chargerSendsInfoThatSessionWasFinished();

        //then
        sessionMarkedAsFinished();
    }

    private void sessionMarkedAsFinished() {
        Optional<ChargingSession> session = sessionRepository.findById("123");
        assertThat(session).isPresent();
        assertThat(session.get().isFinished()).isTrue();
    }

    private void chargerSendsInfoThatSessionWasFinished() {
        testRestTemplate.postForEntity("/ends", new StopTransaction("123"), Void.class);
    }

    private void chargerSendsInfoThatSessionWasStarted() {
        testRestTemplate.postForEntity("/starts", new StartTransaction("123"), Void.class);
    }

}

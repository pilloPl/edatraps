package io.dddbyexamples.edatraps.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@NoArgsConstructor
public class ChargingSession {

    enum State {STARTED, FINISHED}

    @Id
    private String id;
    private Instant startedAt;
    private Instant finishedAt;
    private State state;


    public ChargingSession(String id, Instant startedAt) {
        this.id = id;
        this.startedAt = startedAt;
        this.state = State.STARTED;
    }

    public void finishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
        this.state = State.FINISHED;
    }

    public boolean isFinished() {
        return State.FINISHED.equals(state);
    }
}

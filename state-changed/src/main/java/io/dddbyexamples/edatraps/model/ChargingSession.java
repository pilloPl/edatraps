package io.dddbyexamples.edatraps.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class ChargingSession {

    enum State {STARTED, FINISHED}

    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private State state;

    public ChargingSession(String id) {
        this.id = id;
        this.state = State.STARTED;
    }

    public void finish() {
        this.state = State.FINISHED;
    }

    public boolean isFinished() {
        return State.FINISHED.equals(state);
    }

    public void restart() {
        state = State.STARTED;
    }
}

package io.dddbyexamples.edatraps.sink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
class ChargingSessionStateChanged {

    private String op;
    private String ts_ms;
    private ChargingSession before;
    private ChargingSession after;

}

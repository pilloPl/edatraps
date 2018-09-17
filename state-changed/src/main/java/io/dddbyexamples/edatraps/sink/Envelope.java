package io.dddbyexamples.edatraps.sink;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
class Envelope {

    private ChargingSessionStateChanged payload;

    boolean isUpdate() {
        return payload.getOp().equals("u");
    }
}

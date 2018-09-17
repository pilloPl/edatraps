package io.dddbyexamples.edatraps.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
public class Tax {

    @Id @GeneratedValue
    private Long id;
    private String sessionId;
    @Getter private BigDecimal value;

    public Tax(String sessionId, BigDecimal value) {
        this.sessionId = sessionId;
        this.value = value;
    }

}

package io.dddbyexamples.edatraps.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
public class TaxRate {

    @Id @GeneratedValue
    private Long id;
    @Getter
    private BigDecimal rate;
    private String commodity;

    public TaxRate(String commodity) {
        this.commodity = commodity;
    }

    public void changeTo(BigDecimal rate) {
        this.rate = rate;
    }

}

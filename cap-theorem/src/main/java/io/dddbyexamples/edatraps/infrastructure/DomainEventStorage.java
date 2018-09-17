package io.dddbyexamples.edatraps.infrastructure;

import lombok.Getter;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.List;


interface DomainEventStorage extends CrudRepository<StoredDomainEvent, Long> {
    List<StoredDomainEvent> findAllBySentOrderByTimestampDesc(boolean sent);
}


@Entity
class StoredDomainEvent {

    @Id
    @GeneratedValue
    Long id;
    @Getter
    private String content;
    private boolean sent;
    private Instant timestamp = Instant.now();
    @Getter
    private String type;

    StoredDomainEvent(String content, String type) {
        this.content = content;
        this.type = type;
    }

    private StoredDomainEvent() {
    }

    void sent() {
        sent = true;
    }

}
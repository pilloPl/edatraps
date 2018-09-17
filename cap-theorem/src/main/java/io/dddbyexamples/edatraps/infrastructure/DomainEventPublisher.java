package io.dddbyexamples.edatraps.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dddbyexamples.edatraps.model.DomainEvent;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Component
public class DomainEventPublisher {

    private final Source source;
    private final DomainEventStorage domainEventStorage;
    private final ObjectMapper objectMapper;

    public DomainEventPublisher(Source source, DomainEventStorage domainEventStorage, ObjectMapper objectMapper) {
        this.source = source;
        this.domainEventStorage = domainEventStorage;
        this.objectMapper = objectMapper;
    }

    public void publish(DomainEvent event) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", event.getType());
        source.output().send(new GenericMessage<>(event, headers));
    }

    public void publishAfterCommit(DomainEvent event) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", event.getType());
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit(){
                source.output().send(new GenericMessage<>(event, headers));
            }
        });
    }

    public void saveAndPublishAfterCommit(DomainEvent domainEvent) {
        try {
            domainEventStorage.save(new StoredDomainEvent(objectMapper.writeValueAsString(domainEvent), domainEvent.getType()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 2000)
    @Transactional
    public void publishExternally() {
        domainEventStorage
                .findAllBySentOrderByTimestampDesc(false)
                .forEach(event -> {
                            Map<String, Object> headers = new HashMap<>();
                            headers.put("type", event.getType());
                            source.output().send(new GenericMessage<>(event.getContent(), headers));
                            event.sent();
                        }

                );
    }
}

package io.dddbyexamples.edatraps.sink;

//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY,
//        property = "type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(name = "session-finished", value = ChargingSessionFinished.class)
//})
public interface DomainEvent {

    String getType();
}

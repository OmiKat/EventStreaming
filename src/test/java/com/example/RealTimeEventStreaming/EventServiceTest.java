package com.example.RealTimeEventStreaming;


import com.example.RealTimeEventStreaming.PandC.EventProducer;
import com.example.RealTimeEventStreaming.domain.Event;
import com.example.RealTimeEventStreaming.domain.EventType;
import com.example.RealTimeEventStreaming.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
//@RequiredArgsConstructor
public class EventServiceTest {

    @Mock
    private EventProducer eventProducer;

    @InjectMocks
    private KafkaService kafkaService;

    @Test
    void publishEvent_shouldSetTimestamp_whenTimestampIsNull(){
        Event event = Event.builder()
                .playerId("player_123")
                .sessionId("session_001")
                .eventType(EventType.PLAYER_KILL)
                .metadata(Map.of("weapon", "sniper"))
                .build();

        kafkaService.publishEvent(event);

        assertNotNull(event.getTimeStamp(),
                "Service should set timestamp when it is null");
    }

    @Test
    void publishEvent_shouldSetId_whenIdIsNull(){
        Event event = Event.builder()
                .playerId("player_69")
                .sessionId("session_420")
                .eventType(EventType.LEVEL_UP)
                .metadata(Map.of("newLevel" , 5))
                .build();

        kafkaService.publishEvent(event);

        assertNotNull(event.getId() ,
                "Service should set timestamp when it is null");
    }

    @Test
    void publishEvent_shouldCallProducer_whenEventIsValid(){
        Event event = Event.builder()
                .playerId("player_69")
                .sessionId("session_420")
//                .eventType(EventType.LEVEL_UP)
                .metadata(Map.of("newLevel" , 5))
                .build();


        kafkaService.publishEvent(event);

        verify(eventProducer , times(1)).SendEvent(event);

    }

    @Test
    void publishEventShouldNotOverrideTheTimeStampWhenAlreadySet(){
        Event event = Event.builder()
                .playerId("player_69")
                .sessionId("session_420")
                .timeStamp(Instant.now())
                .eventType(EventType.LEVEL_UP)
                .metadata(Map.of("newLevel" , 5))
                .build();

        Instant original = event.getTimeStamp();
        kafkaService.publishEvent(event);

        Assertions.assertEquals(original , event.getTimeStamp());

    }




}

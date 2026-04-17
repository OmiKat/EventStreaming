package com.example.RealTimeEventStreaming.producer;

import com.example.RealTimeEventStreaming.constants.KafkaConstants;
import com.example.RealTimeEventStreaming.domain.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, Event> kafkaTemplate;

    public void SendEvent(Event event){
        CompletableFuture<SendResult<String , Event>> future = kafkaTemplate.send(
                KafkaConstants.GAME_EVENT_TOPIC,
                event.getPlayerId(),
                event
        );
        future.whenComplete((result, ex) -> {
            if(ex != null){
                log.error("Failed to send Event :{} for player : {}"
                        ,event.getEventType(), event.getPlayerId()
                );
            }
            else{
                log.info("Event : {} Sent successfully for player : {} | partition : {} | offset : {}",
                        event.getEventType(),
                        event.getPlayerId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset()
                        );
            }
        });
    }

}

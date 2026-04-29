package com.example.RealTimeEventStreaming.PandC;

import com.example.RealTimeEventStreaming.Repo.EventRepo;
import com.example.RealTimeEventStreaming.domain.Event;
import com.example.RealTimeEventStreaming.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {
    private final EventRepo repo;
    private final RedisService service;

    @KafkaListener(
            topics = "game-events",
            groupId = "gaming-event-consumers"
    )
    public void consume(@Payload Event event,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        @Header(KafkaHeaders.OFFSET) long offset){
        log.info("Received event [{}] from player [{}] | partition [{}] | offset [{}]",
                event.getEventType(),
                event.getPlayerId(),
                partition,
                offset
        );
        saveToMongoDB(event);
        updateRedis(event);
    }
    private void saveToMongoDB(Event event){
        try {
            repo.save(event);
            log.info("Saved to MongoDB: event [{}] player [{}]", event.getEventType(), event.getPlayerId());
        } catch (Exception e) {
            log.error(" Failed to save to MongoDB: event [{}] player [{}] — {}",
                    event.getEventType(), event.getPlayerId(), e.getMessage());
        }
    }
    private void updateRedis(Event event){
        service.recordEvent(
                event.getEventType().name(),
                event.getPlayerId()
        );

        // additional Redis logic per event type
        switch (event.getEventType()) {
            case PLAYER_KILL -> {

                log.info("Kill recorded for player: {}", event.getPlayerId());
            }
            case LEVEL_UP -> {
                Integer newLevel = (Integer) event.getMetadata().get("newLevel");
                if (newLevel != null) {
                    service.updatePlayerLevel(event.getPlayerId(), newLevel);
                }
            }
            case ACHIEVEMENT_UNLOCKED -> {
                log.info("Achievement unlocked for player: {}", event.getPlayerId());
            }
            default -> log.info("Event type {} recorded in stats",
                    event.getEventType());
        }
    }

}

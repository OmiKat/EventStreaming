package com.example.RealTimeEventStreaming.controller;

import com.example.RealTimeEventStreaming.Repo.EventRepo;
import com.example.RealTimeEventStreaming.domain.Event;
import com.example.RealTimeEventStreaming.service.HistoryService;
import com.example.RealTimeEventStreaming.service.KafkaService;
import com.example.RealTimeEventStreaming.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final KafkaService kafkaService;
    private final EventRepo repo;
    private final HistoryService historyService;
    private final StatsService statsService;

    @PostMapping
    @Operation(summary = "Publish a gaming event",
            description = "Accepts a player event and publishes it to Kafka for async processing")
    public ResponseEntity<Map<String , String>> sendToKafka(@RequestBody Event event){
        kafkaService.publishEvent(event);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "status" , "accepted",
                        "eventId", event.getId(),
                        "message" , "sent to kafka successfully"
                ));
    }
    @GetMapping("full/history")
    public ResponseEntity<List<Event>> getHistory(){
        List<Event> fullhistory = historyService.getHistory();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(fullhistory);
    }


    @GetMapping("/history/player")
    public ResponseEntity<?> getPlayerHistory(
            @RequestParam String playerId,
            @RequestParam(required = false ) String eventType
    ){
        try {
            List<Event> playerHistory =
                    (eventType != null)
                    ? historyService.getPlayerHistoryWithEventType(playerId, eventType)
                    : historyService.getPlayerHistory(playerId);

            return ResponseEntity.ok(
                    Map.of(
                       "playerId" , playerId,
                       "eventCount" , playerHistory.size(),
                       "events" , playerHistory
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Invalid event type : " + eventType));

        }

    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam String type) {
        try {
            Map<String, Long> stats = statsService.getStatsByEventType(type);
            return ResponseEntity.ok(Map.of(
                    "eventType", type.toUpperCase(),
                    "stats", stats
            ));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Invalid event type: " + type));
        }
    }

}

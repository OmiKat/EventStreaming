package com.example.RealTimeEventStreaming.controller;

import com.example.RealTimeEventStreaming.domain.Event;
import com.example.RealTimeEventStreaming.service.KafkaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final KafkaService service;

    @PostMapping("/users")
    @Operation(summary = "Publish a gaming event",
            description = "Accepts a player event and publishes it to Kafka for async processing")
    public ResponseEntity<Map<String , String>> sendToKafka(@RequestBody Event event){
        service.publishEvent(event);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "status" , "accepted",
                        "eventId", event.getId(),
                        "message" , "sent to kafka successfully"
                ));
    }

}

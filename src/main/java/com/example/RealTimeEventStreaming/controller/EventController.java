package com.example.RealTimeEventStreaming.controller;

import com.example.RealTimeEventStreaming.domain.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final KafkaTemplate<String,Event> kafkaTemplate;

    @PostMapping("/users")
    public ResponseEntity<String> sendToKafka(@RequestBody Event event){
        kafkaTemplate.send("gaming-event-consumers" , event);
        return new ResponseEntity<>(event.getPlayerId() , HttpStatus.OK);
    }

}

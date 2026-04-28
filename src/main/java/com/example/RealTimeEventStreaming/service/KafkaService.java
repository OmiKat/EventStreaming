package com.example.RealTimeEventStreaming.service;

import com.example.RealTimeEventStreaming.domain.Event;
import com.example.RealTimeEventStreaming.PandC.EventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final EventProducer eventProducer;

    public void publishEvent(Event event){
        if(event.getTimeStamp()==null){
            event.setTimeStamp(Instant.now());
        }
        if(event.getId() == null){
            event.setId(UUID.randomUUID().toString());
        }
        eventProducer.SendEvent(event);
    }

}

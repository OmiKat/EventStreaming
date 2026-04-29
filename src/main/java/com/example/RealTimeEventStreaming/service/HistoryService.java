package com.example.RealTimeEventStreaming.service;

import com.example.RealTimeEventStreaming.Repo.EventRepo;
import com.example.RealTimeEventStreaming.domain.Event;
import com.example.RealTimeEventStreaming.domain.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistoryService {

    private final EventRepo eventRepo;

    public List<Event> getHistory(){
        return eventRepo.findAll();
    }

    public  List<Event> getPlayerHistory(String playerId){
        log.info("Fetching full history for player: {}", playerId);
        return eventRepo.findByPlayerId(playerId);
    }

    public List<Event> getPlayerHistoryWithEventType(String playerId , String eventType){
        EventType type = EventType.valueOf(eventType.toUpperCase());
        log.info("Fetching {} history for player: {} ", eventType , type );
        return  eventRepo.findByPlayerIdAndEventType(playerId , type);
    }
}

package com.example.RealTimeEventStreaming.Repo;

import com.example.RealTimeEventStreaming.domain.Event;
import com.example.RealTimeEventStreaming.domain.EventType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepo extends MongoRepository<Event,String> {

    List<Event> findByPlayerId(String playerId);

    List<Event> findByPlayerIdAndEventType(String playerId , EventType eventType);
}

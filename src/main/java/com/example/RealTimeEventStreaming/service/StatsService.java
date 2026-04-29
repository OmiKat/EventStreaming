package com.example.RealTimeEventStreaming.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatsService {
    private final RedisService redisService;

    public Map<String, Long> getStatsByEventType(String eventType) {
        String normalised = eventType.toUpperCase();
        log.info("Fetching stats for event type: {}", normalised);
        return redisService.getStats(normalised);
    }
}

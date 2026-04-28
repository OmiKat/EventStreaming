package com.example.RealTimeEventStreaming.service;

import com.example.RealTimeEventStreaming.domain.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String,String> redisTemplate;

    private static final String STATS_KEY_PREFIX = "stats:";

    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;


    public void recordEvent(String playerId , String eventType){
        String key = STATS_KEY_PREFIX + eventType;

        //zset sorts according to score and score is time
        double score = Instant.now().getEpochSecond();
        String member = score  + ":" + playerId;

        redisTemplate.opsForZSet().add(key , member , score);

        double cutoff = Instant.now().getEpochSecond() - ONE_DAY;
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, cutoff);

        log.info("Redis updated for event type: {} | player: {}", eventType, playerId);

    }
    public Map<String, Long> getStats(String eventType) {
        String key = STATS_KEY_PREFIX + eventType;
        double now = Instant.now().getEpochSecond();

        // count members whose score falls within the time window
        // ZSet score = timestamp, so score range = time range
        Long lastHour = redisTemplate.opsForZSet().count(key,now - ONE_HOUR,now);

        Long lastDay = redisTemplate.opsForZSet().count(key,now - ONE_DAY, now);

        Map<String, Long> stats = new HashMap<>();
        stats.put("lastHour", lastHour != null ? lastHour : 0L);
        stats.put("last24Hours", lastDay != null ? lastDay : 0L);

        return stats;
    }

    public void updatePlayerLevel(String playerId, int newLevel) {
        // Redis Hash — "player:123" → { "level": "15" }
        redisTemplate.opsForHash().put(
                "player:" + playerId,
                "level",
                String.valueOf(newLevel)
        );
        log.info("Updated level for player: {} to level: {}", playerId, newLevel);
    }


}

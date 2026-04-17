package com.example.RealTimeEventStreaming.config;

import com.example.RealTimeEventStreaming.constants.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic gameEventsTopic(){
        return TopicBuilder
                .name(KafkaConstants.GAME_EVENT_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }


}

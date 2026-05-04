package com.example.RealTimeEventStreaming.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;


import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Real-Time Gaming Event Streaming API")
                        .description("""
                                A real-time event pipeline for multiplayer gaming platforms.
                                
                                Events (kills, deaths, level-ups, achievements) are published 
                                via REST, streamed through Apache Kafka, persisted to MongoDB, 
                                and aggregated in Redis for real-time stats.
                                
                                Architecture:
                                POST /events → Kafka → Consumer → MongoDB (raw storage) → Redis (aggregated stats)
                                """)
                        .version("1.0.0")

                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server")
                ));
    }
}

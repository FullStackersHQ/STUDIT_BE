package com.studit.backend.global.config;

import com.studit.backend.domain.todoList.timer.TodoTimerHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // WebSocketHandlerRegistry에 핸들러를 추가
        // "/ws/timer" 경로로 WebSocket 연결을 처리하도록 설정
        registry.addHandler(new TodoTimerHandler(), "/ws/timer")
                .setAllowedOrigins("*");  // 허용할 도메인만 설정
    }
}

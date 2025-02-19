package com.studit.backend.domain.todoList.timer;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;

public class TodoTimerHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        // 세션에서 studyId 가져오기
        String studyIdString = getStudyIdFromSession(session);

        if (studyIdString == null) {
            sendTimerUpdate(session, "Test timer message: 0:00");
            return;
            // studyId가 null일 경우, 세션을 종료하고 오류 상태 반환
            //session.close(CloseStatus.BAD_DATA);
            //return;
        }

        try {
            // studyId를 Long으로 변환
            Long studyId = Long.parseLong(studyIdString);
            // studyId를 기반으로 추가적인 로직을 처리할 수 있음
            System.out.println("웹소켓 연결 성공, studyId: " + studyId);
        } catch (NumberFormatException e) {
            // studyId가 잘못된 포맷일 경우, 세션을 종료하고 오류 상태 반환
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트로부터 받은 메시지 처리
        String payload = message.getPayload();
        // payload를 JSON 파싱 후 처리
        // 예: {"action": "start", "studyId": 123, "todoId": 456}

        System.out.println("메시지 수신: " + payload);

        // JSON을 파싱하여 각 액션 처리 (시작, 중지, 업데이트 등)
        // 예: JSON 파싱 후 적절한 액션을 취하는 로직을 작성
        // 이 부분에 실제 비즈니스 로직을 추가
        // 예를 들어 타이머를 시작, 중지하거나, 타이머 업데이트를 할 수 있음
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 예외 처리 로직
        System.err.println("웹소켓 오류: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println("웹소켓 연결 종료: " + status);
        // 연결 종료 시 리소스 정리 등 추가적인 처리
    }

    // 세션에서 studyId를 가져오는 메서드
    private String getStudyIdFromSession(WebSocketSession session) {
        // 세션에서 studyId 값을 가져옴
        return (String) session.getAttributes().get("studyId");  // 예시: 세션 속성에서 studyId 가져오기
    }

    // 타이머 업데이트 메시지를 클라이언트에 전송하는 메서드
    private void sendTimerUpdate(WebSocketSession session, String timer) throws Exception {
        // 예시로 "timer"라는 값을 클라이언트에 전송
        session.sendMessage(new TextMessage("{\"timer\": \"" + timer + "\"}"));
    }
}

package com.DOCKin.global.config;

import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final JwtUtil jwtUtil;

    // 접속 중인 세션 관리 (sessionId -> userId)
    private static final Map<String, String> onlineUsers = new ConcurrentHashMap<>();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 1. 웹소켓 연결 시도 시 (CONNECT)
        if (StompCommand.CONNECT == accessor.getCommand()) {
            // 헤더에서 'token' 값을 읽어옴
            String token = accessor.getFirstNativeHeader("token");

            log.info("WebSocket 연결 시도 - 수신 토큰: [{}]", token);

            try {
                // 토큰 부재 시 차단
                if (token == null || token.isEmpty()) {
                    throw new MessageDeliveryException("인증 토큰이 누락되었습니다.");
                }

                // Bearer 접두사가 포함되어 있다면 제거 (JwtUtil 설정에 따라 다를 수 있음)
                if (token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }

                // JWT 토큰 유효성 검증
                if (!jwtUtil.isValidToken(token)) {
                    throw new BusinessException(ErrorCode.INVALID_TOKEN);
                }

                // 토큰에서 유저 아이디(UserId) 추출
                String userId = jwtUtil.getUserId(token);

                // 세션 속성에 userId 저장 (나중에 SUBSCRIBE 등에서 꺼내 쓰기 위함)
                if (accessor.getSessionAttributes() != null) {
                    accessor.getSessionAttributes().put("userId", userId);
                }

                // 온라인 유저 맵에 등록
                onlineUsers.put(accessor.getSessionId(), userId);
                log.info("WebSocket 인증 성공: userId={}", userId);

            } catch (Exception e) {
                log.error("WebSocket 인증 실패: {}", e.getMessage());
                // 여기서 에러를 던지면 연결이 거부됩니다.
                throw new MessageDeliveryException("인증 실패: " + e.getMessage());
            }
        }

        // 2. 특정 채널 구독 시도 시 (SUBSCRIBE)
        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            // CONNECT 단계에서 저장했던 userId를 꺼냄
            String userId = (String) accessor.getSessionAttributes().get("userId");

            if (userId == null) {
                throw new MessageDeliveryException("로그인이 필요한 서비스입니다.");
            }
            log.info("구독 요청 - 유저: {}, 경로: {}", userId, accessor.getDestination());
        }

        // 3. 연결 해제 시 (DISCONNECT)
        else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = accessor.getSessionId();
            String removedUser = onlineUsers.remove(sessionId);
            log.info("WebSocket 연결 종료 - userId: {}", removedUser);
        }

        return message;
    }
}
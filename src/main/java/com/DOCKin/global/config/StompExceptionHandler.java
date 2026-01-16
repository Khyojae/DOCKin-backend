package com.DOCKin.global.config;

import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class StompExceptionHandler extends StompSubProtocolErrorHandler {
    public StompExceptionHandler(){
        super();
    }

    //에러가 발생하면 자동으로 호출됨
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex){
        Throwable exception = ex;
        if(ex instanceof MessageDeliveryException){
            exception = ex.getCause();
        }

        if(exception instanceof BusinessException){
            return prepareErrorMessage(((BusinessException) exception).getErrorCode());
        }
        return super.handleClientMessageProcessingError(clientMessage,ex);
    }

    //에러 박스 만들기
    private Message<byte[]> prepareErrorMessage(ErrorCode errorCode){
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(errorCode.getMessage());
        accessor.setLeaveMutable(true);

        accessor.setNativeHeader("code",errorCode.getCode());

        return MessageBuilder.createMessage(
                errorCode.getMessage().getBytes(StandardCharsets.UTF_8),
                accessor.getMessageHeaders()
        );
    }
}

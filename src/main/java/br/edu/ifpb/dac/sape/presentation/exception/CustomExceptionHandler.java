package br.edu.ifpb.dac.sape.presentation.exception;

import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(ErrorMessage.of(errorMessage));
    }

    @ExceptionHandler(SuapClientException.class)
    public ResponseEntity<ErrorMessage> handleSuapClientException(SuapClientException ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.badRequest().body(ErrorMessage.of(errorMessage));
    }

    @Builder
    public static class ErrorMessage {
        private String detailMessage;

        public static ErrorMessage of(String detailMessage) {
            return ErrorMessage.builder().detailMessage(detailMessage).build();
        }
    }

}

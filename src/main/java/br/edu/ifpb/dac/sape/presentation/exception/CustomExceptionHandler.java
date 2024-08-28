package br.edu.ifpb.dac.sape.presentation.exception;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorMessage handleException(Throwable ex) {
        String errorMessage = ex.getLocalizedMessage();
        return ErrorMessage.of(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorMessage handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorMessage = ex.getMessage();
        return ErrorMessage.of(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorMessage handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ErrorMessage.of(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SuapClientException.class)
    public ErrorMessage handleSuapClientException(SuapClientException ex) {
        String errorMessage = ex.getMessage();
        return ErrorMessage.of(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaximumParticipantCapacityExceededException.class)
    public ErrorMessage handleMaximumParticipantCapacityExceededException(MaximumParticipantCapacityExceededException ex) {
        String errorMessage = ex.getMessage();
        return ErrorMessage.of(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingFieldException.class)
    public ErrorMessage handleMissingFieldException(MissingFieldException ex) {
        String errorMessage = ex.getMessage();
        return ErrorMessage.of(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FavoriteSportException.class)
    public ErrorMessage handleFavoriteSportException(FavoriteSportException ex) {
        String errorMessage = ex.getMessage();
        return ErrorMessage.of(errorMessage);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ErrorMessage handleObjectAlreadyExistsException(ObjectAlreadyExistsException ex) {
        String errorMessage = ex.getMessage();
        return ErrorMessage.of(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TimeAlreadyScheduledException.class)
    public ErrorMessage handleTimeAlreadyScheduledException(TimeAlreadyScheduledException ex) {
        String errorMessage = ex.getMessage();
        return ErrorMessage.of(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuleViolationException.class)
    public ErrorMessage handleRuleViolationException(RuleViolationException ex) {
        String errorMessage = ex.getMessage();
        return ErrorMessage.of(errorMessage);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ErrorMessage handleObjectNotFoundException(ObjectNotFoundException ex) {
        String errorMessage = ex.getMessage();
        return ErrorMessage.of(errorMessage);
    }

    @Builder
    @Data
    public static class ErrorMessage {
        private String detailMessage;

        public static ErrorMessage of(String detailMessage) {
            return ErrorMessage.builder().detailMessage(detailMessage).build();
        }
    }

}

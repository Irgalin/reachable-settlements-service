package com.github.irgalin.reachablesettlements;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.irgalin.reachablesettlements.service.SettlementsServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SettlementsServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(Exception serviceExc, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage(serviceExc.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);

    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    private class ErrorResponse {

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
        private LocalDateTime timestamp;
        private String message;

        public ErrorResponse() {
        }

        public ErrorResponse(LocalDateTime timestamp, int status, String message) {
            this.timestamp = timestamp;
            this.message = message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(@NotNull LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(@NotNull String message) {
            this.message = message;
        }
    }


}

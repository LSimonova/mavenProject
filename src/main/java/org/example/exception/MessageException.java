package org.example.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class MessageException {
    private final String message;
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public MessageException(String message, HttpStatus status, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

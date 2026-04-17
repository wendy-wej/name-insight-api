package com.wendy_wej.name_insight_api.exception;

import com.wendy_wej.name_insight_api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<?> handleExternalApiException(ExternalApiException ex) {
        return ResponseEntity
                .status(502)
                .body(new ApiResponse<>(
                        "error",      // status
                        ex.getMessage(), // message
                        null,         // no data on errors
                        null          // no count on errors
                ));
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<?> handleProfileNotFoundException(ProfileNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(new ApiResponse<>(
                        "error",
                        ex.getMessage(),
                        null,
                        null
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(500)
                .body(new ApiResponse<>("error", "An unexpected error occurred", null, null));
    }
}

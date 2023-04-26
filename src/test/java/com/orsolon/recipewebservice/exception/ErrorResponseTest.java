package com.orsolon.recipewebservice.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ErrorResponseTest {
    @Test
    public void testErrorResponseBuilder_goodPath() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(404)
                .error("Not Found")
                .message("The requested resource was not found.")
                .path("/api/resource")
                .build();

        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("The requested resource was not found.", errorResponse.getMessage());
        assertEquals("/api/resource", errorResponse.getPath());
    }

    @Test
    public void testErrorResponseConstructor_goodPath() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse(timestamp, 400, "Bad Request", "/api/resource");

        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(400, errorResponse.getStatus());
        assertEquals(null, errorResponse.getError());
        assertEquals("Bad Request", errorResponse.getMessage());
        assertEquals("/api/resource", errorResponse.getPath());
    }

    @Test
    public void testErrorResponseEqualsAndHashCode_badPath() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse errorResponse1 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(404)
                .error("Not Found")
                .message("The requested resource was not found.")
                .path("/api/resource")
                .build();

        ErrorResponse errorResponse2 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(404)
                .error("Not Found")
                .message("The requested resource was not found. This is a different message.")
                .path("/api/resource")
                .build();

        assertNotEquals(errorResponse1, errorResponse2);
        assertNotEquals(errorResponse1.hashCode(), errorResponse2.hashCode());
    }

    @Test
    public void testErrorResponseToString_badPath() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse errorResponse1 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(404)
                .error("Not Found")
                .message("The requested resource was not found.")
                .path("/api/resource")
                .build();

        ErrorResponse errorResponse2 = ErrorResponse.builder()
                .timestamp(timestamp.plusSeconds(1))
                .status(404)
                .error("Not Found")
                .message("The requested resource was not found.")
                .path("/api/resource")
                .build();

        assertNotEquals(errorResponse1.toString(), errorResponse2.toString());
    }
}

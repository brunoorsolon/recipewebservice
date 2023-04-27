package com.orsolon.recipewebservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Error Response Test")
public class ErrorResponseTest {

    @Test
    @DisplayName("Build with valid data should return ErrorResponse")
    public void build_WithValidData_ShouldReturnErrorResponse() {
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
    @DisplayName("Construct with valid data should return ErrorResponse")
    public void construct_WithValidData_ShouldReturnErrorResponse() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse(timestamp, 400, "Bad Request", "/api/resource");

        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(400, errorResponse.getStatus());
        assertNull(errorResponse.getError());
        assertEquals("Bad Request", errorResponse.getMessage());
        assertEquals("/api/resource", errorResponse.getPath());
    }

    @Test
    @DisplayName("Equals and hashCode with different data should not be equal")
    public void equalsAndHashCode_WithDifferentData_ShouldNotBeEqual() {
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
    @DisplayName("ToString with different data should not be equal")
    public void toString_WithDifferentData_ShouldNotBeEqual() {
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

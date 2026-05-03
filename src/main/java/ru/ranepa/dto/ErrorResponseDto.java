package ru.ranepa.dto;

import java.util.List;

public record ErrorResponseDto(
        String message,
        List<String> errors
) {
    public ErrorResponseDto(String message) {
        this(message, List.of());
    }
}
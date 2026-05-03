package ru.ranepa.dto;

import java.math.BigDecimal;

public record EmployeeStatsDto(
        BigDecimal averageSalary,
        EmployeeResponseDto highestPaidEmployee
) {
}
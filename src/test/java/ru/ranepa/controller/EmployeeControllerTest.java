package ru.ranepa.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.ranepa.model.Employee;
import ru.ranepa.service.EmployeeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void shouldReturnAllEmployees() throws Exception {
        Employee employee = new Employee(
                "Ivan Ivanov",
                "Developer",
                new BigDecimal("150000"),
                LocalDate.of(2024, 1, 1)
        );

        when(employeeService.findAllEmployees(any()))
                .thenReturn(new PageImpl<>(List.of(employee)));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Ivan Ivanov"));
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        Employee employee = new Employee(
                "Ivan Ivanov",
                "Developer",
                new BigDecimal("150000"),
                LocalDate.of(2024, 1, 1)
        );

        when(employeeService.findEmployeeById(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan Ivanov"));
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeDoesNotExist() throws Exception {
        when(employeeService.findEmployeeById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateEmployee() throws Exception {
        Employee employee = new Employee(
                "Ivan Ivanov",
                "Developer",
                new BigDecimal("150000"),
                LocalDate.of(2024, 1, 1)
        );

        when(employeeService.createEmployee(any())).thenReturn(employee);

        String requestJson = """
                {
                  "name": "Ivan Ivanov",
                  "position": "Developer",
                  "salary": 150000,
                  "hireDate": "2024-01-01"
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ivan Ivanov"));
    }

    @Test
    void shouldUpdateEmployee() throws Exception {
        Employee employee = new Employee(
                "Petr Petrov",
                "QA Engineer",
                new BigDecimal("180000"),
                LocalDate.of(2024, 2, 1)
        );

        when(employeeService.updateEmployee(eq(1L), any())).thenReturn(employee);

        String requestJson = """
                {
                  "name": "Petr Petrov",
                  "position": "QA Engineer",
                  "salary": 180000,
                  "hireDate": "2024-02-01"
                }
                """;

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Petr Petrov"))
                .andExpect(jsonPath("$.position").value("QA Engineer"));
    }

    @Test
    void shouldReturnStatisticsWhenDatabaseIsEmpty() throws Exception {
        when(employeeService.calculateAverageSalary()).thenReturn(BigDecimal.ZERO);
        when(employeeService.findHighestPaidEmployee()).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageSalary").value(0))
                .andExpect(jsonPath("$.highestPaidEmployee").doesNotExist());
    }
}
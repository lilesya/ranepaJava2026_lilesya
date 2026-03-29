package ru.ranepa.service;

import org.junit.jupiter.api.Test;
import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepositoryImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    @Test
    void shouldCalculateAverageSalary() {
        EmployeeService employeeService = new EmployeeService(new EmployeeRepositoryImpl());

        employeeService.addEmployee(new Employee(
                "Makanova Olesya",
                "Systems Analyst",
                LocalDate.of(2025, 10, 5),
                200000.0
        ));

        employeeService.addEmployee(new Employee(
                "Tashkeeva Maria",
                "Business Analyst",
                LocalDate.of(2025, 5, 10),
                185000.0
        ));

        employeeService.addEmployee(new Employee(
                "Karpova Valeriya",
                "Developer",
                LocalDate.of(2025, 10, 5),
                250000.0
        ));

        employeeService.addEmployee(new Employee(
                "Privalova Vladislava",
                "Tester",
                LocalDate.of(2025, 5, 10),
                215000.0
        ));

        double averageSalary = employeeService.calculateAverageSalary();

        assertEquals(212500.0, averageSalary);
    }

    @Test
    void shouldFindHighestPaidEmployee() {
        EmployeeService employeeService = new EmployeeService(new EmployeeRepositoryImpl());

        employeeService.addEmployee(new Employee(
                "Makanova Olesya",
                "Systems Analyst",
                LocalDate.of(2025, 10, 5),
                200000.0
        ));

        employeeService.addEmployee(new Employee(
                "Tashkeeva Maria",
                "Business Analyst",
                LocalDate.of(2025, 5, 10),
                185000.0
        ));

        employeeService.addEmployee(new Employee(
                "Karpova Valeriya",
                "Developer",
                LocalDate.of(2025, 10, 5),
                250000.0
        ));

        employeeService.addEmployee(new Employee(
                "Privalova Vladislava",
                "Tester",
                LocalDate.of(2025, 5, 10),
                215000.0
        ));

        Optional<Employee> highestPaidEmployee = employeeService.findHighestPaidEmployee();

        assertTrue(highestPaidEmployee.isPresent());
        assertEquals("Karpova Valeriya", highestPaidEmployee.get().getName());
        assertEquals(250000.0, highestPaidEmployee.get().getSalary());
    }

    @Test
    void shouldReturnEmptyOptionalWhenNoEmployeesExist() {
        EmployeeService employeeService = new EmployeeService(new EmployeeRepositoryImpl());

        Optional<Employee> highestPaidEmployee = employeeService.findHighestPaidEmployee();

        assertTrue(highestPaidEmployee.isEmpty());
    }
}
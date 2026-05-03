package ru.ranepa.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ranepa.dto.EmployeeRequestDto;
import ru.ranepa.dto.EmployeeResponseDto;
import ru.ranepa.dto.EmployeeStatsDto;
import ru.ranepa.model.Employee;
import ru.ranepa.service.EmployeeService;
import ru.ranepa.exception.EmployeeNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public Page<EmployeeResponseDto> findAllEmployees(Pageable pageable) {
        return employeeService.findAllEmployees(pageable)
                .map(this::toResponseDto);
    }

    @GetMapping("/{id}")
    public EmployeeResponseDto findEmployeeById(@PathVariable Long id) {
        return employeeService.findEmployeeById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponseDto createEmployee(@Valid @RequestBody EmployeeRequestDto requestDto) {
        Employee employee = new Employee(
                requestDto.name(),
                requestDto.position(),
                requestDto.salary(),
                requestDto.hireDate()
        );

        return toResponseDto(employeeService.createEmployee(employee));
    }

    @PutMapping("/{id}")
    public EmployeeResponseDto updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto requestDto
    ) {
        Employee employee = new Employee(
                requestDto.name(),
                requestDto.position(),
                requestDto.salary(),
                requestDto.hireDate()
        );

        return toResponseDto(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/position/{position}")
    public List<EmployeeResponseDto> findEmployeesByPosition(@PathVariable String position) {
        return employeeService.findEmployeesByPosition(position).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @GetMapping("/stats")
    public EmployeeStatsDto getStatistics() {
        return new EmployeeStatsDto(
                employeeService.calculateAverageSalary(),
                employeeService.findHighestPaidEmployee()
                        .map(this::toResponseDto)
                        .orElse(null)
        );
    }

    private EmployeeResponseDto toResponseDto(Employee employee) {
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getPosition(),
                employee.getSalary(),
                employee.getHireDate(),
                employee.getCreatedAt()
        );
    }
}
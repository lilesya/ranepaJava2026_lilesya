package ru.ranepa.repository;

import ru.ranepa.model.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final Map<Long, Employee> employees = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Employee save(Employee employee) {
        employee.setId(nextId);
        employees.put(nextId, employee);
        nextId++;
        return employee;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(employees.get(id));
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    @Override
    public boolean delete(Long id) {
        return employees.remove(id) != null;
    }
}
package ru.ranepa.repository;

import ru.ranepa.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    Employee save(Employee employee);
    Optional<Employee> findById(Long id);
    List<Employee> findAll();
    boolean delete(Long id);
    void saveToFile(String filename);
}
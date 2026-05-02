package ru.ranepa.repository;

import ru.ranepa.model.Employee;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private static final String CSV_HEADER = "id,name,position,salary,hireDate";

    private final Map<Long, Employee> employees = new HashMap<>();
    private long nextId = 1L;

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

    @Override
    public void saveToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(CSV_HEADER);

            for (Employee employee : employees.values()) {
                writer.printf(
                        "%d,%s,%s,%s,%s%n",
                        employee.getId(),
                        employee.getName(),
                        employee.getPosition(),
                        employee.getSalary(),
                        employee.getHireDate()
                );
            }

            System.out.println("Data saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
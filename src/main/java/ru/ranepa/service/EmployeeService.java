package ru.ranepa.service;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public boolean deleteEmployee(Long id) {
        return employeeRepository.delete(id);
    }

    public double calculateAverageSalary() {
        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;

        for (Employee employee : employees) {
            sum += employee.getSalary();
        }

        return sum / employees.size();
    }

    public Optional<Employee> findHighestPaidEmployee() {
        return employeeRepository.findAll().stream()
                .max(Comparator.comparingDouble(Employee::getSalary));
    }

    public List<Employee> findEmployeesByPosition(String position) {
        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("Position cannot be empty");
        }

        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getPosition().equalsIgnoreCase(position))
                .toList();
    }

    public List<Employee> getEmployeesSortedByName() {
        return employeeRepository.findAll().stream()
                .sorted(Comparator.comparing(Employee::getName))
                .toList();
    }

    public List<Employee> getEmployeesSortedByHireDate() {
        return employeeRepository.findAll().stream()
                .sorted(Comparator.comparing(Employee::getHireDate))
                .toList();
    }
    public void saveEmployeesToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("id,name,position,salary,hireDate");
            writer.newLine();

            for (Employee employee : employeeRepository.findAll()) {
                writer.write(
                        employee.getId() + "," +
                                employee.getName() + "," +
                                employee.getPosition() + "," +
                                employee.getSalary() + "," +
                                employee.getHireDate()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save employees to file", e);
        }
    }
}

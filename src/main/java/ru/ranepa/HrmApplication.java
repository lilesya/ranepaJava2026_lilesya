package ru.ranepa;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepositoryImpl;
import ru.ranepa.service.EmployeeService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class HrmApplication {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmployeeService employeeService = new EmployeeService(new EmployeeRepositoryImpl());

        while (true) {
            System.out.println("\n=== HRM System Menu ===");
            System.out.println("1. Show all employees");
            System.out.println("2. Add employee");
            System.out.println("3. Delete employee by ID");
            System.out.println("4. Find employee by ID");
            System.out.println("5. Show statistics");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showAllEmployees(employeeService);
                    break;

                case "2":
                    addEmployee(scanner, employeeService);
                    break;

                case "3":
                    deleteEmployee(scanner, employeeService);
                    break;

                case "4":
                    findEmployeeById(scanner, employeeService);
                    break;

                case "5":
                    showStatistics(employeeService);
                    break;

                case "0":
                    employeeService.saveEmployeesToFile("employees.csv");
                    System.out.println("Employees saved to employees.csv");
                    System.out.println("Program finished.");
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void showAllEmployees(EmployeeService employeeService) {
        if (employeeService.getAllEmployees().isEmpty()) {
            System.out.println("Employee list is empty.");
            return;
        }

        for (Employee employee : employeeService.getAllEmployees()) {
            System.out.println(employee);
        }
    }

    private static void addEmployee(Scanner scanner, EmployeeService employeeService) {
        try {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();

            System.out.print("Enter position: ");
            String position = scanner.nextLine();

            System.out.print("Enter salary: ");
            double salary = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter hire date (yyyy-mm-dd): ");
            LocalDate hireDate = LocalDate.parse(scanner.nextLine());

            Employee employee = new Employee(name, position, hireDate, salary);
            Employee savedEmployee = employeeService.addEmployee(employee);

            System.out.println("Employee added successfully with ID: " + savedEmployee.getId());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use yyyy-mm-dd.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteEmployee(Scanner scanner, EmployeeService employeeService) {
        try {
            System.out.print("Enter employee ID to delete: ");
            Long id = Long.parseLong(scanner.nextLine());

            boolean deleted = employeeService.deleteEmployee(id);

            if (deleted) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Employee not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void findEmployeeById(Scanner scanner, EmployeeService employeeService) {
        try {
            System.out.print("Enter employee ID to find: ");
            Long id = Long.parseLong(scanner.nextLine());

            employeeService.findEmployeeById(id)
                    .ifPresentOrElse(
                            employee -> System.out.println("Employee found: " + employee),
                            () -> System.out.println("Employee not found.")
                    );
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void showStatistics(EmployeeService employeeService) {
        double averageSalary = employeeService.calculateAverageSalary();
        System.out.println("Average salary: " + averageSalary);

        employeeService.findHighestPaidEmployee()
                .ifPresentOrElse(
                        employee -> System.out.println("Highest paid employee: " + employee),
                        () -> System.out.println("No employees in the system.")
                );
    }
}
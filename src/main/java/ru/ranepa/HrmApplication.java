package ru.ranepa;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepositoryImpl;
import ru.ranepa.service.EmployeeService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class HrmApplication {
    private static final String EMPLOYEES_FILE_NAME = "employees.csv";

    private static final Scanner scanner = new Scanner(System.in);
    private static final EmployeeRepositoryImpl repository = new EmployeeRepositoryImpl();
    private static final EmployeeService employeeService = new EmployeeService(repository);

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showAllEmployees();
                    break;
                case "2":
                    addEmployee();
                    break;
                case "3":
                    deleteEmployee();
                    break;
                case "4":
                    findEmployeeById();
                    break;
                case "5":
                    showStatistics();
                    break;
                case "0":
                    exitApplication();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== HRM System Menu ===");
        System.out.println("1. Show all employees");
        System.out.println("2. Add employee");
        System.out.println("3. Delete employee by ID");
        System.out.println("4. Find employee by ID");
        System.out.println("5. Show statistics");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void showAllEmployees() {
        List<Employee> employees = repository.findAll();

        if (employees.isEmpty()) {
            System.out.println("No employees found");
            return;
        }

        employees.forEach(System.out::println);
    }

    private static void addEmployee() {
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

    private static void deleteEmployee() {
        try {
            System.out.print("Enter employee ID to delete: ");
            Long id = Long.parseLong(scanner.nextLine());

            boolean deleted = employeeService.deleteEmployee(id);

            if (deleted) {
                System.out.println("Employee deleted successfully.");
                return;
            }

            System.out.println("Employee not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void findEmployeeById() {
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

    private static void showStatistics() {
        double averageSalary = employeeService.calculateAverageSalary();
        System.out.println("Average salary: " + averageSalary);

        employeeService.findHighestPaidEmployee()
                .ifPresentOrElse(
                        employee -> System.out.println("Highest paid employee: " + employee),
                        () -> System.out.println("No employees in the system.")
                );
    }

    private static void exitApplication() {
        repository.saveToFile(EMPLOYEES_FILE_NAME);
        System.out.println("Employees saved to " + EMPLOYEES_FILE_NAME);
        System.out.println("Program finished.");
    }
}
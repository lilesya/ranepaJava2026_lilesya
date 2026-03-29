package ru.ranepa;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepositoryImpl;

import java.sql.SQLOutput;
import java.time.LocalDate;

public class HrmApplication {
    public static void main(String[] args) {
        Employee emp = new Employee(
                "Makanova Olesya Sergeevna",
                "analitic",
                LocalDate.of(2026, 3, 2),
                185_000.0
                );

       //sout
        System.out.println(emp.getSalary());

        var repo = new EmployeeRepositoryImpl();
        System.out.println("===========");
        System.out.println(repo.save(emp));
        System.out.println("===========");
        var emp1 = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        System.out.println("Employee was found: " + emp1);
    }
}
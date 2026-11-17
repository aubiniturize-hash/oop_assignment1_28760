package com.auca.teacher;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;

// Person -> AcademicMember -> Teacher  (hierarchical inheritance: Student and Teacher share AcademicMember)
public class Teacher extends AcademicMember {
    private String employeeNumber;
    private String specialization;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String officeNumber;
    private String employmentType;

    public Teacher(String personId, String firstName, String lastName, String email, String phoneNumber,
            LocalDate dateOfBirth, String memberNumber, Department department, LocalDate joinDate,
            String academicStatus, String institutionalEmail, String advisorName, String employeeNumber,
            String specialization, LocalDate hireDate, BigDecimal salary, String officeNumber, String employmentType) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth, memberNumber, department, joinDate,
                academicStatus, institutionalEmail, advisorName);
        this.employeeNumber = employeeNumber;
        this.specialization = specialization;
        this.hireDate = hireDate;
        this.salary = salary;
        this.officeNumber = officeNumber;
        this.employmentType = employmentType;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    @Override
    public String getRoleDescription() {
        return "Teacher";
    }

    @Override
    public String getAcademicCategory() {
        return "Instructor";
    }

    @Override
    public String getDisplayName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public String getId() {
        return employeeNumber;
    }

    @Override
    public boolean isActive() {
        return DEFAULT_STATUS.equalsIgnoreCase(getAcademicStatus());
    }

    @Override
    public String getRegistrationSummary() {
        return "Teacher " + employeeNumber + " | " + getDisplayName() + " | " + specialization
                + " | " + employmentType;
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("Employee number: " + employeeNumber);
        System.out.println("Specialization: " + specialization);
        System.out.println("Hire date: " + hireDate);
        System.out.println("Salary: " + salary);
        System.out.println("Office number: " + officeNumber);
        System.out.println("Employment type: " + employmentType);
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Registrable.requireText(employeeNumber, "Employee number", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireText(specialization, "Specialization", 2, 120);
        Registrable.requireNotFuture(hireDate, "Hire date");
        if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Salary must be greater than zero.");
        }
        Registrable.requireText(officeNumber, "Office number", 1, 30);
        Registrable.requireOneOf(employmentType, "Employment type", Registrable.employmentTypes());
    }

    @Override
    public String toString() {
        return super.toString() + " | Teacher " + employeeNumber;
    }
}

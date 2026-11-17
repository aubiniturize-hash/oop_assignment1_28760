package com.auca.abstracts;

import java.time.LocalDate;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;

// Abstract: a person who belongs to a department (Student or Teacher).
public abstract class AcademicMember extends Person {
    private String memberNumber;
    private Department department;
    private LocalDate joinDate;
    private String academicStatus;
    private String institutionalEmail;
    private String advisorName;

    public AcademicMember(String personId, String firstName, String lastName, String email, String phoneNumber,
            LocalDate dateOfBirth, String memberNumber, Department department, LocalDate joinDate,
            String academicStatus, String institutionalEmail, String advisorName) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth); // constructor chaining
        this.memberNumber = memberNumber;
        this.department = department;
        this.joinDate = joinDate;
        this.academicStatus = academicStatus;
        this.institutionalEmail = institutionalEmail;
        this.advisorName = advisorName;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    public String getInstitutionalEmail() {
        return institutionalEmail;
    }

    public void setInstitutionalEmail(String institutionalEmail) {
        this.institutionalEmail = institutionalEmail;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }

    // Every child class must say its academic category
    public abstract String getAcademicCategory();

    @Override
    public void validate() throws ValidationException {
        super.validate(); // first check the Person fields
        Registrable.requireText(memberNumber, "Member number", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireRef(department, "Department");
        Registrable.requireNotFuture(joinDate, "Join date");
        Registrable.requireOneOf(academicStatus, "Academic status", Registrable.academicStatuses());
        Registrable.requireEmail(institutionalEmail, "Institutional email");
        Registrable.requireText(advisorName, "Advisor name", 2, 120);
    }

    @Override
    public void printDetails() {
        super.printDetails(); // print the Person part first
        System.out.println("Member number: " + memberNumber);
        System.out.println("Department: " + (department == null ? "(none)" : department.getDepartmentName()));
        System.out.println("Join date: " + joinDate);
        System.out.println("Academic status: " + academicStatus);
        System.out.println("Institutional email: " + institutionalEmail);
        System.out.println("Advisor name: " + advisorName);
    }

    @Override
    public String toString() {
        return super.toString() + " (member " + memberNumber + ")";
    }
}

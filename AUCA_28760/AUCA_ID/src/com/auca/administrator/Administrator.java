package com.auca.administrator;

import java.time.LocalDate;

import com.auca.abstracts.Person;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;

// Person -> Administrator
public class Administrator extends Person {
    private String staffNumber;
    private String jobTitle;
    private String assignedOffice;
    private LocalDate employmentDate;
    private int accessLevel;
    private boolean active;

    public Administrator(String personId, String firstName, String lastName, String email, String phoneNumber,
            LocalDate dateOfBirth, String staffNumber, String jobTitle, String assignedOffice, LocalDate employmentDate,
            int accessLevel, boolean active) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth);
        this.staffNumber = staffNumber;
        this.jobTitle = jobTitle;
        this.assignedOffice = assignedOffice;
        this.employmentDate = employmentDate;
        this.accessLevel = accessLevel;
        this.active = active;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getAssignedOffice() {
        return assignedOffice;
    }

    public void setAssignedOffice(String assignedOffice) {
        this.assignedOffice = assignedOffice;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    // Also fulfils Identifiable.isActive()
    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String getRoleDescription() {
        return "Administrator";
    }

    @Override
    public String getDisplayName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public String getId() {
        return staffNumber;
    }

    @Override
    public String getRegistrationSummary() {
        return "Administrator " + staffNumber + " | " + getDisplayName() + " | " + jobTitle
                + " | Office: " + assignedOffice;
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("Staff number: " + staffNumber);
        System.out.println("Job title: " + jobTitle);
        System.out.println("Assigned office: " + assignedOffice);
        System.out.println("Employment date: " + employmentDate);
        System.out.println("Access level: " + accessLevel);
        System.out.println("Active: " + (active ? "YES" : "NO"));
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Registrable.requireText(staffNumber, "Staff number", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireText(jobTitle, "Job title", 2, 120);
        Registrable.requireText(assignedOffice, "Assigned office", 1, 60);
        Registrable.requireNotFuture(employmentDate, "Employment date");
        Registrable.requireRange(accessLevel, "Access level", 1, 5);
    }

    @Override
    public String toString() {
        return super.toString() + " | Staff " + staffNumber;
    }
}

package com.auca.student;

import java.time.LocalDate;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Registrable;

// Person -> AcademicMember -> Student  (multilevel inheritance)
public class Student extends AcademicMember {
    private String studentNumber;
    private String programName;
    private LocalDate admissionDate;
    private int studyYear;
    private String guardianPhone;
    private boolean scholarshipStatus;

    public Student(String personId, String firstName, String lastName, String email, String phoneNumber,
            LocalDate dateOfBirth, String memberNumber, Department department, LocalDate joinDate,
            String academicStatus, String institutionalEmail, String advisorName, String studentNumber,
            String programName, LocalDate admissionDate, int studyYear, String guardianPhone,
            boolean scholarshipStatus) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth, memberNumber, department, joinDate,
                academicStatus, institutionalEmail, advisorName);
        this.studentNumber = studentNumber;
        this.programName = programName;
        this.admissionDate = admissionDate;
        this.studyYear = studyYear;
        this.guardianPhone = guardianPhone;
        this.scholarshipStatus = scholarshipStatus;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public int getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(int studyYear) {
        this.studyYear = studyYear;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }

    public boolean isScholarshipStatus() {
        return scholarshipStatus;
    }

    public void setScholarshipStatus(boolean scholarshipStatus) {
        this.scholarshipStatus = scholarshipStatus;
    }

    @Override
    public String getRoleDescription() {
        return "Student";
    }

    @Override
    public String getAcademicCategory() {
        return "Learner";
    }

    @Override
    public String getDisplayName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public String getId() {
        return studentNumber;
    }

    @Override
    public boolean isActive() {
        return DEFAULT_STATUS.equalsIgnoreCase(getAcademicStatus());
    }

    @Override
    public String getRegistrationSummary() {
        return "Student " + studentNumber + " | " + getDisplayName() + " | " + programName
                + " | Year " + studyYear + " | Scholarship: " + (scholarshipStatus ? "YES" : "NO");
    }

    @Override
    public void printDetails() {
        super.printDetails(); // Person + AcademicMember fields
        System.out.println("Student number: " + studentNumber);
        System.out.println("Program name: " + programName);
        System.out.println("Admission date: " + admissionDate);
        System.out.println("Study year: " + studyYear);
        System.out.println("Guardian phone: " + guardianPhone);
        System.out.println("Scholarship: " + (scholarshipStatus ? "YES" : "NO"));
    }

    @Override
    public void validate() throws ValidationException {
        super.validate(); // Person + AcademicMember rules
        Registrable.requireText(studentNumber, "Student number", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireText(programName, "Program name", 2, 120);
        Registrable.requireNotFuture(admissionDate, "Admission date");
        Registrable.requireRange(studyYear, "Study year", 1, 6);
        Registrable.requirePhone(guardianPhone, "Guardian phone");
    }

    @Override
    public String toString() {
        return super.toString() + " | Student " + studentNumber;
    }
}

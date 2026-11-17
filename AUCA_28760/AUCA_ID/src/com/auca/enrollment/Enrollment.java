package com.auca.enrollment;

import java.time.LocalDate;

import com.auca.course.Course;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;

public class Enrollment implements Identifiable, Registrable {
    private String enrollmentId;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private String semester;
    private String enrollmentStatus;

    public Enrollment(String enrollmentId, Student student, Course course, LocalDate enrollmentDate, String semester,
            String enrollmentStatus) {
        this.enrollmentId = enrollmentId;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
        this.semester = semester;
        this.enrollmentStatus = enrollmentStatus;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    @Override
    public String getDisplayName() {
        return student.getDisplayName() + " in " + course.getCourseTitle() + " (" + semester + ")";
    }

    @Override
    public String getId() {
        return enrollmentId;
    }

    @Override
    public boolean isActive() {
        return DEFAULT_STATUS.equalsIgnoreCase(enrollmentStatus);
    }

    @Override
    public String getRegistrationSummary() {
        return "Enrollment " + enrollmentId + " | " + student.getDisplayName() + " -> " + course.getCourseTitle()
                + " | " + semester + " | " + enrollmentStatus;
    }

    @Override
    public void printDetails() {
        System.out.println("Enrollment ID: " + enrollmentId);
        System.out.println("Student: [" + student.getStudentNumber() + "] " + student.getDisplayName());
        System.out.println("Course: [" + course.getCourseCode() + "] " + course.getCourseTitle());
        System.out.println("Enrollment date: " + enrollmentDate);
        System.out.println("Semester: " + semester);
        System.out.println("Enrollment status: " + enrollmentStatus);
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(enrollmentId, "Enrollment ID", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireRef(student, "Student");
        Registrable.requireRef(course, "Course");
        Registrable.requireNotFuture(enrollmentDate, "Enrollment date");
        Registrable.requireOneOf(semester, "Semester", Registrable.semesters());
        Registrable.requireOneOf(enrollmentStatus, "Enrollment status", Registrable.enrollmentStatuses());
    }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}

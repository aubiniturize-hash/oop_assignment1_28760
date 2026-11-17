package com.auca.course;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.teacher.Teacher;

public class Course implements Identifiable, Registrable {
    private String courseId;
    private String courseCode;
    private String courseTitle;
    private int creditHours;
    private Department department;
    private Teacher assignedTeacher;

    public Course(String courseId, String courseCode, String courseTitle, int creditHours, Department department,
            Teacher assignedTeacher) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.creditHours = creditHours;
        this.department = department;
        this.assignedTeacher = assignedTeacher;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Teacher getAssignedTeacher() {
        return assignedTeacher;
    }

    public void setAssignedTeacher(Teacher assignedTeacher) {
        this.assignedTeacher = assignedTeacher;
    }

    @Override
    public String getDisplayName() {
        return courseTitle;
    }

    @Override
    public String getId() {
        return courseId;
    }

    @Override
    public boolean isActive() {
        return department != null && assignedTeacher != null;
    }

    @Override
    public String getRegistrationSummary() {
        return "Course " + courseId + " | " + courseCode + " | " + courseTitle + " | " + creditHours
                + " credits | Teacher: " + (assignedTeacher == null ? "(none)" : assignedTeacher.getDisplayName());
    }

    @Override
    public void printDetails() {
        System.out.println("Course ID: " + courseId);
        System.out.println("Course code: " + courseCode);
        System.out.println("Course title: " + courseTitle);
        System.out.println("Credit hours: " + creditHours);
        System.out.println("Department: " + (department == null ? "(none)" : department.getDepartmentName()));
        System.out.println("Assigned teacher: " + (assignedTeacher == null ? "(none)" : assignedTeacher.getDisplayName()));
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(courseId, "Course ID", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireText(courseCode, "Course code", 2, 20);
        Registrable.requireText(courseTitle, "Course title", 2, 120);
        Registrable.requireRange(creditHours, "Credit hours", 1, 6);
        Registrable.requireRef(department, "Department");
        Registrable.requireRef(assignedTeacher, "Assigned teacher");
    }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}

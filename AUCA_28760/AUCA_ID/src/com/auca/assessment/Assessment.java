package com.auca.assessment;

import java.time.LocalDate;

import com.auca.enrollment.Enrollment;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

public class Assessment implements Identifiable, Registrable {
    private String assessmentId;
    private Enrollment enrollment;
    private String assessmentType;
    private double maximumScore;
    private double obtainedScore;
    private LocalDate assessmentDate;

    public Assessment(String assessmentId, Enrollment enrollment, String assessmentType, double maximumScore,
            double obtainedScore, LocalDate assessmentDate) {
        this.assessmentId = assessmentId;
        this.enrollment = enrollment;
        this.assessmentType = assessmentType;
        this.maximumScore = maximumScore;
        this.obtainedScore = obtainedScore;
        this.assessmentDate = assessmentDate;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public double getMaximumScore() {
        return maximumScore;
    }

    public void setMaximumScore(double maximumScore) {
        this.maximumScore = maximumScore;
    }

    public double getObtainedScore() {
        return obtainedScore;
    }

    public void setObtainedScore(double obtainedScore) {
        this.obtainedScore = obtainedScore;
    }

    public LocalDate getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDate assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    @Override
    public String getDisplayName() {
        return assessmentType + " (" + enrollment.getStudent().getDisplayName() + ")";
    }

    @Override
    public String getId() {
        return assessmentId;
    }

    @Override
    public boolean isActive() {
        return enrollment != null && enrollment.isActive();
    }

    @Override
    public String getRegistrationSummary() {
        return "Assessment " + assessmentId + " | " + assessmentType + " | " + obtainedScore + "/" + maximumScore
                + " | " + assessmentDate;
    }

    @Override
    public void printDetails() {
        System.out.println("Assessment ID: " + assessmentId);
        System.out.println("Enrollment: [" + enrollment.getEnrollmentId() + "] " + enrollment.getDisplayName());
        System.out.println("Assessment type: " + assessmentType);
        System.out.println("Maximum score: " + maximumScore);
        System.out.println("Obtained score: " + obtainedScore);
        System.out.println("Assessment date: " + assessmentDate);
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(assessmentId, "Assessment ID", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireRef(enrollment, "Enrollment");
        Registrable.requireOneOf(assessmentType, "Assessment type", Registrable.assessmentTypes());
        if (maximumScore <= 0) {
            throw new ValidationException("Maximum score must be greater than zero.");
        }
        if (obtainedScore < 0 || obtainedScore > maximumScore) {
            throw new ValidationException("Obtained score must be between 0 and " + maximumScore + ".");
        }
        Registrable.requireNotFuture(assessmentDate, "Assessment date");
    }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}

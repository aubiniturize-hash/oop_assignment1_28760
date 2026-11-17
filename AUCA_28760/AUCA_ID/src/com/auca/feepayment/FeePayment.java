package com.auca.feepayment;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;

public class FeePayment implements Identifiable, Registrable {
    private String paymentId;
    private Student student;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String receiptNumber;

    public FeePayment(String paymentId, Student student, BigDecimal amount, LocalDate paymentDate, String paymentMethod,
            String receiptNumber) {
        this.paymentId = paymentId;
        this.student = student;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.receiptNumber = receiptNumber;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    @Override
    public String getDisplayName() {
        return "Receipt " + receiptNumber;
    }

    @Override
    public String getId() {
        return paymentId;
    }

    @Override
    public boolean isActive() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0 && paymentDate != null;
    }

    @Override
    public String getRegistrationSummary() {
        return "Payment " + paymentId + " | " + student.getDisplayName() + " | " + amount + " | " + paymentMethod
                + " | Receipt: " + receiptNumber + " | " + paymentDate;
    }

    @Override
    public void printDetails() {
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Student: [" + student.getStudentNumber() + "] " + student.getDisplayName());
        System.out.println("Amount: " + amount);
        System.out.println("Payment date: " + paymentDate);
        System.out.println("Payment method: " + paymentMethod);
        System.out.println("Receipt number: " + receiptNumber);
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(paymentId, "Payment ID", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireRef(student, "Student");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than zero.");
        }
        Registrable.requireNotFuture(paymentDate, "Payment date");
        Registrable.requireOneOf(paymentMethod, "Payment method", Registrable.paymentMethods());
        Registrable.requireText(receiptNumber, "Receipt number", 1, 30);
    }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}

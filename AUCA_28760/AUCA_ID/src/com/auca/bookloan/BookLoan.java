package com.auca.bookloan;

import java.time.LocalDate;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;

public class BookLoan implements Identifiable, Registrable {
    private String loanId;
    private Student student;
    private LibraryBook book;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate; // null until the book is returned

    public BookLoan(String loanId, Student student, LibraryBook book, LocalDate borrowedDate, LocalDate dueDate,
            LocalDate returnedDate) {
        this.loanId = loanId;
        this.student = student;
        this.book = book;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LibraryBook getBook() {
        return book;
    }

    public void setBook(LibraryBook book) {
        this.book = book;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    @Override
    public String getDisplayName() {
        return book.getTitle() + " -> " + student.getDisplayName();
    }

    @Override
    public String getId() {
        return loanId;
    }

    // A loan is active while the book has not been returned
    @Override
    public boolean isActive() {
        return returnedDate == null;
    }

    @Override
    public String getRegistrationSummary() {
        return "Loan " + loanId + " | " + student.getDisplayName() + " | " + book.getTitle() + " | Borrowed: "
                + borrowedDate + " | Due: " + dueDate + " | Returned: "
                + (returnedDate == null ? "not yet" : returnedDate.toString());
    }

    @Override
    public void printDetails() {
        System.out.println("Loan ID: " + loanId);
        System.out.println("Student: [" + student.getStudentNumber() + "] " + student.getDisplayName());
        System.out.println("Book: [" + book.getBookId() + "] " + book.getTitle());
        System.out.println("Borrowed date: " + borrowedDate);
        System.out.println("Due date: " + dueDate);
        System.out.println("Returned date: " + (returnedDate == null ? "(not returned yet)" : returnedDate.toString()));
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(loanId, "Loan ID", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireRef(student, "Student");
        Registrable.requireRef(book, "Library book");
        Registrable.requireNotFuture(borrowedDate, "Borrowed date");
        Registrable.requireRef(dueDate, "Due date");
        if (!dueDate.isAfter(borrowedDate)) {
            throw new ValidationException("Due date must be after the borrowed date.");
        }
        if (returnedDate != null && returnedDate.isBefore(borrowedDate)) {
            throw new ValidationException("Returned date cannot be before the borrowed date.");
        }
    }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}

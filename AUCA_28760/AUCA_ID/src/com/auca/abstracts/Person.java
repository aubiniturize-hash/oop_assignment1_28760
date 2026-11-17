package com.auca.abstracts;

import java.time.LocalDate;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

// Abstract: a "Person" alone is too general, so it cannot be created with new.
// Only its children (Student, Teacher, Administrator) can be created.
public abstract class Person implements Identifiable, Registrable {
    private String personId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    public Person(String personId, String firstName, String lastName, String email, String phoneNumber,
            LocalDate dateOfBirth) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Every child class must say what kind of person it is
    public abstract String getRoleDescription();

    // Validates the 6 Person fields. Children call super.validate() first.
    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(personId, "Person ID", MIN_ID_LENGTH, MAX_ID_LENGTH);
        Registrable.requireText(firstName, "First name", MIN_NAME_LENGTH, MAX_NAME_LENGTH);
        Registrable.requireText(lastName, "Last name", MIN_NAME_LENGTH, MAX_NAME_LENGTH);
        Registrable.requireEmail(email, "Email");
        Registrable.requirePhone(phoneNumber, "Phone");
        Registrable.requirePast(dateOfBirth, "Date of birth");
    }

    // Prints the Person part. Children call super.printDetails() and then print their own fields.
    @Override
    public void printDetails() {
        System.out.println("Role: " + getRoleDescription());
        System.out.println("Person ID: " + personId);
        System.out.println("First name: " + firstName);
        System.out.println("Last name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Date of birth: " + dateOfBirth);
    }

    @Override
    public String toString() {
        return personId + " - " + firstName + " " + lastName;
    }
}

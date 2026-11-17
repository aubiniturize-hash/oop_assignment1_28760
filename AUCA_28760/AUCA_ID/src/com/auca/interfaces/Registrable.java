package com.auca.interfaces;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import com.auca.exceptions.ValidationException;

public interface Registrable {
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 60;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MIN_PHONE_LENGTH = 10;
    public static final int MAX_PHONE_LENGTH = 15;
    public static final String DATE_PATTERN = "uuuu-MM-dd"; // shown to users as YYYY-MM-DD

    void validate() throws ValidationException;
    void printDetails();
    String getRegistrationSummary();

    // ------------------------------------------------------------------
    // Static helper methods: shared validation rules used by the entity
    // classes (validate()) and by the console forms in Main.
    // They are called like this:  Registrable.requireText(...)
    // ------------------------------------------------------------------

    // Allowed values (one place only, so forms and validate() always agree)
    static String[] academicStatuses()  { return new String[] {"ACTIVE", "INACTIVE"}; }
    static String[] enrollmentStatuses(){ return new String[] {"ACTIVE", "COMPLETED", "DROPPED"}; }
    static String[] semesters()         { return new String[] {"FALL", "SPRING", "SUMMER"}; }
    static String[] assessmentTypes()   { return new String[] {"QUIZ", "ASSIGNMENT", "MIDTERM", "FINAL_EXAM", "PROJECT"}; }
    static String[] paymentMethods()    { return new String[] {"CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CARD"}; }
    static String[] employmentTypes()   { return new String[] {"FULL_TIME", "PART_TIME", "CONTRACT"}; }

    // Not blank, trimmed, and length between min and max. Returns the trimmed text.
    static String requireText(String value, String label, int min, int max) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(label + " cannot be blank.");
        }
        String text = value.trim();
        if (text.length() < min || text.length() > max) {
            throw new ValidationException(label + " must be " + min + "-" + max + " characters long.");
        }
        return text;
    }

    static String requireEmail(String value, String label) throws ValidationException {
        String text = requireText(value, label, 5, MAX_EMAIL_LENGTH);
        if (!text.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ValidationException(label + " is not a valid email (example: name@example.com).");
        }
        return text;
    }

    // 10-15 digits with an optional leading +
    static String requirePhone(String value, String label) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(label + " cannot be blank.");
        }
        String text = value.trim();
        if (!text.matches("^\\+?[0-9]{" + MIN_PHONE_LENGTH + "," + MAX_PHONE_LENGTH + "}$")) {
            throw new ValidationException(label + " must contain " + MIN_PHONE_LENGTH + "-"
                    + MAX_PHONE_LENGTH + " digits (optional leading +).");
        }
        return text;
    }

    static void requireRef(Object reference, String label) throws ValidationException {
        if (reference == null) {
            throw new ValidationException(label + " is required.");
        }
    }

    static void requireRange(int value, String label, int min, int max) throws ValidationException {
        if (value < min || value > max) {
            throw new ValidationException(label + " must be between " + min + " and " + max + ".");
        }
    }

    static void requireOneOf(String value, String label, String[] allowed) throws ValidationException {
        for (String option : allowed) {
            if (option.equalsIgnoreCase(value)) {
                return;
            }
        }
        throw new ValidationException(label + " must be one of: " + String.join(", ", allowed) + ".");
    }

    static void requirePast(LocalDate date, String label) throws ValidationException {
        requireRef(date, label);
        if (!date.isBefore(LocalDate.now())) {
            throw new ValidationException(label + " must be in the past.");
        }
    }

    static void requireNotFuture(LocalDate date, String label) throws ValidationException {
        requireRef(date, label);
        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException(label + " cannot be in the future.");
        }
    }

    // ---- Parsing text typed by the user (NumberFormatException and
    // ---- DateTimeParseException are caught here and turned into ValidationException)

    static LocalDate parseDate(String value, String label) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(label + " cannot be blank.");
        }
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_PATTERN).withResolverStyle(ResolverStyle.STRICT);
            return LocalDate.parse(value.trim(), format);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid date: use YYYY-MM-DD (example: 2004-05-16).");
        }
    }

    static int parseInt(String value, String label) throws ValidationException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be a whole number.");
        }
    }

    static double parseDouble(String value, String label) throws ValidationException {
        double number;
        try {
            number = Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be a number.");
        }
        if (Double.isNaN(number) || Double.isInfinite(number)) {
            throw new ValidationException(label + " must be a valid number.");
        }
        return number;
    }

    // Money: BigDecimal, strictly positive, 2 decimal places
    static BigDecimal parseMoney(String value, String label) throws ValidationException {
        BigDecimal money;
        try {
            money = new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be a valid amount (example: 150000.50).");
        }
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(label + " must be greater than zero.");
        }
        return money.setScale(2, RoundingMode.HALF_UP);
    }
}

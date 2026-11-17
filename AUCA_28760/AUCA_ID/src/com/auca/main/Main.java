package com.auca.main;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.auca.abstracts.AcademicMember;
import com.auca.abstracts.Person;
import com.auca.administrator.Administrator;
import com.auca.assessment.Assessment;
import com.auca.bookloan.BookLoan;
import com.auca.classroom.Classroom;
import com.auca.course.Course;
import com.auca.department.Department;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.BookUnavailableException;
import com.auca.exceptions.DuplicateRecordException;
import com.auca.exceptions.PrerequisiteMissingException;
import com.auca.exceptions.RecordNotFoundException;
import com.auca.exceptions.ValidationException;
import com.auca.feepayment.FeePayment;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import com.auca.teacher.Teacher;


public class Main {
    // 1. ONE shared Scanner and the in-memory lists
    static Scanner sc = new Scanner(System.in);

    static List<Administrator> administrators = new ArrayList<>();
    static List<Assessment> assessments = new ArrayList<>();
    static List<BookLoan> bookLoans = new ArrayList<>();
    static List<Classroom> classrooms = new ArrayList<>();
    static List<Course> courses = new ArrayList<>();
    static List<Department> departments = new ArrayList<>();
    static List<Enrollment> enrollments = new ArrayList<>();
    static List<FeePayment> feePayments = new ArrayList<>();
    static List<LibraryBook> libraryBooks = new ArrayList<>();
    static List<Student> students = new ArrayList<>();
    static List<Teacher> teachers = new ArrayList<>();

 
    // 2. Small helper types used by the forms
    // Thrown when the user cancels a form. It is caught at the menu (or in a nested form).
    private static class CancelException extends RuntimeException {
        CancelException() {
            super("Cancelled by the user");
        }
    }

    // Checks the text typed by the user and returns the converted value (String, int, LocalDate...)
    private interface Checker {
        Object check(String input) throws ValidationException, DuplicateRecordException;
    }

    // Lets the user pick an existing record from a numbered list
    private interface Selector {
        Object select();
    }

    // Creates, validates and stores the record. Returns the success message.
    private interface Saver {
        String save() throws ValidationException, DuplicateRecordException, BookUnavailableException;
    }

    // Changes one value of an existing record (used by Update Record)
    private interface Setter {
        void set(Object value);
    }

    // One question of a form
    private static class Field {
        private String group;     // the step title, e.g. "PERSONAL DETAILS"
        private String label;     // e.g. "First name"
        private String hint;      // e.g. "String | 2-60 chars"
        private Checker checker;  // used for typed fields
        private Selector selector; // used for fields chosen from a list
        private Object value;     // the answer

        Field(String group, String label, String hint, Checker checker) {
            this.group = group;
            this.label = label;
            this.hint = hint;
            this.checker = checker;
        }

        Field(String group, String label, String hint, Selector selector) {
            this.group = group;
            this.label = label;
            this.hint = hint;
            this.selector = selector;
        }
    }

    // One editable value in "Update Existing Record"
    private static class Edit {
        private String label;
        private String hint;
        private Checker checker;
        private Selector selector;
        private Supplier<Object> getter;
        private Setter setter;

        Edit(String label, String hint, Checker checker, Supplier<Object> getter, Setter setter) {
            this.label = label;
            this.hint = hint;
            this.checker = checker;
            this.getter = getter;
            this.setter = setter;
        }

        Edit(String label, String hint, Selector selector, Supplier<Object> getter, Setter setter) {
            this.label = label;
            this.hint = hint;
            this.selector = selector;
            this.getter = getter;
            this.setter = setter;
        }
    }
    // 3. main() and the menu
    public static void main(String[] args) {

        int choice;

        do {
            printMenu();
            choice = readChoice("Select an option: ", 0, 19);

            try {
                runOption(choice);

            } catch (CancelException e) {
                System.out.println("\nCancelled. Nothing was saved.");
            } catch (PrerequisiteMissingException e) {
                System.out.println("\n! " + e.getMessage());
            } catch (RecordNotFoundException | BookUnavailableException e) {
                System.out.println("\n! " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("\n! Something unexpected happened (" + e.getMessage()
                        + "). Returning to the main menu.");
            }

        } while (choice != 0);

        System.out.println("System closed. Goodbye!");
    }

    private static void printMenu() {
        String[] left = { "Register Department", "Register Student", "Register Teacher",
                "Register Administrator", "Register Course", "Register Classroom", "Enroll Student",
                "Record Assessment", "Record Fee Payment", "Register Library Book" };
        String[] right = { "Borrow Library Book", "Return Library Book", "View All Records",
                "Search Record by ID", "Update Existing Record", "Student Academic Summary",
                "Student Payment History", "View Available Books", "Demonstrate Polymorphism", "Exit" };

        System.out.println("\n============== AUCA_ID SCHOOL MANAGEMENT ==============");
        for (int i = 0; i < 10; i++) {
            String rightNumber = (i == 9) ? "0" : String.valueOf(11 + i);
            System.out.printf("%2d. %-26s %2s. %s%n", i + 1, left[i], rightNumber, right[i]);
        }
    }

    private static void runOption(int choice) throws RecordNotFoundException, BookUnavailableException {

        switch (choice) {

            case 1:
                registerDepartment();
                break;

            case 2:
                registerStudent();
                break;

            case 3:
                registerTeacher();
                break;

            case 4:
                registerAdministrator();
                break;

            case 5:
                registerCourse();
                break;

            case 6:
                registerClassroom();
                break;

            case 7:
                registerEnrollment();
                break;

            case 8:
                registerAssessment();
                break;

            case 9:
                registerFeePayment();
                break;

            case 10:
                registerLibraryBook();
                break;

            case 11:
                borrowBook();
                break;

            case 12:
                returnBook();
                break;

            case 13:
                viewAllRecords();
                break;

            case 14:
                searchById();
                break;

            case 15:
                updateRecord();
                break;

            case 16:
                showAcademicSummary();
                break;

            case 17:
                showPaymentHistory();
                break;

            case 18:
                showAvailableBooks();
                break;

            case 19:
                displayPolymorphism();
                break;

            case 0:
                break;

            default:
                System.out.println("Invalid choice!");
        }
    }
    // 4. Reading input (always whole lines with nextLine(), never nextInt())
    private static String readLine() {
        if (!sc.hasNextLine()) {
            System.out.println("\nNo more input. Closing the system.");
            System.exit(0);
        }
        return sc.nextLine().trim();
    }
    // Asks for a number between min and max. Repeats until the input is valid.
    private static int readChoice(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = readLine();
            try {
                int number = Integer.parseInt(line);
                if (number >= min && number <= max) {
                    return number;
                }
                System.out.println("! Please choose a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("! Invalid input. Please enter a number from " + min + " to " + max + ".");
            }
        }
    }

    // Asks ONE value, repeats until valid. Type CANCEL to stop.
    private static Object askValue(String label, String hint, Checker checker) {
        while (true) {
            System.out.print(label + " [" + hint + "] : ");
            String input = readLine();
            if (input.equalsIgnoreCase("CANCEL")) {
                throw new CancelException();
            }
            try {
                return checker.check(input);
            } catch (ValidationException | DuplicateRecordException e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }
    // 5. The form engine (used by every registration)
    private static void runForm(String title, List<Field> fields, Saver saver) {

        // The steps are the different "group" names, plus one final review step
        List<String> groups = new ArrayList<>();
        for (Field f : fields) {
            if (!groups.contains(f.group)) {
                groups.add(f.group);
            }
        }
        int totalSteps = groups.size() + 1;
        System.out.println("\n+------------------------------------------------------------+");
        System.out.println("| AUCA_ID / SCHOOL MANAGEMENT");
        System.out.println("| " + title + "   (" + totalSteps + " steps)");
        System.out.println("| Type BACK to return to the previous field;");
        System.out.println("| type CANCEL to discard the unsaved record.");
        System.out.println("+------------------------------------------------------------+");
        // ---- Ask the fields one by one ----
        int i = 0;
        String lastGroup = "";
        while (i < fields.size()) {
            Field f = fields.get(i);

            if (!f.group.equals(lastGroup)) {
                System.out.println("\n[STEP " + (groups.indexOf(f.group) + 1) + "/" + totalSteps + "] " + f.group);
                lastGroup = f.group;
            }

            boolean answered = askField(f, i + 1);

            if (answered) {
                i++;
            } else if (i > 0) {
                i--;
                lastGroup = ""; // show the step title again after going back
            } else {
                System.out.println("! This is the first field, you cannot go back.");
            }
        }

        // ---- Review screen: Save / Edit / Cancel ----
        System.out.println("\n[STEP " + totalSteps + "/" + totalSteps + "] REVIEW AND CONFIRM");
        while (true) {
            printReview(fields);
            System.out.println("| [1] Save record   [2] Edit field   [0] Cancel");
            int choice = readChoice("Select option: ", 0, 2);

            if (choice == 0) {
                throw new CancelException();
            }

            if (choice == 1) {
                try {
                    String message = saver.save();
                    System.out.println("\n+ " + message);
                    return;
                } catch (ValidationException | DuplicateRecordException | BookUnavailableException e) {
                    System.out.println("\n! Could not save: " + e.getMessage());
                    System.out.println("  Use [2] Edit field to correct it, or [0] Cancel.");
                }
            } else {
                int number = readChoice("Field number to edit (1-" + fields.size() + "): ", 1, fields.size());
                Field f = fields.get(number - 1);
                System.out.println("Old value: " + show(f.value));
                askField(f, number);
            }
        }
    }

    // Asks one field. Returns false only when the user typed BACK.
    private static boolean askField(Field f, int number) {

        String numberText = String.format("%02d", number);

        // Field chosen from a list of existing records
        if (f.selector != null) {
            System.out.println(numberText + " " + f.label + " [" + f.hint + "]");
            f.value = f.selector.select();
            return true;
        }

        // Typed field
        while (true) {
            System.out.print(String.format("%s %-20s [%s] : ", numberText, f.label, f.hint));
            String input = readLine();

            if (input.equalsIgnoreCase("CANCEL")) {
                throw new CancelException();
            }
            if (input.equalsIgnoreCase("BACK")) {
                return false;
            }

            try {
                f.value = f.checker.check(input);
                return true;
            } catch (ValidationException | DuplicateRecordException e) {
                System.out.println("! " + e.getMessage());
                System.out.println("  Please re-enter field " + numberText + ".");
            }
        }
    }

    private static void printReview(List<Field> fields) {
        System.out.println("\n+---------------------- REVIEW ----------------------+");
        String lastGroup = "";
        for (int i = 0; i < fields.size(); i++) {
            Field f = fields.get(i);
            if (!f.group.equals(lastGroup)) {
                System.out.println("| " + f.group);
                lastGroup = f.group;
            }
            System.out.println(String.format("|  %02d %-20s : %s", i + 1, f.label, show(f.value)));
        }
        System.out.println("+----------------------------------------------------+");
    }

    // Makes a value readable: related records show as [ID] Name, booleans as YES/NO
    private static String show(Object value) {
        if (value == null) {
            return "(none)";
        }
        if (value instanceof Identifiable) {
            Identifiable item = (Identifiable) value;
            return "[" + item.getId() + "] " + item.getDisplayName();
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "YES" : "NO";
        }
        return value.toString();
    }
    // 6. Ready-made Checkers (each one validates and converts the typed text)
    private static Checker textCheck(String label, int min, int max) {
        return v -> Registrable.requireText(v, label, min, max);
    }

    private static Checker emailCheck(String label) {
        return v -> Registrable.requireEmail(v, label);
    }

    private static Checker phoneCheck(String label) {
        return v -> Registrable.requirePhone(v, label);
    }

    // Text that must not already exist (case-insensitive)
    private static Checker uniqueText(String label, int min, int max, Predicate<String> exists) {
        return v -> {
            String text = Registrable.requireText(v, label, min, max);
            if (exists.test(text)) {
                throw new DuplicateRecordException(label + " " + text + " already exists.");
            }
            return text;
        };
    }

    private static Checker uniqueEmail(String label, Predicate<String> exists) {
        return v -> {
            String text = Registrable.requireEmail(v, label);
            if (exists.test(text)) {
                throw new DuplicateRecordException(label + " " + text + " already exists.");
            }
            return text;
        };
    }

    private static Checker pastDateCheck(String label) {
        return v -> {
            LocalDate date = Registrable.parseDate(v, label);
            Registrable.requirePast(date, label);
            return date;
        };
    }

    private static Checker dateCheck(String label) { // date must not be in the future
        return v -> {
            LocalDate date = Registrable.parseDate(v, label);
            Registrable.requireNotFuture(date, label);
            return date;
        };
    }

    private static Checker intCheck(String label, int min, int max) {
        return v -> {
            int number = Registrable.parseInt(v, label);
            Registrable.requireRange(number, label, min, max);
            return number;
        };
    }

    private static Checker moneyCheck(String label) {
        return v -> Registrable.parseMoney(v, label);
    }

    private static Checker positiveDoubleCheck(String label) {
        return v -> {
            double number = Registrable.parseDouble(v, label);
            if (number <= 0) {
                throw new ValidationException(label + " must be greater than zero.");
            }
            return number;
        };
    }

    // Shows "1 ACTIVE / 2 INACTIVE"
    private static String choices(String[] options) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < options.length; i++) {
            if (i > 0) {
                text.append(" / ");
            }
            text.append(i + 1).append(" ").append(options[i]);
        }
        return text.toString();
    }

    // The user types the number (1, 2, ...) or the word itself
    private static Checker choiceCheck(String label, String[] options) {
        return v -> {
            String text = v.trim();
            if (text.matches("[1-9]")) {
                int number = Integer.parseInt(text);
                if (number <= options.length) {
                    return options[number - 1];
                }
            }
            for (String option : options) {
                if (option.equalsIgnoreCase(text)) {
                    return option;
                }
            }
            throw new ValidationException(label + ": choose " + choices(options) + ".");
        };
    }

    private static Checker yesNoCheck(String label) {
        return v -> {
            String text = v.trim();
            if (text.equals("1") || text.equalsIgnoreCase("YES") || text.equalsIgnoreCase("Y")) {
                return Boolean.TRUE;
            }
            if (text.equals("2") || text.equalsIgnoreCase("NO") || text.equalsIgnoreCase("N")) {
                return Boolean.FALSE;
            }
            throw new ValidationException(label + ": enter 1 for YES or 2 for NO.");
        };
    }

    // ================================================================
    // 7. Checking for duplicates
    // ================================================================
    private static boolean idExists(List<? extends Identifiable> list, String id) {
        for (Identifiable item : list) {
            if (item.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    private static List<Person> allPeople() {
        List<Person> people = new ArrayList<>();
        people.addAll(students);
        people.addAll(teachers);
        people.addAll(administrators);
        return people;
    }

    private static List<AcademicMember> allAcademicMembers() {
        List<AcademicMember> members = new ArrayList<>();
        members.addAll(students);
        members.addAll(teachers);
        return members;
    }

    private static boolean personIdExists(String id) {
        for (Person p : allPeople()) {
            if (p.getPersonId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    private static boolean personEmailExists(String email) {
        for (Person p : allPeople()) {
            if (p.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    private static boolean memberNumberExists(String number) {
        for (AcademicMember m : allAcademicMembers()) {
            if (m.getMemberNumber().equalsIgnoreCase(number)) {
                return true;
            }
        }
        return false;
    }

    private static boolean instEmailExists(String email) {
        for (AcademicMember m : allAcademicMembers()) {
            if (m.getInstitutionalEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    private static boolean departmentCodeExists(String code) {
        for (Department d : departments) {
            if (d.getDepartmentCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    private static boolean courseCodeExists(String code) {
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    private static boolean receiptExists(String receipt) {
        for (FeePayment p : feePayments) {
            if (p.getReceiptNumber().equalsIgnoreCase(receipt)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isbnExists(String isbn) {
        for (LibraryBook b : libraryBooks) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                return true;
            }
        }
        return false;
    }

    // Same student + same course + same semester, both ACTIVE ("ignore" = the record being edited)
    private static boolean activeEnrollmentExists(Student student, Course course, String semester, Enrollment ignore) {
        for (Enrollment e : enrollments) {
            if (e != ignore && e.isActive() && e.getStudent() == student && e.getCourse() == course
                    && e.getSemester().equalsIgnoreCase(semester)) {
                return true;
            }
        }
        return false;
    }

    private static void mustBeNew(boolean exists, String label, String value) throws DuplicateRecordException {
        if (exists) {
            throw new DuplicateRecordException(label + " " + value + " already exists.");
        }
    }

    // Last check done at Save time (the same rules are also checked when each field is typed)
    private static void checkDuplicates(Object r) throws DuplicateRecordException {
        if (r instanceof Person) {
            Person p = (Person) r;
            mustBeNew(personIdExists(p.getPersonId()), "Person ID", p.getPersonId());
            mustBeNew(personEmailExists(p.getEmail()), "Email", p.getEmail());
        }
        if (r instanceof AcademicMember) {
            AcademicMember m = (AcademicMember) r;
            mustBeNew(memberNumberExists(m.getMemberNumber()), "Member number", m.getMemberNumber());
            mustBeNew(instEmailExists(m.getInstitutionalEmail()), "Institutional email", m.getInstitutionalEmail());
        }
        if (r instanceof Student) {
            String number = ((Student) r).getStudentNumber();
            mustBeNew(idExists(students, number), "Student number", number);
        }
        if (r instanceof Teacher) {
            String number = ((Teacher) r).getEmployeeNumber();
            mustBeNew(idExists(teachers, number), "Employee number", number);
        }
        if (r instanceof Administrator) {
            String number = ((Administrator) r).getStaffNumber();
            mustBeNew(idExists(administrators, number), "Staff number", number);
        }
        if (r instanceof Department) {
            Department d = (Department) r;
            mustBeNew(idExists(departments, d.getId()), "Department ID", d.getId());
            mustBeNew(departmentCodeExists(d.getDepartmentCode()), "Department code", d.getDepartmentCode());
        }
        if (r instanceof Course) {
            Course c = (Course) r;
            mustBeNew(idExists(courses, c.getId()), "Course ID", c.getId());
            mustBeNew(courseCodeExists(c.getCourseCode()), "Course code", c.getCourseCode());
        }
        if (r instanceof Classroom) {
            String id = ((Classroom) r).getId();
            mustBeNew(idExists(classrooms, id), "Classroom ID", id);
        }
        if (r instanceof Enrollment) {
            Enrollment e = (Enrollment) r;
            mustBeNew(idExists(enrollments, e.getId()), "Enrollment ID", e.getId());
            if (e.isActive() && activeEnrollmentExists(e.getStudent(), e.getCourse(), e.getSemester(), null)) {
                throw new DuplicateRecordException(e.getStudent().getDisplayName() + " already has an active enrollment in "
                        + e.getCourse().getCourseTitle() + " for " + e.getSemester() + ".");
            }
        }
        if (r instanceof Assessment) {
            String id = ((Assessment) r).getId();
            mustBeNew(idExists(assessments, id), "Assessment ID", id);
        }
        if (r instanceof FeePayment) {
            FeePayment p = (FeePayment) r;
            mustBeNew(idExists(feePayments, p.getId()), "Payment ID", p.getId());
            mustBeNew(receiptExists(p.getReceiptNumber()), "Receipt number", p.getReceiptNumber());
        }
        if (r instanceof LibraryBook) {
            LibraryBook b = (LibraryBook) r;
            mustBeNew(idExists(libraryBooks, b.getId()), "Book ID", b.getId());
            mustBeNew(isbnExists(b.getIsbn()), "ISBN", b.getIsbn());
        }
        if (r instanceof BookLoan) {
            String id = ((BookLoan) r).getId();
            mustBeNew(idExists(bookLoans, id), "Loan ID", id);
        }
    }

    // ================================================================
    // 8. Selecting existing records (with "register it now" when it is missing)
    // ================================================================

    // Shows a numbered list. If the list is empty the user can register the missing record
    // right now; the original form then continues with all its values intact.
    private static <T extends Identifiable> T chooseFrom(String name, Supplier<List<T>> source,
            Runnable registerNew, String purpose) {

        while (true) {
            List<T> list = source.get(); // read again each time, a new record may have been added

            System.out.println("\n+----------------- CHOOSE " + name.toUpperCase() + " -----------------+");

            if (list.isEmpty()) {
                System.out.println("| No " + name + " is registered yet.");
                System.out.println("| 1  Register a new " + name + " now");
                System.out.println("| 0  Cancel");
                int choice = readChoice("Select option: ", 0, 1);
                if (choice == 0) {
                    throw new PrerequisiteMissingException(
                            "No " + name + " exists. Register a " + name + " before " + purpose + ".");
                }
                runNested(registerNew);

            } else {
                for (int i = 0; i < list.size(); i++) {
                    System.out.println("| " + (i + 1) + "  [" + list.get(i).getId() + "] " + list.get(i).getDisplayName());
                }
                int plus = list.size() + 1;
                System.out.println("| " + plus + "  [+] Register a new " + name);
                System.out.println("| 0  Cancel");
                int choice = readChoice("Select option: ", 0, plus);
                if (choice == 0) {
                    throw new CancelException();
                }
                if (choice == plus) {
                    runNested(registerNew);
                } else {
                    return list.get(choice - 1);
                }
            }
        }
    }

    // Runs a registration inside another form. If the user cancels it, we just come back.
    private static void runNested(Runnable registerNew) {
        try {
            registerNew.run();
        } catch (CancelException e) {
            System.out.println("\nRegistration cancelled. Going back to the previous form...");
        } catch (PrerequisiteMissingException e) {
            System.out.println("\n! " + e.getMessage());
            System.out.println("Going back to the previous form...");
        }
    }

    // Plain numbered list (no "register new" option)
    private static <T extends Identifiable> T pickOne(String title, List<T> list) {
        System.out.println("\n+----------------- " + title + " -----------------+");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("| " + (i + 1) + "  [" + list.get(i).getId() + "] " + list.get(i).getDisplayName());
        }
        System.out.println("| 0  Cancel");
        int choice = readChoice("Select option: ", 0, list.size());
        if (choice == 0) {
            throw new CancelException();
        }
        return list.get(choice - 1);
    }

    private static Department selectDepartment(String purpose) {
        return chooseFrom("Department", () -> departments, Main::registerDepartment, purpose);
    }

    private static Teacher selectTeacher(String purpose) {
        return chooseFrom("Teacher", () -> teachers, Main::registerTeacher, purpose);
    }

    private static Student selectStudent(String purpose) {
        return chooseFrom("Student", () -> students, Main::registerStudent, purpose);
    }

    private static Course selectCourse(String purpose) {
        return chooseFrom("Course", () -> courses, Main::registerCourse, purpose);
    }

    private static Enrollment selectActiveEnrollment(String purpose) {
        return chooseFrom("Enrollment", () -> activeEnrollments(), Main::registerEnrollment, purpose);
    }

    private static LibraryBook selectAvailableBook(String purpose) {
        return chooseFrom("Library Book", () -> availableBooks(), Main::registerLibraryBook, purpose);
    }

    // The head teacher is OPTIONAL: it can be skipped and assigned later with Update Record.
    private static Teacher chooseHeadTeacher() {
        if (teachers.isEmpty()) {
            System.out.println("  No teacher exists yet. You can assign a head teacher later (Update Existing Record).");
            return null;
        }
        System.out.println("\n+----------------- CHOOSE HEAD TEACHER -----------------+");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println("| " + (i + 1) + "  [" + teachers.get(i).getId() + "] " + teachers.get(i).getDisplayName());
        }
        System.out.println("| 0  No head teacher");
        int choice = readChoice("Select option: ", 0, teachers.size());
        return (choice == 0) ? null : teachers.get(choice - 1);
    }

    private static List<Enrollment> activeEnrollments() {
        List<Enrollment> list = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.isActive()) {
                list.add(e);
            }
        }
        return list;
    }

    private static List<LibraryBook> availableBooks() {
        List<LibraryBook> list = new ArrayList<>();
        for (LibraryBook b : libraryBooks) {
            if (b.getAvailableCopies() > 0) {
                list.add(b);
            }
        }
        return list;
    }

    private static List<BookLoan> activeLoans() {
        List<BookLoan> list = new ArrayList<>();
        for (BookLoan l : bookLoans) {
            if (l.isActive()) {
                list.add(l);
            }
        }
        return list;
    }

    // ================================================================
    // 9. Registration forms (menu options 1-11)
    //    Field numbers (0,1,2...) in the Saver match the order of the constructor.
    // ================================================================

    // The 6 fields every Person has (fields 01-06)
    private static void addPersonFields(List<Field> f) {
        String g = "PERSONAL DETAILS";
        f.add(new Field(g, "Person ID", "String | unique | 4-30 chars",
                uniqueText("Person ID", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, Main::personIdExists)));
        f.add(new Field(g, "First name", "String | 2-60 chars", textCheck("First name", 2, 60)));
        f.add(new Field(g, "Last name", "String | 2-60 chars", textCheck("Last name", 2, 60)));
        f.add(new Field(g, "Email", "String | unique | email", uniqueEmail("Email", Main::personEmailExists)));
        f.add(new Field(g, "Phone", "String | 10-15 digits, optional +", phoneCheck("Phone")));
        f.add(new Field(g, "Date of birth", "LocalDate | YYYY-MM-DD", pastDateCheck("Date of birth")));
    }

    // The 6 fields every AcademicMember (Student or Teacher) has (fields 07-12)
    private static void addAcademicFields(List<Field> f, String purpose) {
        String g = "ACADEMIC DETAILS";
        f.add(new Field(g, "Member number", "String | unique | 4-30",
                uniqueText("Member number", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, Main::memberNumberExists)));
        f.add(new Field(g, "Department", "Department | select", () -> selectDepartment(purpose)));
        f.add(new Field(g, "Join date", "LocalDate | YYYY-MM-DD", dateCheck("Join date")));
        f.add(new Field(g, "Academic status", choices(Registrable.academicStatuses()),
                choiceCheck("Academic status", Registrable.academicStatuses())));
        f.add(new Field(g, "Institutional email", "String | unique | email",
                uniqueEmail("Institutional email", Main::instEmailExists)));
        f.add(new Field(g, "Advisor name", "String | 2-120 chars", textCheck("Advisor name", 2, 120)));
    }

    // ---- Helpers to read answers back from the form ----
    private static String str(List<Field> f, int i) {
        return (String) f.get(i).value;
    }

    private static LocalDate date(List<Field> f, int i) {
        return (LocalDate) f.get(i).value;
    }

    private static int num(List<Field> f, int i) {
        return (Integer) f.get(i).value;
    }

    private static boolean flag(List<Field> f, int i) {
        return (Boolean) f.get(i).value;
    }

    // ---- 1. Department ----
    public static void registerDepartment() {
        List<Field> f = new ArrayList<>();
        String g1 = "BASIC DETAILS";
        String g2 = "LOCATION, CONTACT AND HEAD";

        f.add(new Field(g1, "Department ID", "String | unique | 4-30",
                uniqueText("Department ID", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, x -> idExists(departments, x))));
        f.add(new Field(g1, "Department name", "String | 2-60 chars", textCheck("Department name", 2, 60)));
        f.add(new Field(g1, "Department code", "String | unique | 2-20",
                uniqueText("Department code", 2, 20, Main::departmentCodeExists)));
        f.add(new Field(g2, "Office location", "String | 2-60 chars", textCheck("Office location", 2, 60)));
        f.add(new Field(g2, "Contact email", "String | email", emailCheck("Contact email")));
        f.add(new Field(g2, "Head teacher", "Teacher | optional", () -> chooseHeadTeacher()));

        runForm("DEPARTMENT REGISTRATION", f, () -> {
            Department d = new Department(str(f, 0), str(f, 1), str(f, 2), str(f, 3), str(f, 4),
                    (Teacher) f.get(5).value);
            d.validate();
            checkDuplicates(d);
            departments.add(d);
            return "Department " + d.getId() + " registered successfully.";
        });
    }

    // ---- 2. Student ----
    public static void registerStudent() {
        List<Field> f = new ArrayList<>();
        addPersonFields(f);
        addAcademicFields(f, "registering a student");

        String g = "STUDENT DETAILS";
        f.add(new Field(g, "Student number", "String | unique | 4-30",
                uniqueText("Student number", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, x -> idExists(students, x))));
        f.add(new Field(g, "Program name", "String | 2-120 chars", textCheck("Program name", 2, 120)));
        f.add(new Field(g, "Admission date", "LocalDate | YYYY-MM-DD", dateCheck("Admission date")));
        f.add(new Field(g, "Study year", "int | 1-6", intCheck("Study year", 1, 6)));
        f.add(new Field(g, "Guardian phone", "String | 10-15 digits", phoneCheck("Guardian phone")));
        f.add(new Field(g, "Scholarship", "1 YES / 2 NO", yesNoCheck("Scholarship")));

        runForm("STUDENT REGISTRATION", f, () -> {
            Student s = new Student(str(f, 0), str(f, 1), str(f, 2), str(f, 3), str(f, 4), date(f, 5),
                    str(f, 6), (Department) f.get(7).value, date(f, 8), str(f, 9), str(f, 10), str(f, 11),
                    str(f, 12), str(f, 13), date(f, 14), num(f, 15), str(f, 16), flag(f, 17));
            s.validate();
            checkDuplicates(s);
            students.add(s);
            return "Student " + s.getStudentNumber() + " registered successfully.";
        });
    }

    // ---- 3. Teacher ----
    public static void registerTeacher() {
        List<Field> f = new ArrayList<>();
        addPersonFields(f);
        addAcademicFields(f, "registering a teacher");

        String g = "TEACHER DETAILS";
        f.add(new Field(g, "Employee number", "String | unique | 4-30",
                uniqueText("Employee number", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, x -> idExists(teachers, x))));
        f.add(new Field(g, "Specialization", "String | 2-120 chars", textCheck("Specialization", 2, 120)));
        f.add(new Field(g, "Hire date", "LocalDate | YYYY-MM-DD", dateCheck("Hire date")));
        f.add(new Field(g, "Salary", "BigDecimal | greater than 0", moneyCheck("Salary")));
        f.add(new Field(g, "Office number", "String | 1-30 chars", textCheck("Office number", 1, 30)));
        f.add(new Field(g, "Employment type", choices(Registrable.employmentTypes()),
                choiceCheck("Employment type", Registrable.employmentTypes())));

        runForm("TEACHER REGISTRATION", f, () -> {
            Teacher t = new Teacher(str(f, 0), str(f, 1), str(f, 2), str(f, 3), str(f, 4), date(f, 5),
                    str(f, 6), (Department) f.get(7).value, date(f, 8), str(f, 9), str(f, 10), str(f, 11),
                    str(f, 12), str(f, 13), date(f, 14), (BigDecimal) f.get(15).value, str(f, 16), str(f, 17));
            t.validate();
            checkDuplicates(t);
            teachers.add(t);
            return "Teacher " + t.getEmployeeNumber() + " registered successfully.";
        });
    }

    // ---- 4. Administrator ----
    public static void registerAdministrator() {
        List<Field> f = new ArrayList<>();
        addPersonFields(f);

        String g = "ADMINISTRATOR DETAILS";
        f.add(new Field(g, "Staff number", "String | unique | 4-30",
                uniqueText("Staff number", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, x -> idExists(administrators, x))));
        f.add(new Field(g, "Job title", "String | 2-120 chars", textCheck("Job title", 2, 120)));
        f.add(new Field(g, "Assigned office", "String | 1-60 chars", textCheck("Assigned office", 1, 60)));
        f.add(new Field(g, "Employment date", "LocalDate | YYYY-MM-DD", dateCheck("Employment date")));
        f.add(new Field(g, "Access level", "int | 1-5", intCheck("Access level", 1, 5)));
        f.add(new Field(g, "Active", "1 YES / 2 NO", yesNoCheck("Active")));

        runForm("ADMINISTRATOR REGISTRATION", f, () -> {
            Administrator a = new Administrator(str(f, 0), str(f, 1), str(f, 2), str(f, 3), str(f, 4), date(f, 5),
                    str(f, 6), str(f, 7), str(f, 8), date(f, 9), num(f, 10), flag(f, 11));
            a.validate();
            checkDuplicates(a);
            administrators.add(a);
            return "Administrator " + a.getStaffNumber() + " registered successfully.";
        });
    }

    // ---- 5. Course (needs a Department and a Teacher) ----
    public static void registerCourse() {
        List<Field> f = new ArrayList<>();
        String g1 = "COURSE DETAILS";
        String g2 = "DEPARTMENT AND TEACHER";

        f.add(new Field(g1, "Course ID", "String | unique | 4-30",
                uniqueText("Course ID", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, x -> idExists(courses, x))));
        f.add(new Field(g1, "Course code", "String | unique | 2-20", uniqueText("Course code", 2, 20, Main::courseCodeExists)));
        f.add(new Field(g1, "Course title", "String | 2-120 chars", textCheck("Course title", 2, 120)));
        f.add(new Field(g1, "Credit hours", "int | 1-6", intCheck("Credit hours", 1, 6)));
        f.add(new Field(g2, "Department", "Department | select", () -> selectDepartment("registering a course")));
        f.add(new Field(g2, "Assigned teacher", "Teacher | select", () -> selectTeacher("registering a course")));

        runForm("COURSE REGISTRATION", f, () -> {
            Course c = new Course(str(f, 0), str(f, 1), str(f, 2), num(f, 3), (Department) f.get(4).value,
                    (Teacher) f.get(5).value);
            c.validate();
            checkDuplicates(c);
            courses.add(c);
            return "Course " + c.getCourseId() + " registered successfully.";
        });
    }

    // ---- 6. Classroom ----
    public static void registerClassroom() {
        List<Field> f = new ArrayList<>();
        String g1 = "CLASSROOM DETAILS";
        String g2 = "CAPACITY AND STATUS";

        f.add(new Field(g1, "Classroom ID", "String | unique | 4-30",
                uniqueText("Classroom ID", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, x -> idExists(classrooms, x))));
        f.add(new Field(g1, "Building name", "String | 2-60 chars", textCheck("Building name", 2, 60)));
        f.add(new Field(g1, "Room number", "String | 1-20 chars", textCheck("Room number", 1, 20)));
        f.add(new Field(g2, "Capacity", "int | greater than 0", intCheck("Capacity", 1, 10000)));
        f.add(new Field(g2, "Floor number", "int | 0 or more", intCheck("Floor number", 0, 200)));
        f.add(new Field(g2, "Available", "1 YES / 2 NO", yesNoCheck("Available")));

        runForm("CLASSROOM REGISTRATION", f, () -> {
            Classroom c = new Classroom(str(f, 0), str(f, 1), str(f, 2), num(f, 3), num(f, 4), flag(f, 5));
            c.validate();
            checkDuplicates(c);
            classrooms.add(c);
            return "Classroom " + c.getClassroomId() + " registered successfully.";
        });
    }

    // ---- 7. Enrollment (needs a Student and a Course) ----
    public static void registerEnrollment() {
        List<Field> f = new ArrayList<>();
        String g1 = "ENROLLMENT DETAILS";
        String g2 = "TERM AND STATUS";

        // Active duplicate check for the same student + course + semester
        Checker statusCheck = v -> {
            String status = (String) choiceCheck("Enrollment status", Registrable.enrollmentStatuses()).check(v);
            Student s = (Student) f.get(1).value;
            Course c = (Course) f.get(2).value;
            String semester = (String) f.get(4).value;
            if (status.equals("ACTIVE") && activeEnrollmentExists(s, c, semester, null)) {
                throw new DuplicateRecordException(s.getDisplayName() + " already has an active enrollment in "
                        + c.getCourseTitle() + " for " + semester + ". Type BACK to change the semester.");
            }
            return status;
        };

        f.add(new Field(g1, "Enrollment ID", "String | unique | 4-30",
                uniqueText("Enrollment ID", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, x -> idExists(enrollments, x))));
        f.add(new Field(g1, "Student", "Student | select", () -> selectStudent("enrolling a student")));
        f.add(new Field(g1, "Course", "Course | select", () -> selectCourse("enrolling a student")));
        f.add(new Field(g2, "Enrollment date", "LocalDate | YYYY-MM-DD", dateCheck("Enrollment date")));
        f.add(new Field(g2, "Semester", choices(Registrable.semesters()), choiceCheck("Semester", Registrable.semesters())));
        f.add(new Field(g2, "Enrollment status", choices(Registrable.enrollmentStatuses()), statusCheck));

        runForm("ENROLL STUDENT", f, () -> {
            Enrollment e = new Enrollment(str(f, 0), (Student) f.get(1).value, (Course) f.get(2).value, date(f, 3),
                    str(f, 4), str(f, 5));
            e.validate();
            checkDuplicates(e);
            enrollments.add(e);
            return "Enrollment " + e.getEnrollmentId() + " saved successfully.";
        });
    }

    // ---- 8. Assessment (needs an ACTIVE Enrollment) ----
    public static void registerAssessment() {
        List<Field> f = new ArrayList<>();
        String g1 = "ASSESSMENT DETAILS";
        String g2 = "SCORES AND DATE";

        Checker obtainedCheck = v -> {
            double obtained = Registrable.parseDouble(v, "Obtained score");
            double max = (Double) f.get(3).value;
            if (obtained < 0 || obtained > max) {
                throw new ValidationException("Obtained score must be between 0 and " + max + ".");
            }
            return obtained;
        };

        f.add(new Field(g1, "Assessment ID", "String | unique | 4-30",
                uniqueText("Assessment ID", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, x -> idExists(assessments, x))));
        f.add(new Field(g1, "Enrollment", "Active Enrollment | select", () -> selectActiveEnrollment("recording an assessment")));
        f.add(new Field(g1, "Assessment type", choices(Registrable.assessmentTypes()),
                choiceCheck("Assessment type", Registrable.assessmentTypes())));
        f.add(new Field(g2, "Maximum score", "double | greater than 0", positiveDoubleCheck("Maximum score")));
        f.add(new Field(g2, "Obtained score", "double | 0 to maximum", obtainedCheck));
        f.add(new Field(g2, "Assessment date", "LocalDate | YYYY-MM-DD", dateCheck("Assessment date")));

        runForm("RECORD ASSESSMENT", f, () -> {
            Enrollment enrollment = (Enrollment) f.get(1).value;
            if (!enrollment.isActive()) {
                throw new ValidationException("Only an active enrollment can be assessed.");
            }
            Assessment a = new Assessment(str(f, 0), enrollment, str(f, 2), (Double) f.get(3).value,
                    (Double) f.get(4).value, date(f, 5));
            a.validate();
            checkDuplicates(a);
            assessments.add(a);
            return "Assessment " + a.getAssessmentId() + " recorded successfully.";
        });
    }

    // ---- 9. Fee payment (needs a Student) ----
    public static void registerFeePayment() {
        List<Field> f = new ArrayList<>();
        String g1 = "PAYMENT DETAILS";
        String g2 = "DATE, METHOD AND RECEIPT";

        f.add(new Field(g1, "Payment ID", "String | unique | 4-30",
                uniqueText("Payment ID", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, x -> idExists(feePayments, x))));
        f.add(new Field(g1, "Student", "Student | select", () -> selectStudent("recording a payment")));
        f.add(new Field(g1, "Amount", "BigDecimal | greater than 0", moneyCheck("Amount")));
        f.add(new Field(g2, "Payment date", "LocalDate | YYYY-MM-DD", dateCheck("Payment date")));
        f.add(new Field(g2, "Payment method", choices(Registrable.paymentMethods()),
                choiceCheck("Payment method", Registrable.paymentMethods())));
        f.add(new Field(g2, "Receipt number", "String | unique | 1-30", uniqueText("Receipt number", 1, 30, Main::receiptExists)));

        runForm("RECORD FEE PAYMENT", f, () -> {
            FeePayment p = new FeePayment(str(f, 0), (Student) f.get(1).value, (BigDecimal) f.get(2).value,
                    date(f, 3), str(f, 4), str(f, 5));
            p.validate();
            checkDuplicates(p);
            feePayments.add(p);
            return "Payment " + p.getPaymentId() + " recorded successfully.";
        });
    }

    // ---- 10. Library book ----
    public static void registerLibraryBook() {
        List<Field> f = new ArrayList<>();
        String g1 = "BOOK DETAILS";
        String g2 = "AUTHOR AND STOCK";

        Checker availableCheck = v -> {
            int available = Registrable.parseInt(v, "Available copies");
            int total = (Integer) f.get(4).value;
            Registrable.requireRange(available, "Available copies", 0, total);
            return available;
        };

        f.add(new Field(g1, "Book ID", "String | unique | 4-30",
                uniqueText("Book ID", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, x -> idExists(libraryBooks, x))));
        f.add(new Field(g1, "ISBN", "String | unique | 10-17", uniqueText("ISBN", 10, 17, Main::isbnExists)));
        f.add(new Field(g1, "Title", "String | 2-120 chars", textCheck("Title", 2, 120)));
        f.add(new Field(g2, "Author", "String | 2-120 chars", textCheck("Author", 2, 120)));
        f.add(new Field(g2, "Total copies", "int | 0 or more", intCheck("Total copies", 0, 100000)));
        f.add(new Field(g2, "Available copies", "int | 0 to total copies", availableCheck));

        runForm("LIBRARY BOOK REGISTRATION", f, () -> {
            LibraryBook b = new LibraryBook(str(f, 0), str(f, 1), str(f, 2), str(f, 3), num(f, 4), num(f, 5));
            b.validate();
            checkDuplicates(b);
            libraryBooks.add(b);
            return "Book " + b.getBookId() + " registered successfully.";
        });
    }

    // ---- 11. Borrow a book (creates a BookLoan) ----
    public static void borrowBook() throws BookUnavailableException {
        if (!libraryBooks.isEmpty() && availableBooks().isEmpty()) {
            throw new BookUnavailableException("No book has an available copy right now. Try again after a book is returned.");
        }

        List<Field> f = new ArrayList<>();
        String g1 = "LOAN DETAILS";
        String g2 = "DATES";

        Checker dueCheck = v -> {
            LocalDate due = Registrable.parseDate(v, "Due date");
            LocalDate borrowed = (LocalDate) f.get(3).value;
            if (!due.isAfter(borrowed)) {
                throw new ValidationException("Due date must be after the borrowed date (" + borrowed + ").");
            }
            return due;
        };

        f.add(new Field(g1, "Loan ID", "String | unique | 4-30",
                uniqueText("Loan ID", Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH, x -> idExists(bookLoans, x))));
        f.add(new Field(g1, "Student", "Student | select", () -> selectStudent("borrowing a book")));
        f.add(new Field(g1, "Library book", "Available book | select", () -> selectAvailableBook("borrowing a book")));
        f.add(new Field(g2, "Borrowed date", "LocalDate | YYYY-MM-DD", dateCheck("Borrowed date")));
        f.add(new Field(g2, "Due date", "LocalDate | after borrowed date", dueCheck));
        f.add(new Field(g2, "Returned date", "set automatically when returned", () -> null));

        runForm("BORROW LIBRARY BOOK", f, () -> {
            LibraryBook book = (LibraryBook) f.get(2).value;
            BookLoan loan = new BookLoan(str(f, 0), (Student) f.get(1).value, book, date(f, 3), date(f, 4), null);
            loan.validate();
            checkDuplicates(loan);
            if (book.getAvailableCopies() <= 0) {
                throw new BookUnavailableException("No copy of '" + book.getTitle() + "' is available.");
            }
            // Everything is correct: save the loan and take ONE copy from the stock
            bookLoans.add(loan);
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            return "Loan " + loan.getLoanId() + " saved. Copies left of '" + book.getTitle() + "': "
                    + book.getAvailableCopies();
        });
    }
    // 10. Return a book (updates the existing BookLoan, no new loan)
    public static void returnBook() {
        System.out.println("\n===== RETURN LIBRARY BOOK =====");
        List<BookLoan> open = activeLoans();
        if (open.isEmpty()) {
            System.out.println("No borrowed book is waiting to be returned.");
            return;
        }

        BookLoan loan = pickOne("CHOOSE THE LOAN TO RETURN", open);
        System.out.println(loan.getRegistrationSummary());

        LocalDate returned = (LocalDate) askValue("Return date", "LocalDate | YYYY-MM-DD", v -> {
            LocalDate d = Registrable.parseDate(v, "Return date");
            Registrable.requireNotFuture(d, "Return date");
            if (d.isBefore(loan.getBorrowedDate())) {
                throw new ValidationException("Return date cannot be before the borrowed date (" + loan.getBorrowedDate() + ").");
            }
            return d;
        });

        if (readChoice("Confirm the return? [1] Yes  [0] No: ", 0, 1) == 0) {
            throw new CancelException();
        }
        // Change the loan and add ONE copy back to the stock
        loan.setReturnedDate(returned);
        LibraryBook book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        System.out.println("\n+ Book '" + book.getTitle() + "' returned. Copies available now: "
                + book.getAvailableCopies() + "/" + book.getTotalCopies());
    }
    // 11. View, search and update
    private static <T extends Identifiable & Registrable> void printList(String title, List<T> list) {
        System.out.println("\n--- " + title + " (" + list.size() + ") ---");
        if (list.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        int number = 1;
        for (T item : list) {
            System.out.println(number + ". " + item.getRegistrationSummary());
            number++;
        }
    }

    public static void viewAllRecords() {
        System.out.println("\n===== VIEW ALL RECORDS =====");
        String[] names = { "Departments", "Students", "Teachers", "Administrators", "Courses", "Classrooms",
                "Enrollments", "Assessments", "Fee payments", "Library books", "Book loans" };
        for (int i = 0; i < names.length; i++) {
            System.out.println((i + 1) + ". " + names[i]);
        }
        System.out.println("12. Everything");
        System.out.println("0. Back to main menu");

        int c = readChoice("Select option: ", 0, 12);
        if (c == 0) {
            return;
        }
        if (c == 1 || c == 12) printList("DEPARTMENTS", departments);
        if (c == 2 || c == 12) printList("STUDENTS", students);
        if (c == 3 || c == 12) printList("TEACHERS", teachers);
        if (c == 4 || c == 12) printList("ADMINISTRATORS", administrators);
        if (c == 5 || c == 12) printList("COURSES", courses);
        if (c == 6 || c == 12) printList("CLASSROOMS", classrooms);
        if (c == 7 || c == 12) printList("ENROLLMENTS", enrollments);
        if (c == 8 || c == 12) printList("ASSESSMENTS", assessments);
        if (c == 9 || c == 12) printList("FEE PAYMENTS", feePayments);
        if (c == 10 || c == 12) printList("LIBRARY BOOKS", libraryBooks);
        if (c == 11 || c == 12) printList("BOOK LOANS", bookLoans);
    }

    // Every record of every type, seen as Identifiable
    private static List<Identifiable> allRecords() {
        List<Identifiable> all = new ArrayList<>();
        all.addAll(departments);
        all.addAll(students);
        all.addAll(teachers);
        all.addAll(administrators);
        all.addAll(courses);
        all.addAll(classrooms);
        all.addAll(enrollments);
        all.addAll(assessments);
        all.addAll(feePayments);
        all.addAll(libraryBooks);
        all.addAll(bookLoans);
        return all;
    }

    private static List<Identifiable> findAll(String id) {
        List<Identifiable> found = new ArrayList<>();
        for (Identifiable record : allRecords()) {
            boolean sameId = record.getId().equalsIgnoreCase(id);
            boolean samePersonId = (record instanceof Person) && ((Person) record).getPersonId().equalsIgnoreCase(id);
            if (sameId || samePersonId) {
                found.add(record);
            }
        }
        return found;
    }

    public static void searchById() throws RecordNotFoundException {
        System.out.println("\n===== SEARCH RECORD BY ID =====");
        String id = (String) askValue("ID to search", "String | type CANCEL to go back", textCheck("ID", 1, 60));

        List<Identifiable> found = findAll(id);
        if (found.isEmpty()) {
            throw new RecordNotFoundException("No record with ID '" + id + "' was found.");
        }
        for (Identifiable record : found) {
            System.out.println("\n--- " + record.getClass().getSimpleName() + " ---");
            ((Registrable) record).printDetails();
        }
    }

    public static void updateRecord() throws RecordNotFoundException {
        System.out.println("\n===== UPDATE EXISTING RECORD =====");
        String id = (String) askValue("ID of the record to update", "String | type CANCEL to go back", textCheck("ID", 1, 60));

        List<Identifiable> found = findAll(id);
        if (found.isEmpty()) {
            throw new RecordNotFoundException("No record with ID '" + id + "' was found.");
        }
        Identifiable record = (found.size() == 1) ? found.get(0) : pickOne("SEVERAL RECORDS MATCH", found);

        runUpdate(record, buildEdits(record));
    }

    // Shows the editable fields, asks for the new value, validates and confirms before saving
    private static void runUpdate(Identifiable record, List<Edit> edits) {
        while (true) {
            System.out.println("\n--- UPDATE " + record.getClass().getSimpleName().toUpperCase() + " "
                    + record.getId() + " (the ID cannot be changed) ---");
            for (int i = 0; i < edits.size(); i++) {
                System.out.println((i + 1) + ". " + edits.get(i).label + " : " + show(edits.get(i).getter.get()));
            }
            System.out.println("0. Done");

            int choice = readChoice("Field to edit: ", 0, edits.size());
            if (choice == 0) {
                return;
            }

            Edit edit = edits.get(choice - 1);
            Object newValue;
            if (edit.selector != null) {
                newValue = edit.selector.select();
            } else {
                newValue = askValue(edit.label, edit.hint, edit.checker);
            }

            Object oldValue = edit.getter.get();
            System.out.println("Old value: " + show(oldValue) + "   New value: " + show(newValue));
            if (readChoice("Confirm this change? [1] Yes  [0] No: ", 0, 1) == 0) {
                System.out.println("Change not applied.");
                continue;
            }

            edit.setter.set(newValue);
            try {
                ((Registrable) record).validate(); // check the whole record again
                System.out.println("+ " + edit.label + " updated successfully.");
            } catch (ValidationException e) {
                edit.setter.set(oldValue); // undo the change
                System.out.println("! Change undone: " + e.getMessage());
            }
        }
    }

    // The mutable fields of each type (IDs and unique codes are not editable)
    private static List<Edit> buildEdits(Identifiable record) {
        List<Edit> edits = new ArrayList<>();

        if (record instanceof Department) {
            Department d = (Department) record;
            edits.add(new Edit("Department name", "String | 2-60 chars", textCheck("Department name", 2, 60),
                    () -> d.getDepartmentName(), v -> d.setDepartmentName((String) v)));
            edits.add(new Edit("Office location", "String | 2-60 chars", textCheck("Office location", 2, 60),
                    () -> d.getOfficeLocation(), v -> d.setOfficeLocation((String) v)));
            edits.add(new Edit("Contact email", "String | email", emailCheck("Contact email"),
                    () -> d.getContactEmail(), v -> d.setContactEmail((String) v)));
            edits.add(new Edit("Head teacher", "Teacher | select", () -> chooseHeadTeacher(),
                    () -> d.getHeadTeacher(), v -> d.setHeadTeacher((Teacher) v)));

        } else if (record instanceof Student) {
            Student s = (Student) record;
            edits.add(new Edit("Program name", "String | 2-120 chars", textCheck("Program name", 2, 120),
                    () -> s.getProgramName(), v -> s.setProgramName((String) v)));
            edits.add(new Edit("Study year", "int | 1-6", intCheck("Study year", 1, 6),
                    () -> s.getStudyYear(), v -> s.setStudyYear((Integer) v)));
            edits.add(new Edit("Guardian phone", "String | 10-15 digits", phoneCheck("Guardian phone"),
                    () -> s.getGuardianPhone(), v -> s.setGuardianPhone((String) v)));
            edits.add(new Edit("Scholarship", "1 YES / 2 NO", yesNoCheck("Scholarship"),
                    () -> s.isScholarshipStatus(), v -> s.setScholarshipStatus((Boolean) v)));
            edits.add(new Edit("Academic status", choices(Registrable.academicStatuses()),
                    choiceCheck("Academic status", Registrable.academicStatuses()),
                    () -> s.getAcademicStatus(), v -> s.setAcademicStatus((String) v)));
            edits.add(new Edit("Advisor name", "String | 2-120 chars", textCheck("Advisor name", 2, 120),
                    () -> s.getAdvisorName(), v -> s.setAdvisorName((String) v)));

        } else if (record instanceof Teacher) {
            Teacher t = (Teacher) record;
            edits.add(new Edit("Specialization", "String | 2-120 chars", textCheck("Specialization", 2, 120),
                    () -> t.getSpecialization(), v -> t.setSpecialization((String) v)));
            edits.add(new Edit("Salary", "BigDecimal | greater than 0", moneyCheck("Salary"),
                    () -> t.getSalary(), v -> t.setSalary((BigDecimal) v)));
            edits.add(new Edit("Office number", "String | 1-30 chars", textCheck("Office number", 1, 30),
                    () -> t.getOfficeNumber(), v -> t.setOfficeNumber((String) v)));
            edits.add(new Edit("Employment type", choices(Registrable.employmentTypes()),
                    choiceCheck("Employment type", Registrable.employmentTypes()),
                    () -> t.getEmploymentType(), v -> t.setEmploymentType((String) v)));
            edits.add(new Edit("Academic status", choices(Registrable.academicStatuses()),
                    choiceCheck("Academic status", Registrable.academicStatuses()),
                    () -> t.getAcademicStatus(), v -> t.setAcademicStatus((String) v)));
            edits.add(new Edit("Advisor name", "String | 2-120 chars", textCheck("Advisor name", 2, 120),
                    () -> t.getAdvisorName(), v -> t.setAdvisorName((String) v)));

        } else if (record instanceof Administrator) {
            Administrator a = (Administrator) record;
            edits.add(new Edit("Job title", "String | 2-120 chars", textCheck("Job title", 2, 120),
                    () -> a.getJobTitle(), v -> a.setJobTitle((String) v)));
            edits.add(new Edit("Assigned office", "String | 1-60 chars", textCheck("Assigned office", 1, 60),
                    () -> a.getAssignedOffice(), v -> a.setAssignedOffice((String) v)));
            edits.add(new Edit("Access level", "int | 1-5", intCheck("Access level", 1, 5),
                    () -> a.getAccessLevel(), v -> a.setAccessLevel((Integer) v)));
            edits.add(new Edit("Active", "1 YES / 2 NO", yesNoCheck("Active"),
                    () -> a.isActive(), v -> a.setActive((Boolean) v)));

        } else if (record instanceof Course) {
            Course c = (Course) record;
            edits.add(new Edit("Course title", "String | 2-120 chars", textCheck("Course title", 2, 120),
                    () -> c.getCourseTitle(), v -> c.setCourseTitle((String) v)));
            edits.add(new Edit("Credit hours", "int | 1-6", intCheck("Credit hours", 1, 6),
                    () -> c.getCreditHours(), v -> c.setCreditHours((Integer) v)));
            edits.add(new Edit("Assigned teacher", "Teacher | select", () -> selectTeacher("changing the course teacher"),
                    () -> c.getAssignedTeacher(), v -> c.setAssignedTeacher((Teacher) v)));

        } else if (record instanceof Classroom) {
            Classroom r = (Classroom) record;
            edits.add(new Edit("Building name", "String | 2-60 chars", textCheck("Building name", 2, 60),
                    () -> r.getBuildingName(), v -> r.setBuildingName((String) v)));
            edits.add(new Edit("Room number", "String | 1-20 chars", textCheck("Room number", 1, 20),
                    () -> r.getRoomNumber(), v -> r.setRoomNumber((String) v)));
            edits.add(new Edit("Capacity", "int | greater than 0", intCheck("Capacity", 1, 10000),
                    () -> r.getCapacity(), v -> r.setCapacity((Integer) v)));
            edits.add(new Edit("Floor number", "int | 0 or more", intCheck("Floor number", 0, 200),
                    () -> r.getFloorNumber(), v -> r.setFloorNumber((Integer) v)));
            edits.add(new Edit("Available", "1 YES / 2 NO", yesNoCheck("Available"),
                    () -> r.isAvailable(), v -> r.setAvailable((Boolean) v)));

        } else if (record instanceof Enrollment) {
            Enrollment e = (Enrollment) record;
            Checker statusCheck = v -> {
                String status = (String) choiceCheck("Enrollment status", Registrable.enrollmentStatuses()).check(v);
                if (status.equals("ACTIVE")
                        && activeEnrollmentExists(e.getStudent(), e.getCourse(), e.getSemester(), e)) {
                    throw new DuplicateRecordException("This student already has another active enrollment in this course and semester.");
                }
                return status;
            };
            edits.add(new Edit("Enrollment status", choices(Registrable.enrollmentStatuses()), statusCheck,
                    () -> e.getEnrollmentStatus(), v -> e.setEnrollmentStatus((String) v)));

        } else if (record instanceof Assessment) {
            Assessment a = (Assessment) record;
            Checker scoreCheck = v -> {
                double score = Registrable.parseDouble(v, "Obtained score");
                if (score < 0 || score > a.getMaximumScore()) {
                    throw new ValidationException("Obtained score must be between 0 and " + a.getMaximumScore() + ".");
                }
                return score;
            };
            edits.add(new Edit("Assessment type", choices(Registrable.assessmentTypes()),
                    choiceCheck("Assessment type", Registrable.assessmentTypes()),
                    () -> a.getAssessmentType(), v -> a.setAssessmentType((String) v)));
            edits.add(new Edit("Obtained score", "double | 0 to " + a.getMaximumScore(), scoreCheck,
                    () -> a.getObtainedScore(), v -> a.setObtainedScore((Double) v)));

        } else if (record instanceof FeePayment) {
            FeePayment p = (FeePayment) record;
            edits.add(new Edit("Payment method", choices(Registrable.paymentMethods()),
                    choiceCheck("Payment method", Registrable.paymentMethods()),
                    () -> p.getPaymentMethod(), v -> p.setPaymentMethod((String) v)));
            edits.add(new Edit("Amount", "BigDecimal | greater than 0", moneyCheck("Amount"),
                    () -> p.getAmount(), v -> p.setAmount((BigDecimal) v)));

        } else if (record instanceof LibraryBook) {
            LibraryBook b = (LibraryBook) record;
            Checker totalCheck = v -> {
                int total = Registrable.parseInt(v, "Total copies");
                int borrowed = b.getTotalCopies() - b.getAvailableCopies();
                if (total < borrowed) {
                    throw new ValidationException("Total copies cannot be less than the " + borrowed
                            + " copies currently borrowed.");
                }
                Registrable.requireRange(total, "Total copies", 0, 100000);
                return total;
            };
            edits.add(new Edit("Title", "String | 2-120 chars", textCheck("Title", 2, 120),
                    () -> b.getTitle(), v -> b.setTitle((String) v)));
            edits.add(new Edit("Author", "String | 2-120 chars", textCheck("Author", 2, 120),
                    () -> b.getAuthor(), v -> b.setAuthor((String) v)));
            // changing the total also changes the available copies by the same difference
            edits.add(new Edit("Total copies", "int | not less than borrowed copies", totalCheck,
                    () -> b.getTotalCopies(), v -> {
                        int newTotal = (Integer) v;
                        b.setAvailableCopies(b.getAvailableCopies() + newTotal - b.getTotalCopies());
                        b.setTotalCopies(newTotal);
                    }));

        } else if (record instanceof BookLoan) {
            BookLoan l = (BookLoan) record;
            Checker dueCheck = v -> {
                LocalDate due = Registrable.parseDate(v, "Due date");
                if (!due.isAfter(l.getBorrowedDate())) {
                    throw new ValidationException("Due date must be after the borrowed date (" + l.getBorrowedDate() + ").");
                }
                return due;
            };
            edits.add(new Edit("Due date", "LocalDate | after borrowed date", dueCheck,
                    () -> l.getDueDate(), v -> l.setDueDate((LocalDate) v)));
        }

        return edits;
    }
    // 12. Reports (menu options 16, 17, 18)
    public static void showAcademicSummary() {
        System.out.println("\n===== STUDENT ACADEMIC SUMMARY =====");
        if (students.isEmpty()) {
            throw new PrerequisiteMissingException("No student exists. Register a student first.");
        }
        Student student = pickOne("CHOOSE STUDENT", students);

        System.out.println();
        student.printDetails();

        System.out.println("\n--- Enrollments and assessments ---");
        boolean hasEnrollment = false;
        int activeCredits = 0;
        double percentTotal = 0;
        int assessmentCount = 0;

        for (Enrollment e : enrollments) {
            if (e.getStudent() != student) {
                continue;
            }
            hasEnrollment = true;
            System.out.println("* [" + e.getCourse().getCourseCode() + "] " + e.getCourse().getCourseTitle() + " | "
                    + e.getSemester() + " | " + e.getEnrollmentStatus() + " | " + e.getCourse().getCreditHours() + " credits");
            if (e.isActive()) {
                activeCredits += e.getCourse().getCreditHours();
            }
            for (Assessment a : assessments) {
                if (a.getEnrollment() == e) {
                    double percent = a.getObtainedScore() / a.getMaximumScore() * 100;
                    System.out.println("     - " + a.getAssessmentType() + ": " + a.getObtainedScore() + "/"
                            + a.getMaximumScore() + String.format(" (%.1f%%)", percent));
                    percentTotal += percent;
                    assessmentCount++;
                }
            }
        }

        if (!hasEnrollment) {
            System.out.println("This student has no enrollment yet.");
            return;
        }
        System.out.println("\nActive credit hours: " + activeCredits);
        if (assessmentCount == 0) {
            System.out.println("No assessment recorded yet.");
        } else {
            System.out.println(String.format("Average score: %.1f%% (%d assessments)", percentTotal / assessmentCount,
                    assessmentCount));
        }
    }

    public static void showPaymentHistory() {
        System.out.println("\n===== STUDENT PAYMENT HISTORY =====");
        if (students.isEmpty()) {
            throw new PrerequisiteMissingException("No student exists. Register a student first.");
        }
        Student student = pickOne("CHOOSE STUDENT", students);

        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        System.out.println("\nPayments of " + student.getDisplayName() + " [" + student.getStudentNumber() + "]:");
        for (FeePayment p : feePayments) {
            if (p.getStudent() == student) {
                System.out.println("* " + p.getPaymentDate() + " | " + p.getAmount() + " | " + p.getPaymentMethod()
                        + " | Receipt " + p.getReceiptNumber());
                total = total.add(p.getAmount());
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No payment recorded for this student.");
        } else {
            System.out.println("Total paid: " + total + " (" + count + " payments)");
        }
    }

    public static void showAvailableBooks() {
        System.out.println("\n===== AVAILABLE BOOKS =====");
        List<LibraryBook> available = availableBooks();
        if (available.isEmpty()) {
            System.out.println("No book has an available copy at the moment.");
            return;
        }
        for (LibraryBook b : available) {
            System.out.println("* [" + b.getBookId() + "] " + b.getTitle() + " by " + b.getAuthor()
                    + " | Available: " + b.getAvailableCopies() + "/" + b.getTotalCopies());
        }
    }
    // 13. Polymorphism demonstration (menu option 19)
    public static void displayPolymorphism() {
        System.out.println("\n===== PERSON POLYMORPHISM =====");
        List<Person> people = new ArrayList<>();
        people.addAll(students);
        people.addAll(teachers);
        people.addAll(administrators);

        if (people.isEmpty()) {
            System.out.println("No student, teacher or administrator registered yet.");
        }

        for (Person p : people) {
            System.out.println("ID: " + p.getId());
            System.out.println("Name: " + p.getDisplayName());
            System.out.println("Role: " + p.getRoleDescription());
            if (p instanceof AcademicMember) {
                System.out.println("Academic category: " + ((AcademicMember) p).getAcademicCategory());
            }
            System.out.println("Summary: " + p.getRegistrationSummary());
            System.out.println("Active: " + p.isActive());
            System.out.println("-----------------------");
        }
        System.out.println("\n===== IDENTIFIABLE POLYMORPHISM =====");

        List<Identifiable> records = new ArrayList<>();
        records.addAll(departments);
        records.addAll(courses);
        records.addAll(classrooms);
        records.addAll(libraryBooks);
        records.addAll(enrollments);
        records.addAll(assessments);
        records.addAll(feePayments);
        records.addAll(bookLoans);

        if (records.isEmpty()) {
            System.out.println("No department, course, classroom, book, enrollment, assessment, payment or loan yet.");
        }

        for (Identifiable item : records) {
            System.out.println("ID: " + item.getId());
            System.out.println("Name: " + item.getDisplayName());
            System.out.println("Active: " + item.isActive());
            System.out.println("-----------------------");
        }
    }
}

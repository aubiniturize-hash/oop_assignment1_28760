# AUCA_ID - School Management System (Java console project)

## How to compile and run
From the `AUCA_ID` folder:

    javac -d bin $(find src -name "*.java")      (Linux / macOS)
    java -cp bin com.auca.main.Main

On Windows (PowerShell):

    dir src -Recurse -Filter *.java | % { $_.FullName } > files.txt
    javac -d bin "@files.txt"
    java -cp bin com.auca.main.Main

In VS Code: open the `AUCA_ID` folder, keep sources in `src`, press Run on `Main.java`.

## What is inside (15 domain types + Main + exceptions)
- `com.auca.interfaces`   : Identifiable, Registrable (6 constants each + the required methods)
- `com.auca.abstracts`    : Person, AcademicMember (abstract)
- `com.auca.student / teacher / administrator / department / course / classroom /
   enrollment / assessment / feepayment / librarybook / bookloan` : the 11 concrete classes
- `com.auca.exceptions`   : ValidationException, DuplicateRecordException, RecordNotFoundException,
                            PrerequisiteMissingException, BookUnavailableException
- `com.auca.main.Main`    : menu, forms, search, update, reports, polymorphism demo

## How the forms work (Main.java)
1. A form is a list of `Field` objects (one per question).
2. `runForm(...)` asks them one by one. BACK = previous field, CANCEL = discard.
3. A field is either TYPED (a `Checker` validates the text) or SELECTED
   (a numbered list of existing records, with "[+] Register a new ..." inside the list).
4. At the end there is the review screen: Save / Edit field / Cancel.
5. Save runs the "Saver": create object -> `validate()` -> duplicate check -> add to the list.
   If it fails, the message is shown and nothing is stored.

## Things to explain during the physical demonstration
- Why Person and AcademicMember cannot be instantiated: they are `abstract` (they still have
  abstract methods: getRoleDescription() and getAcademicCategory()). Only children can be created.
- Multilevel inheritance: Person -> AcademicMember -> Student / Teacher.
  Hierarchical inheritance: Student and Teacher share AcademicMember; Administrator extends Person.
- `super(...)` and constructor chaining: Student -> AcademicMember -> Person constructors.
- Overriding + super: `validate()` and `printDetails()` call `super.validate()` / `super.printDetails()`.
- Design choice for getRoleDescription()/getAcademicCategory(): they are abstract methods in
  Person and AcademicMember (the "cleaner alternative" allowed in section 4 of the assignment),
  so non-person entities are not forced to implement them.
- Static helper methods in `Registrable` (requireText, parseDate, ...) are the shared validation
  rules, used by `validate()` in every class and by the forms in Main.
- Polymorphism: menu 19 (List<Person> and List<Identifiable>).
- Borrow (menu 11) decreases availableCopies once; Return (menu 12) increases it once.

## Changes made to the code you already wrote
- Fixed the spelling of attribute names so they match the assignment exactly:
  memberNumeber -> memberNumber, programNumber -> programName, employeeNumeber -> employeeNumber,
  officeNumeber -> officeNumber, enrollmenDate -> enrollmentDate, assessmenDate -> assessmentDate,
  paymenDate -> paymentDate, retrunedDate -> returnedDate.
- Interface constants renamed to the exact required names (MAX_ID_LENGTH, ID_SEPARATOR,
  DEFAULT_STATUS, MIN_NAME_LENGTH, ...) and given real values.
- Exceptions: created ValidationException, fixed names BookUnavailableException and
  RecordNotFoundException (you had a duplicated file), added message constructors.
- Filled every TODO (getId, getDisplayName, validate, printDetails, getRegistrationSummary).
- Main menu now follows the exact numbering of the assignment (1-19, 0 Exit).

## Rules chosen where the assignment gives two values
- Study year: 1-6 (Section 8). Section 17 shows 1-8; change `intCheck("Study year", 1, 6)` in Main
  and `requireRange(studyYear, ...)` in Student if your lecturer wants 1-8.
- Allowed values: academic status ACTIVE/INACTIVE; enrollment status ACTIVE/COMPLETED/DROPPED;
  semester FALL/SPRING/SUMMER; assessment type QUIZ/ASSIGNMENT/MIDTERM/FINAL_EXAM/PROJECT;
  payment method CASH/MOBILE_MONEY/BANK_TRANSFER/CARD; employment type FULL_TIME/PART_TIME/CONTRACT.
  (All defined once in `Registrable`.)
- Dates of birth must be in the past; admission/hire/enrollment/payment/assessment/borrowed dates
  must not be in the future.

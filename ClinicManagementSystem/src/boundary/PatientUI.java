package boundary;

/**
 * @author Lucas *
 */
import java.util.Scanner;
import entity.Patient;
import entity.PatientRecord;
import control.PatientManager;

public class PatientUI {

    private final PatientManager manager;
    private final Scanner scanner = new Scanner(System.in);

    public PatientUI(PatientManager manager) {
        this.manager = manager;
    }

    // Replacement for String.repeat() method for Java 8 compatibility
    private String repeat(String str, int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(str);
        }
        return result.toString();
    }

    // Admin or User??
    public void start() {
        System.out.println("Are you an Admin or User? (enter 'exit' to quit):");
        String role = scanner.nextLine().trim().toLowerCase();

        if (role.equals("exit")) {
            System.out.println("\nThank you for your time!");
            System.out.println("Have a nice day!");
        } else if (role.equals("admin")) {
            AdminMenu();
        } else if (role.equals("user")) {
            UserMenu();
        } else {
            showErrorMessage("Invalid role! Please enter admin/user/exit only.");
            start();
        }
    }

    // =================== Admin Menu =================== 
    private void AdminMenu() {
        int choice;

        printAdminMenu();
        choice = getIntInput("Enter your choice: ");

        if (choice == 1) {
            //New patient
            registerNewPatient();
        } else if (choice == 2) {
            //View all patient
            viewAllPatientsBasic();
        } else if (choice == 3) {
            //Update patient
            updatePatientMenu();
        } else if (choice == 4) {
            //Delete patient
            deletePatientMenu();
        } else if (choice == 5) {
            //Search patient through ID
            viewPatientDetailsMenu();
        } else if (choice == 6) {
            //View all queued patients
            viewQueuedPatients();
        } else if (choice == 7) {
            //View statistics
            displayStatisticsReport();
        } else if (choice == 0) {
            System.out.println("\nReturning to main menu...");
        } else {
            showErrorMessage("Invalid selection. Please try again");
        }

        // Loop back to AdminMenu
        if (choice != 0) {
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            AdminMenu();
        }
    }

    private void printAdminMenu() {
        System.out.println("\n" + repeat("=", 55));
        System.out.println("                      ADMIN MENU ");
        System.out.println("\n" + repeat("=", 55));
        System.out.println("1. Register New Patient");
        System.out.println("2. View All Patients");
        System.out.println("3. Update Patient Information");
        System.out.println("4. Delete Patient Information");
        System.out.println("5. Search Patient");
        System.out.println("6. View Queue");
        System.out.println("7. Statistic Report");
        System.out.println("0. Back to Main Menu");
        System.out.println("\n" + repeat("=", 55));
    }
    // =================== Admin Menu =================== 

    // =================== User Menu =================== 
    private void UserMenu() {
        int choice;

        printUserMenu();
        choice = getIntInput("Enter your choice: ");

        if (choice == 1) {
            //Register new patient or add new visit
            registerPatientOrAddVisit();
        } else if (choice == 2) {
            //View my record
            viewMyRecords();
        } else if (choice == 3) {
            //Check queue position
            checkMyQueuePosition();
        } else if (choice == 4) {
            //Check queue status
            viewQueueStatus();
        } else if (choice == 0) {
            System.out.println("\nReturning to main menu...");
        } else {
            showErrorMessage("Invalid selection. Please try again.");
        }

        // Loop back to UserMenu
        if (choice != 0) {
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            UserMenu();
        }
    }

    private void printUserMenu() {
        System.out.println("\n" + repeat("=", 55));
        System.out.println("                      USER MENU ");
        System.out.println("\n" + repeat("=", 55));
        System.out.println("1. Register as New Patient / Add New Visit");
        System.out.println("2. View My Medical Records");
        System.out.println("3. Check My Queue Position");
        System.out.println("4. View Queue Status");
        System.out.println("0. Back to Main Menu");
        System.out.println("\n" + repeat("=", 55));
    }
    // =================== User Menu =================== 

    // =================== Registration Methods =================== 
    // Modified method for User Menu - handles both new patient and existing patient
    void registerPatientOrAddVisit() {
        System.out.println("\n" + repeat("=", 60));
        System.out.println("         PATIENT REGISTRATION / NEW VISIT");
        System.out.println(repeat("=", 60));

        // First, get the patient ID
        String patientId = inputPatientId();
        if (patientId == null) {
            return;
        }

        // Check if patient already exists
        if (manager.patientExists(patientId)) {
            // Existing patient - add new visit
            addNewVisitForExistingPatient(patientId);
        } else {
            // New patient - full registration
            registerNewPatientWithId(patientId);
        }
    }

    public void getRegNewP() {
        registerNewPatient();
    }

    // Register New Patient (Admin Menu)
    private void registerNewPatient() {
        System.out.println("\n" + repeat("=", 50));
        System.out.println("            NEW PATIENT REGISTRATION");
        System.out.println(repeat("=", 50));

        Patient newPatient = inputPatientDetails();
        if (newPatient != null) {
            System.out.print("Visit reason: ");
            String visitReason = scanner.nextLine().trim();
            if (visitReason.isEmpty()) {
                visitReason = "General consultation";
            }

            if (manager.registerPatient(newPatient, visitReason)) {
                showSuccessMessage("Registration completed successfully! 🎉");
                System.out.println("Visit reason: " + visitReason);
                System.out.println("Patient added to queue for consultation.");
            }
        }
    }

    // Register new patient with specific ID
    private void registerNewPatientWithId(String patientId) {
        System.out.println("\n NEW PATIENT REGISTRATION");
        System.out.println("Patient ID: " + patientId + " (New Patient)");
        System.out.println(repeat("-", 50));

        Patient newPatient = inputPatientDetailsWithId(patientId);
        if (newPatient != null) {
            System.out.print("Visit reason: ");
            String visitReason = scanner.nextLine().trim();
            if (visitReason.isEmpty()) {
                visitReason = "General consultation";
            }

            if (manager.registerPatient(newPatient, visitReason)) {
                showSuccessMessage("New patient registration completed successfully! 🎉");
                System.out.println("Visit reason: " + visitReason);
                System.out.println("Patient added to queue for consultation.");
            }
        }
    }

    // Add new visit for existing patient
    private void addNewVisitForExistingPatient(String patientId) {
        System.out.println("\n ADD NEW VISIT RECORD");
        System.out.println("Patient ID: " + patientId + " (Existing Patient)");
        System.out.println(repeat("-", 50));

        // Get current patient information
        Patient currentPatient = manager.getCurrentPatient(patientId);
        if (currentPatient == null) {
            showErrorMessage("Unable to retrieve current patient information.");
            return;
        }

        // Display current information
        System.out.println("CURRENT PATIENT INFORMATION:");
        displayPatient(currentPatient);

        System.out.println("\n" + repeat("=", 50));
        System.out.println("UPDATE PATIENT INFORMATION FOR NEW VISIT");
        System.out.println("(Press Enter to keep current value)");
        System.out.println(repeat("=", 50));

        // Allow user to update information for new visit
        Patient updatedPatient = updatePatientInfoForNewVisit(currentPatient);
        if (updatedPatient == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        // Get visit reason
        System.out.print("Visit reason: ");
        String visitReason = scanner.nextLine().trim();
        if (visitReason.isEmpty()) {
            visitReason = "Follow-up consultation";
        }

        // Add new visit record
        if (manager.addNewVisit(patientId, updatedPatient, visitReason)) {
            showSuccessMessage("New visit record added successfully! 🎉");
            System.out.println("Visit reason: " + visitReason);
            System.out.println("Patient added to queue for consultation.");
            System.out.println("Previous records have been saved to history.");
        } else {
            showErrorMessage("Failed to add new visit record.");
        }
    }

    // Helper method to update patient info for new visit
    private Patient updatePatientInfoForNewVisit(Patient currentPatient) {
        try {
            System.out.print("Name [" + currentPatient.getName() + "]: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = currentPatient.getName();
            }

            System.out.print("Age [" + currentPatient.getAge() + "]: ");
            String ageInput = scanner.nextLine().trim();
            int age = currentPatient.getAge();
            if (!ageInput.isEmpty()) {
                try {
                    age = Integer.parseInt(ageInput);
                    if (age <= 0 || age > 150) {
                        showErrorMessage("Please enter a valid age (1-150). Using current age.");
                        age = currentPatient.getAge();
                    }
                } catch (NumberFormatException e) {
                    showErrorMessage("Invalid age format. Using current age.");
                }
            }

            System.out.print("Gender [" + currentPatient.getGender() + "]: ");
            String gender = scanner.nextLine().trim();
            if (gender.isEmpty()) {
                gender = currentPatient.getGender();
            }

            System.out.print("Contact Number [" + currentPatient.getContactNumber() + "]: ");
            String contactNumber = scanner.nextLine().trim();
            if (contactNumber.isEmpty()) {
                contactNumber = currentPatient.getContactNumber();
            }

            System.out.print("Address [" + currentPatient.getAddress() + "]: ");
            String address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                address = currentPatient.getAddress();
            }

            System.out.print("Illness Description [" + currentPatient.getIllnessDescription() + "]: ");
            String illnessDescription = scanner.nextLine().trim();
            if (illnessDescription.isEmpty()) {
                illnessDescription = currentPatient.getIllnessDescription();
            }

            return new Patient(currentPatient.getPatientId(), name, age, gender,
                    contactNumber, address, illnessDescription);

        } catch (Exception e) {
            showErrorMessage("An error occurred while updating patient information.");
            return null;
        }
    }

    public void getViewAllP() {
        viewAllPatientsBasic();
    }

    // View All Patients
    private void viewAllPatientsBasic() {
        System.out.println("\n" + repeat("=", 50));
        System.out.println("             ALL PATIENTS INFO");
        System.out.println(repeat("=", 50));

        String[][] patients = manager.getAllPatientsBasicInfo();
        if (patients.length == 0) {
            showErrorMessage("No patients found in the system.");
            return;
        }

        System.out.println("Total Patients: " + patients.length);
        System.out.println("=" + repeat("=", 40));
        System.out.printf("%-15s | %-25s%n", "PATIENT ID", "NAME");
        System.out.println(repeat("-", 43));

        for (String[] patient : patients) {
            System.out.printf("%-15s | %-25s%n", patient[0], patient[1]);
        }

        System.out.println(repeat("-", 43));
        System.out.print("\nEnter Patient ID to view details (or press Enter to return): ");
        String selection = scanner.nextLine().trim();

        if (!selection.isEmpty()) {
            viewPatientDetailsById(selection);
        }
    }

    private void viewPatientDetailsById(String patientId) {
        PatientRecord[] history = manager.getPatientHistory(patientId);
        if (history.length == 0) {
            showErrorMessage("Patient ID not found: " + patientId);
            return;
        }

        // Show current active record
        for (PatientRecord record : history) {
            if (record.isActive()) {
                System.out.println("\nCURRENT PATIENT DETAILS");
                displayPatientRecord(record);
                break;
            }
        }
    }

    public void viewPD() {
        viewPatientDetailsMenu();
    }

    private void viewPatientDetailsMenu() {
        System.out.println("\n" + repeat("=", 50));
        System.out.println("         PATIENT DETAILS & HISTORY VIEWER");
        System.out.println(repeat("=", 50));

        String patientId = inputPatientId();
        if (patientId == null) {
            return;
        }

        PatientRecord[] history = manager.getPatientHistory(patientId);
        if (history.length == 0) {
            showErrorMessage("No records found for Patient ID: " + patientId);
            return;
        }

        System.out.println("\nFound " + history.length + " record(s) for Patient ID: " + patientId);
        System.out.println("=" + repeat("=", 60));

        // Display all records with selection
        for (int i = 0; i < history.length; i++) {
            PatientRecord record = history[i];
            String status = record.isActive() ? " ACTIVE" : " HISTORICAL";
            System.out.println((i + 1) + ". " + status + " - " + record.getFormattedDate()
                    + " | Visit: " + record.getVisitReason());
        }

        System.out.println("\nSelect record to view details (1-" + history.length + "), or 0 to cancel:");
        int selection = getIntInput("Selection");

        if (selection > 0 && selection <= history.length) {
            displayPatientRecord(history[selection - 1]);
        }
    }

    public void getUpdPM() {
        updatePatientMenu();
    }

    // Update Patient Information
    private void updatePatientMenu() {
        System.out.println("\n" + repeat("=", 50));
        System.out.println("            ️ UPDATE PATIENT INFORMATION");
        System.out.println(repeat("=", 50));

        String patientId = inputPatientId();
        if (patientId == null) {
            return;
        }

        Patient current = manager.getCurrentPatient(patientId);
        if (current == null) {
            showErrorMessage("Patient not found or has no active record.");
            return;
        }

        System.out.println("\nCURRENT PATIENT DETAILS:");
        displayPatient(current);

        System.out.println("\nSELECT FIELD TO UPDATE:");
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Gender");
        System.out.println("4. Contact Number");
        System.out.println("5. Address");
        System.out.println("6. Illness Description");
        System.out.println("7. Visit Reason");
        System.out.println("0. Cancel");

        int choice = getIntInput("Select field");
        if (choice == 0) {
            return;
        }

        String fieldName = "";
        String currentValue = "";

        if (choice == 1) {
            fieldName = "name";
            currentValue = current.getName();
        } else if (choice == 2) {
            fieldName = "age";
            currentValue = String.valueOf(current.getAge());
        } else if (choice == 3) {
            fieldName = "gender";
            currentValue = current.getGender();
        } else if (choice == 4) {
            fieldName = "contact";
            currentValue = current.getContactNumber();
        } else if (choice == 5) {
            fieldName = "address";
            currentValue = current.getAddress();
        } else if (choice == 6) {
            fieldName = "illness";
            currentValue = current.getIllnessDescription();
        } else if (choice == 7) {
            fieldName = "visitreason";
            PatientRecord[] history = manager.getPatientHistory(patientId);
            currentValue = "";
            for (PatientRecord record : history) {
                if (record.isActive()) {
                    currentValue = record.getVisitReason();
                    break;
                }
            }
        } else {
            showErrorMessage("Invalid selection.");
            return;
        }

        System.out.println("\nCurrent " + fieldName + ": " + currentValue);
        System.out.print("Enter new value (or press Enter to cancel): ");
        String newValue = scanner.nextLine().trim();

        if (newValue.isEmpty()) {
            System.out.println("Update cancelled.");
            return;
        }

        if (manager.updatePatientField(patientId, fieldName, newValue)) {
            showSuccessMessage("Patient information updated successfully! ✅");
        } else {
            showErrorMessage("Failed to update patient information.");
        }
    }

    public void getDelP() {
        deletePatientMenu();
    }

    // Delete Patient Information
    private void deletePatientMenu() {
        System.out.println("\n" + repeat("=", 50));
        System.out.println("            ️ DELETE PATIENT");
        System.out.println(repeat("=", 50));

        String patientId = inputPatientId();
        if (patientId == null) {
            return;
        }

        PatientRecord[] history = manager.getPatientHistory(patientId);
        if (history.length == 0) {
            showErrorMessage("Patient not found.");
            return;
        }

        System.out.println("\nPATIENT TO BE DELETED:");
        System.out.println("Total records: " + history.length);
        for (PatientRecord record : history) {
            if (record.isActive()) {
                displayPatientRecord(record);
                break;
            }
        }

        System.out.println("\nWARNING: This will delete ALL records for this patient!");
        System.out.print("Are you sure you want to proceed? Type 'DELETE' to confirm: ");
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equals("DELETE")) {
            if (manager.deletePatient(patientId)) {
                showSuccessMessage("Patient and all records deleted successfully.");
            } else {
                showErrorMessage("Failed to delete patient.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    public void viewQP() {
        viewQueuedPatients();
    }

    // View Queue
    private void viewQueuedPatients() {
        System.out.println("\n" + repeat("=", 50));
        System.out.println("             CURRENT QUEUE");
        System.out.println(repeat("=", 50));

        Patient[] queuedPatients = manager.getQueuedPatients();
        if (queuedPatients.length == 0) {
            showErrorMessage("No patients currently in queue.");
            return;
        }

        System.out.println("Total patients in queue: " + queuedPatients.length);
        System.out.println("=" + repeat("=", 55));
        System.out.printf("%-8s | %-15s | %-20s | %-10s%n", "POS", "PATIENT ID", "NAME", "AGE");
        System.out.println(repeat("-", 57));

        for (int i = 0; i < queuedPatients.length; i++) {
            Patient p = queuedPatients[i];
            System.out.printf("%-8d | %-15s | %-20s | %-10d%n",
                    (i + 1), p.getPatientId(), p.getName(), p.getAge());
        }
        System.out.println(repeat("-", 57));
    }

    public void getStatRep() {
        displayStatisticsReport();
    }

    private void displayStatisticsReport() {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("                    PATIENT STATISTICS REPORT");
        System.out.println(repeat("=", 70));

        PatientManager.StatisticsReport report = manager.generateStatisticsReport();

        // Basic System Information
        System.out.println("\n📊 SYSTEM OVERVIEW:");
        System.out.println(repeat("-", 50));
        System.out.println("Total Patient Records    : " + report.totalRecords);
        System.out.println("Unique Patients          : " + report.totalUniquePatients);
        System.out.println("Patients in Queue        : " + report.currentQueueSize);
        System.out.printf("Average Visits per Patient: %.2f%n", report.averageVisitsPerPatient);

        // Most Frequent Patient
        System.out.println("\n🔄 VISIT FREQUENCY:");
        System.out.println(repeat("-", 50));
        if (!report.mostFrequentPatientId.isEmpty()) {
            System.out.println("Most Frequent Patient    : " + report.mostFrequentPatientName
                    + " (" + report.mostFrequentPatientId + ")");
            System.out.println("Total Visits             : " + report.mostFrequentPatientVisits);
        } else {
            System.out.println("No visit frequency data available.");
        }

        // Age Demographics
        System.out.println("\n👥 AGE DEMOGRAPHICS:");
        System.out.println(repeat("-", 50));
        System.out.printf("Average Age              : %.1f years%n", report.averageAge);
        System.out.println("Children (Under 18)      : " + report.patientsUnder18);
        System.out.println("Adults (18-65)           : " + report.patientsAdults);
        System.out.println("Seniors (65+)            : " + report.patientsSeniors);

        // Gender Distribution
        System.out.println("\n⚥ GENDER DISTRIBUTION:");
        System.out.println(repeat("-", 50));
        System.out.println("Male Patients            : " + report.malePatients);
        System.out.println("Female Patients          : " + report.femalePatients);
        if (report.otherGenderPatients > 0) {
            System.out.println("Other Gender             : " + report.otherGenderPatients);
        }

        // Calculate and display percentages
        if (report.totalUniquePatients > 0) {
            double malePercent = (double) report.malePatients / report.totalUniquePatients * 100;
            double femalePercent = (double) report.femalePatients / report.totalUniquePatients * 100;
            System.out.printf("Male: %.1f%%, Female: %.1f%%%n", malePercent, femalePercent);
        }

        // Common Visit Reasons
        System.out.println("\n🏥 VISIT PATTERNS:");
        System.out.println(repeat("-", 50));
        if (!report.mostCommonVisitReason.isEmpty()) {
            System.out.println("Most Common Visit Reason : " + report.mostCommonVisitReason);
            System.out.println("Occurrences              : " + report.mostCommonVisitReasonCount);
        } else {
            System.out.println("No visit reason data available.");
        }

        // Common Illnesses
        System.out.println("\n🩺 HEALTH CONDITIONS:");
        System.out.println(repeat("-", 50));
        if (!report.mostCommonIllness.isEmpty()) {
            System.out.println("Most Common Condition    : " + report.mostCommonIllness);
            System.out.println("Affected Patients        : " + report.mostCommonIllnessCount);
        } else {
            System.out.println("No illness data available.");
        }

        System.out.println("\n" + repeat("=", 70));
        System.out.println("Report generated successfully! 📈");
    }

    public void getViewQueueS() {
        viewQueueStatus();
    }

    private void viewQueueStatus() {
        int queueSize = manager.getQueueSize();
        System.out.println("\n" + repeat("=", 40));
        System.out.println("           QUEUE STATUS");
        System.out.println(repeat("=", 40));
        System.out.println(" Patients in queue: " + queueSize);

        if (queueSize > 0) {
            Patient next = manager.peekNextPatient();
            System.out.println("Next patient: " + next.getName() + " (" + next.getPatientId() + ")");

            if (queueSize > 1) {
                System.out.println("Estimated wait time: ~" + (queueSize * 15) + " minutes");
            }
        } else {
            System.out.println("No patients waiting");
        }
        System.out.println(repeat("=", 40));
    }

    private void checkQueuePosition() {
        String patientId = inputPatientId();
        if (patientId == null) {
            return;
        }

        int position = manager.getPatientQueuePosition(patientId);
        if (position > 0) {
            System.out.println("\nQueue Position Information:");
            System.out.println("Patient ID '" + patientId + "' is at position " + position);
            System.out.println("Estimated wait time: ~" + (position * 15) + " minutes");
            if (position == 1) {
                System.out.println("You're next!");
            }
        } else {
            showErrorMessage("Patient '" + patientId + "' is not in queue or doesn't exist.");
        }
    }

    public void getMyCheckQueueP() {
        checkMyQueuePosition();
    }

    private void checkMyQueuePosition() {
        System.out.println("\n" + repeat("=", 50));
        System.out.println("             MY QUEUE POSITION");
        System.out.println(repeat("=", 50));
        checkQueuePosition();
    }

    // Statistic Report
    public void getViewRec() {
        viewMyRecords();
    }

    // ========== User Methods ==========
    private void viewMyRecords() {
        System.out.println("\n" + repeat("=", 50));
        System.out.println("             MY MEDICAL RECORDS");
        System.out.println(repeat("=", 50));

        String patientId = inputPatientId();
        if (patientId == null) {
            return;
        }

        PatientRecord[] history = manager.getPatientHistory(patientId);
        if (history.length == 0) {
            showErrorMessage("No records found for your Patient ID.");
            System.out.println("If you're a new patient, please register first.");
            return;
        }

        System.out.println("Your medical history (" + history.length + " record(s)):");
        System.out.println("=" + repeat("=", 60));

        for (int i = 0; i < history.length; i++) {
            PatientRecord record = history[i];
            String status = record.isActive() ? " CURRENT" : " " + record.getFormattedDate();
            System.out.println((i + 1) + ". " + status + " - Visit: " + record.getVisitReason());
        }

        System.out.println("\nSelect record to view details (1-" + history.length + "), or 0 to return:");
        int selection = getIntInput("Selection");

        if (selection > 0 && selection <= history.length) {
            displayPatientRecord(history[selection - 1]);
        }
    }
    // ========== User Methods ==========

    // ==========  Input Methods ==========
    private Patient inputPatientDetails() {
        String id = manager.generateNewPatientID();
        System.out.println("Generated Patient ID: " + id);
        return inputPatientDetailsWithId(id);
    }

    private Patient inputPatientDetailsWithId(String id) {
        try {
            String name;
            while (true) {
                System.out.print("Name: ");
                name = scanner.nextLine().trim();
                if (!name.isEmpty() && name.matches("^[A-Za-z ]+$")) {
                    break;
                }
                showErrorMessage("Name cannot be empty and must contain only letters.");
            }

            int age;
            while (true) {
                System.out.print("Age: ");
                age = getIntInput("");
                if (age > 0 && age <= 150) {
                    break;
                }
                showErrorMessage("Please enter a valid age.");
            }

            String gender;
            while (true) {
                System.out.print("Gender (M/F): ");
                gender = scanner.nextLine().trim().toUpperCase();
                if (gender.equals("M") || gender.equals("F")) {
                    break;
                }
                showErrorMessage("Gender must be either 'M' or 'F'.");
            }

            String contactNumber;
            while (true) {
                System.out.print("Contact Number: ");
                contactNumber = scanner.nextLine().trim();
                if (contactNumber.matches("^0\\d{9,10}$")) {
                    break;
                }
                showErrorMessage("Contact number must be a valid Malaysian number (10–11 digits, starting with 0).");
            }

            String address;
            while (true) {
                System.out.print("Address: ");
                address = scanner.nextLine().trim();
                if (!address.isEmpty() && address.length() >= 5) {
                    break;
                }
                showErrorMessage("Address cannot be empty and must be at least 5 characters.");
            }

            String illnessDescription;
            while (true) {
                System.out.print("Illness Description: ");
                illnessDescription = scanner.nextLine().trim();
                if (!illnessDescription.isEmpty() && illnessDescription.length() <= 200) {
                    break;
                }
                showErrorMessage("Illness description cannot be empty and must be less than 200 characters.");
            }

            return new Patient(id, name, age, gender, contactNumber, address, illnessDescription);

        } catch (Exception e) {
            showErrorMessage("An error occurred while entering patient details. Please try again.");
            return null;
        }
    }

    private String inputPatientId() {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            showErrorMessage("Patient ID cannot be empty.");
            return null;
        }
        return id;
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                if (!prompt.isEmpty()) {
                    System.out.print(prompt + ": ");
                }
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                showErrorMessage("Please enter a valid number.");
            }
        }
    }

    private void displayPatient(Patient p) {
        System.out.println("\n" + repeat("=", 45));
        System.out.println("            PATIENT INFORMATION");
        System.out.println(repeat("=", 45));
        System.out.printf("%-15s: %s%n", "Patient ID", p.getPatientId());
        System.out.printf("%-15s: %s%n", "Name", p.getName());
        System.out.printf("%-15s: %d years old%n", "Age", p.getAge());
        System.out.printf("%-15s: %s%n", "Gender", p.getGender());
        System.out.printf("%-15s: %s%n", "Contact", p.getContactNumber());
        System.out.printf("%-15s: %s%n", "Address", p.getAddress());
        System.out.printf("%-15s: %s%n", "Illness", p.getIllnessDescription());
        System.out.println(repeat("=", 45));
    }

    private void displayPatientRecord(PatientRecord record) {
        System.out.println("\n" + repeat("=", 50));
        System.out.println("            DETAILED PATIENT RECORD");
        System.out.println(repeat("=", 50));
        String status = record.isActive() ? " ACTIVE RECORD" : " HISTORICAL RECORD";
        System.out.println("Status: " + status);
        System.out.println("Record ID: " + record.getRecordId());
        System.out.println("Date: " + record.getFormattedDate());
        System.out.println("Visit Reason: " + record.getVisitReason());
        System.out.println(repeat("-", 50));
        System.out.printf("%-15s: %s%n", "Patient ID", record.getPatientId());
        System.out.printf("%-15s: %s%n", "Name", record.getName());
        System.out.printf("%-15s: %d years old%n", "Age", record.getAge());
        System.out.printf("%-15s: %s%n", "Gender", record.getGender());
        System.out.printf("%-15s: %s%n", "Contact", record.getContactNumber());
        System.out.printf("%-15s: %s%n", "Address", record.getAddress());
        System.out.printf("%-15s: %s%n", "Illness", record.getIllnessDescription());
        System.out.println(repeat("=", 50));
    }

    private void showSuccessMessage(String msg) {
        System.out.println("\n [SUCCESS] " + msg);
    }

    private void showErrorMessage(String msg) {
        System.out.println("\n [ERROR] " + msg);
    }

}

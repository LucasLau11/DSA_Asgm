/*@author chngthingwei*/
package boundary;

import adt.*;
import control.*;
import entity.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Scanner;

public class TreatmentUI {

    private final PharmacyManager pharmacyManager;
    private final TreatmentManager manager;
    private final PatientManager patientManager;
    private DoctorManager doctorManager = new DoctorManager();
    private final Scanner sc = new Scanner(System.in);

    private MyLinkedList<Consultation> bookingList = BookingList.bookingList;

    public TreatmentUI(PharmacyManager pharmacyManager, PatientManager patientManager) {
        this.patientManager = patientManager;
        this.manager = new TreatmentManager(patientManager);
        this.pharmacyManager = pharmacyManager;  // shared instance
    }

    public void patientMenu() {
        while (true) {
            System.out.print("Enter your Patient ID (format P001): ");
            String patientId = sc.nextLine();
            if (!patientId.matches("P\\d{3}")) {
                System.out.println("Invalid Patient ID format! Please try again.");
                continue;
            }
            System.out.println("\n==== Your Treatments ====");
            manager.searchTreatmentByPatientId(patientId);
            break;
        }

    }

    public void menu(Map timetablePerDate) {

        int choice = -1;
        do {
            System.out.println("+--------------------------------------+");
            System.out.println("|      TREATMENT MANAGEMENT MENU       |");
            System.out.println("+--------------------------------------+");
            System.out.println("| 1. Display All Treatments            |");
            System.out.println("| 2. Add Treatment                     |");
            System.out.println("| 3. Search by Patient ID              |");
            System.out.println("| 4. Remove by Treatment ID            |");
            System.out.println("| 5. Update Treatment                  |");
            System.out.println("| 6. Sort by Date                      |");
            System.out.println("| 7. Treatment Summary Reports         |");
            System.out.println("| 0. Exit                              |");
            System.out.println("+--------------------------------------+");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 ->
                    displayAllTreatments();//done
                case 2 ->
                    addTreatmentUI(timetablePerDate);//done validation,wait doctor id link
                case 3 ->
                    searchTreatmentUI();//done
                case 4 ->
                    removeTreatmentUI();//done
                case 5 ->
                    updateTreatmentUI();//done, wait doctor link
                case 6 ->
                    sortByDate();//done
                case 7 ->
                    summaryReportsMenu();//enhancement
                case 0 ->
                    System.out.println("Goodbye.");
                default ->
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    public void displayAllTreatments() {
        ListInterface<Treatment> treatmentList = manager.getTreatmentList();

        if (treatmentList.getLength() == 0) {
            System.out.println("\n+-----------------------------------------------------------+");
            System.out.println("|                  No treatments found.                       |");
            System.out.println("+-----------------------------------------------------------+");
            return;
        }

        final int PAGE_SIZE = 3; // number of treatments per page
        int currentPage = 1;
        int totalPages = (int) Math.ceil((double) treatmentList.getLength() / PAGE_SIZE);

        while (true) {
            System.out.println("\n+-----------------------------------------------------------+");
            System.out.printf("|               ALL TREATMENTS (Page %d of %d)              |\n", currentPage, totalPages);
            System.out.println("+-----------------------------------------------------------+");

            int start = (currentPage - 1) * PAGE_SIZE + 1;
            int end = Math.min(start + PAGE_SIZE - 1, treatmentList.getLength());

            for (int i = start; i <= end; i++) {
                Treatment t = treatmentList.getEntry(i);
                String medID = t.getMedicineID();
                PharmacyMedicine med = pharmacyManager.searchMedicineByID(medID);

                System.out.printf("| %-15s: %-40s |\n", "Treatment ID", t.getTreatmentID());
                System.out.printf("| %-15s: %-40s |\n", "Patient", t.getPatientName() + "(" + t.getPatientID() + ")");
                System.out.printf("| %-15s: %-40s |\n", "Diagnosis", t.getDiagnosis());
                System.out.printf("| %-15s: %-40s |\n", "Doctor Name", t.getDoctorID());
                System.out.printf("| %-15s: %-40s |\n", "Medicine",
                        (med != null ? med.getName() : "Unknown Medicine (ID: " + medID + ")"));
                System.out.printf("| %-15s: %-40s |\n", "Dosage", t.getDosage());
                System.out.printf("| %-15s: %-40s |\n", "Duration(Days)", t.getDuration());
                System.out.printf("| %-15s: %-40s |\n", "Date", t.getTreatmentDate());
                System.out.printf("| %-15s: RM %-37.2f |\n", "Unit Price", t.getMedicineUnitPrice());
                System.out.printf("| %-15s: RM %-37.2f |\n", "ConsultationFee", t.getConsultationFee());
                System.out.printf("| %-15s: RM %-37.2f |\n", "Total Cost", t.getTotalCost());
                System.out.println("+-----------------------------------------------------------+");
            }

            // Pagination options
            System.out.println("\n[N] Next page | [P] Previous page | [E] Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim().toUpperCase();

            if (choice.equals("N") && currentPage < totalPages) {
                currentPage++;
            } else if (choice.equals("P") && currentPage > 1) {
                currentPage--;
            } else if (choice.equals("E")) {
                break;
            } else {
                System.out.println("Invalid choice or no more pages.");
            }
        }
    }

    public void callAdd(Map timetablePerDate) {
        addTreatmentUI(timetablePerDate);
    }

    private void addTreatmentUI(Map timetablePerDate) {

        ManageAppointmentUser manageAppointment = new ManageAppointmentUser();
        TreatmentManager treatmentManager = new TreatmentManager();
        Scanner scan = new Scanner(System.in);

        String patientName;
        int age;

        // patient validation loop
        while (true) {

            Map<Integer, Integer> displayMap = treatmentManager.displayAllBookingsTreatment(timetablePerDate);

            //WIP (let user choose specific record n do diagnosis treatment)
            System.out.print("Choose a booking to perform diagnosis [Q to quit] : ");

            String option = scan.nextLine().trim();

            if (option.equalsIgnoreCase("Q")) {
                System.out.println("Cancelled. Returning to main menu...");
                return;
            }
            if (!option.matches("\\d+")) {
                System.out.println("Invalid input. Please enter a number or 'Q' to quit.");
                return;
            }

            int treat = Integer.parseInt(option);

            if (displayMap.containsKey(treat)) {
                int actualIndex = displayMap.get(treat);   // convert to real index
                Consultation c = bookingList.getEntry(actualIndex);
                String chosenDate = c.getChosenDate();
                String chosenDr = c.getChosenDr();
                String patientID = c.getPatientID();

                patientName = patientManager.getPatientNameById(patientID);
                age = patientManager.getPatientAgeById(patientID);

                if (patientName == null) {
                    System.out.println("Error: Patient not found.");
                    continue; // re-prompt again
                }

                // show details
                System.out.println("Patient Name: " + patientName);
                System.out.println("Patient Age: " + age);

                // confirmation
                System.out.print("Use this patient? (Y=Yes, N=No, Q=Quit): ");
                String confirm = sc.nextLine().trim().toUpperCase();

                if (confirm.equals("Y")) {
                    String[] diagnosisList = {"Fever", "Flu", "Cough", "Cold", "Headache", "Stomachache", "Allergy", "Other"};

                    System.out.println("Select Diagnosis:");
                    for (int i = 0; i < diagnosisList.length; i++) {
                        System.out.println((i + 1) + ". " + diagnosisList[i]);
                    }

                    int choice;
                    while (true) {
                        System.out.print("Enter choice (1-" + diagnosisList.length + "): ");
                        try {
                            choice = Integer.parseInt(sc.nextLine());
                            if (choice >= 1 && choice <= diagnosisList.length) {
                                break;
                            }
                        } catch (NumberFormatException e) {
                            // Ignore and reprompt
                        }
                        System.out.println("Invalid choice, please try again.");
                    }

                    String diagnosis = diagnosisList[choice - 1];
                    PharmacyMedicine medicine = pharmacyManager.suggestAndSelectMedicine(diagnosis);

                    // Get the medicine ID and price
                    String medicineID = medicine.getMedicineID();

                    while (true) {
                        // Display available medicines first
                        if (medicine != null) {
                            if (medicine.getQuantityInStock() > 0) {
                                System.out.println("Medicine: " + medicine.getName());
                                System.out.printf("Price per Unit: RM %.2f%n", medicine.getPricePerUnit());
                                break;
                            } else {
                                System.out.println("Medicine is out of stock, please choose another.");
                            }
                        } else {
                            System.out.println("Unknown medicine ID. Please try again.");
                        }
                    }

                    // Get the unit price
                    double medicineUnitPrice = medicine.getPricePerUnit();

                    String suggestedDosage;

                    switch (medicine.getType()) {
                        case "Tablet":
                        case "Capsule":
                            // Most patients are 18–25
                            suggestedDosage = "500mg"; // standard adult dose
                            break;

                        case "Liquid":
                            suggestedDosage = "10ml"; // standard adult dose
                            break;

                        case "Injection":
                            suggestedDosage = "1ml"; // standard adult dose
                            break;

                        case "Ointment":
                            suggestedDosage = "Apply thin layer twice daily";
                            break;

                        default:
                            suggestedDosage = "Consult doctor";
                    }

                    System.out.println("Suggested Dosage: " + suggestedDosage);
                    System.out.print("Dosage (press Enter to accept suggestion): ");
                    String dosage = sc.nextLine();
                    if (dosage.isEmpty()) {
                        dosage = suggestedDosage;
                    }

                    String duration;
                    while (true) {
                        System.out.print("Duration (e.g., 5 days): ");
                        duration = sc.nextLine();
                        if (!duration.trim().isEmpty()) {
                            break;
                        }
                        System.out.println("Duration cannot be empty.");
                    }

                    double consultationFee;
                    consultationFee = 20.0;

                    System.out.println("Consultation Fee: RM " + consultationFee);

                    // Confirm before adding
                    while (true) {
                        System.out.print("Confirm add treatment? (Y/N): ");
                        String conf = sc.nextLine().trim().toUpperCase();
                        if (conf.equals("Y")) {
                            // Reduce stock
                            String treatmentID = manager.generateNewTreatmentID();
                            System.out.println("Auto-generated Treatment ID: " + treatmentID);
                            System.out.println("\n------ Treatment Summary ------");
                            System.out.println("Treatment ID        : " + treatmentID);
                            System.out.println("Patient             : " + patientName + " (" + patientID + "), Age: " + age);
                            System.out.println("Diagnosis           : " + diagnosis);
                            System.out.println("Doctor              : " + chosenDr);
                            System.out.println("Medicine            : " + medicine.getName() + " (" + medicineID + ")");
                            System.out.println("Dosage              : " + dosage);
                            System.out.println("Duration            : " + duration);
                            System.out.println("Treatment Date      : " + chosenDate);
                            System.out.printf("Medicine Unit Price  : RM %.2f%n", medicineUnitPrice);
                            System.out.printf("Consultation Fee     : RM %.2f%n", consultationFee);
                            System.out.printf("Total Cost           : RM %.2f%n", medicineUnitPrice + consultationFee);
                            medicine.setQuantityInStock(medicine.getQuantityInStock() - 1);
                            Treatment t = new Treatment(treatmentID, patientID, patientName, age, diagnosis, chosenDr, medicineID, chosenDate, dosage, duration, medicineUnitPrice, consultationFee);
                            manager.addTreatment(t);
                            Consultation chosen = bookingList.getEntry(treat);
                            chosen.setDiagnosed(true);
                            bookingList.remove(actualIndex);
                            System.out.println("Treatment added successfully. Stock updated. Remaining: " + medicine.getQuantityInStock());
                            break;
                        } else if (conf.equals("N")) {
                            System.out.println("Add treatment cancelled. No changes made.");
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter Y or N.");
                        }
                    }
                } else if (confirm.equals("N")) {
                    continue; // re-prompt for another patient ID
                } else if (confirm.equals("Q")) {
                    System.out.println("Cancelled. Returning to main menu...");
                    break;
                } else {
                    System.out.println("Invalid choice! Please enter Y, N or Q.");
                }

            } else {
                System.out.println("\n>> No booking was Found. Please Try Again Later. << \n");
                System.out.print("Press ENTER to proceed...");
                scan.nextLine();
            }
        }
    }

    public void callSearch() {
        searchTreatmentUI();
    }

    private void searchTreatmentUI() {
        while (true) {
            System.out.print("\nEnter Patient ID to search (or type 'Q' to quit): ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("Q")) {
                System.out.println("Exiting treatment search.");
                break;
            }

            if (input.isEmpty()) {
                System.out.println("Patient ID cannot be empty. Try again.");
                continue;
            }

            manager.searchTreatmentByPatientId(input);
        }
    }

    public void callRemove() {
        removeTreatmentUI();
    }

    private void removeTreatmentUI() {
        while (true) {
            System.out.print("Enter Patient ID to remove treatment (or Q to quit): ");
            String uid = sc.nextLine().trim();

            if (uid.equalsIgnoreCase("Q")) {
                System.out.println("Exiting treatment removal.");
                return; // exit whole method
            }

            // Check if patient exists
            if (!manager.existsPatientId(uid)) {
                System.out.println("No treatments found for Patient ID: " + uid);
                continue; // ask again for Patient ID
            }

            while (true) {
                // Display treatments for this patient
                manager.displayTreatmentsByPatientId(uid);

                int totalRecords = manager.countTreatmentsByPatientId(uid);
                System.out.print("Enter the record number to remove (1-" + totalRecords + ", or Q to quit): ");
                String input = sc.nextLine().trim();

                if (input.equalsIgnoreCase("Q")) {
                    System.out.println("Removal cancelled.");
                    break; // exit the loop
                }

                int recordNumber;
                try {
                    recordNumber = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number or Q to quit.");
                    continue; // restart loop
                }

                if (recordNumber < 1 || recordNumber > totalRecords) {
                    System.out.println("Invalid record number. Please try again.");
                    continue; // restart loop
                }

                // Confirm deletion
                System.out.print("Are you sure you want to delete treatment record " + recordNumber + "? (Y/N): ");
                String confirm = sc.nextLine().trim();

                if (!confirm.equalsIgnoreCase("Y")) {
                    System.out.println("Deletion cancelled.");
                    continue; // back to loop
                }

                // Perform removal using record number
                boolean success = manager.removeSpecificTreatment(uid, recordNumber);

                if (success) {
                    System.out.println("Treatment record " + recordNumber + " removed successfully.");
                } else {
                    System.out.println("Failed to remove treatment record.");
                }

                break; // stop loop after deletion attempt
            }
        }
    }

    public String[] getAllDoctors() {
        return doctorManager.getAllDoctorNames();
    }

    public void callUpd() {
        updateTreatmentUI();
    }

    private void updateTreatmentUI() {
        System.out.print("Enter Patient ID to update: ");
        String uid = sc.nextLine();

        if (!manager.existsPatientId(uid)) {
            System.out.println("No treatment found for Patient ID: " + uid);
            return;
        }

        while (true) {
            manager.displayTreatmentsByPatientId(uid);

            int totalRecords = manager.countTreatmentsByPatientId(uid);
            System.out.print("Enter the record number to update (1-" + totalRecords + ", or Q to quit): ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("Q")) {
                System.out.println("Update cancelled.");
                break;
            }

            int recordNumber;
            try {
                recordNumber = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or Q to quit.");
                continue;
            }

            if (recordNumber < 1 || recordNumber > totalRecords) {
                System.out.println("Invalid record number. Try again.");
                continue;
            }

            // --- Confirm Patient Info ---
            String patientName = patientManager.getPatientNameById(uid);
            if (patientName == null) {
                System.out.println("Error: Patient not found.");
                return;
            }
            System.out.println("Patient Name: " + patientName);

            int age = patientManager.getPatientAgeById(uid);
            System.out.println("Patient Age: " + age);

            // --- Diagnosis selection ---
            String[] diagnosisListNew = {"Fever", "Flu", "Cough", "Cold", "Headache", "Stomachache", "Allergy", "Other"};
            System.out.println("Select Diagnosis:");
            for (int i = 0; i < diagnosisListNew.length; i++) {
                System.out.println((i + 1) + ". " + diagnosisListNew[i]);
            }

            int choice;
            while (true) {
                System.out.print("Enter choice (1-" + diagnosisListNew.length + "): ");
                try {
                    choice = Integer.parseInt(sc.nextLine());
                    if (choice >= 1 && choice <= diagnosisListNew.length) {
                        break;
                    }
                } catch (NumberFormatException ignored) {
                }
                System.out.println("Invalid choice, try again.");
            }
            String newDiagnosis = diagnosisListNew[choice - 1];

            // --- Doctor List ---
            String[] allDoctors = getAllDoctors();
            String newDoctor;
            while (true) {
                System.out.println("\n--- Doctor List ---");
                for (int i = 0; i < allDoctors.length; i++) {
                    System.out.println((i + 1) + ". " + allDoctors[i]);
                }

                System.out.print("Choose a doctor (1/2/...) : ");
                String docInput = sc.nextLine().trim();

                if (!docInput.matches("\\d+")) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                int docChoice = Integer.parseInt(docInput);
                if (docChoice >= 1 && docChoice <= allDoctors.length) {
                    newDoctor = allDoctors[docChoice - 1];
                    break;
                } else {
                    System.out.println("Choice out of range. Try again.");
                }
            }

            // --- Medicine ---
            PharmacyMedicine medicine = null;

            while (true) {
                // Suggest and select medicine based on chosen diagnosis
                medicine = pharmacyManager.suggestAndSelectMedicine(newDiagnosis);

                if (medicine == null) {
                    System.out.println("Unknown medicine ID. Please try again (or type 'q' to quit).");
                    String choice2 = sc.nextLine();
                    if (choice2.equalsIgnoreCase("q")) {
                        return; // quit the process
                    }
                    continue; // retry loop
                }

                // Check stock
                if (medicine.getQuantityInStock() > 0) {
                    System.out.println("Medicine: " + medicine.getName());
                    System.out.printf("Price per Unit: RM %.2f%n", medicine.getPricePerUnit());

                    // Confirm before reducing stock
                    System.out.print("Confirm dispense this medicine? (y/n): ");
                    String confirm2 = sc.nextLine();
                    if (confirm2.equalsIgnoreCase("y")) {
                        // Reduce stock by 1
                        medicine.setQuantityInStock(medicine.getQuantityInStock() - 1);
                        System.out.println("Stock updated. Remaining: " + medicine.getQuantityInStock());
                        break; // exit loop after successful dispense
                    } else {
                        System.out.println("Cancelled. Please select again.");
                    }
                } else {
                    System.out.println("Medicine is out of stock, please choose another.");
                }
            }

            // Use medicine details
            String medicineID = medicine.getMedicineID();
            double medicineUnitPrice = medicine.getPricePerUnit();

            // ...
            // --- Dosage ---
            String suggestedDosage;
            if (age < 12) {
                suggestedDosage = "250mg";
            } else if (age < 60) {
                suggestedDosage = "500mg";
            } else {
                suggestedDosage = "300mg";
            }

            System.out.println("Suggested Dosage: " + suggestedDosage);
            System.out.print("New Dosage (Enter to use suggested): ");
            String newDosage = sc.nextLine();
            if (newDosage.isEmpty()) {
                newDosage = suggestedDosage;
            }

            // --- Duration ---
            String newDuration;
            while (true) {
                System.out.print("New Duration (e.g., 5 days): ");
                newDuration = sc.nextLine();
                if (!newDuration.trim().isEmpty()) {
                    break;
                }
                System.out.println("Duration cannot be empty.");
            }

            // --- Date ---
            String newDate;
            while (true) {
                System.out.print("New Date (DD-MM-YYYY) [Enter for today]: ");
                String newInputDate = sc.nextLine();
                if (newInputDate.isEmpty()) {
                    LocalDate newTreatmentDate = LocalDate.now();
                    DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    newDate = newTreatmentDate.format(newFormatter);
                    break;
                }
                try {
                    LocalDate newTreatmentDate = LocalDate.parse(newInputDate);
                    DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    newDate = newTreatmentDate.format(newFormatter);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format. Try again.");
                }
            }

            // --- Consultation Fee ---
            double newConsultationFee;
            while (true) {
                System.out.print("New Consultation Fee (e.g. 50.00): ");
                try {
                    newConsultationFee = Double.parseDouble(sc.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            // --- Confirm before updating ---
            System.out.print("Confirm update? (Y/N): ");
            String confirm = sc.nextLine().trim();
            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("Update cancelled.");
                continue; // back to main loop
            }

            boolean success = manager.updateSpecificTreatment(uid, recordNumber,
                    newDiagnosis, newDoctor, medicineID, newDosage, newDuration, newDate, newConsultationFee);

            System.out.println(success ? "Treatment updated successfully." : "Update failed.");
            break; // exit after update
        }
    }

    public void callSort() {
        sortByDate();
    }

    private void sortByDate() {
        ListInterface<Treatment> treatmentList = manager.getTreatmentList();
        int n = treatmentList.getLength();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= n - i; j++) {
                Treatment t1 = treatmentList.getEntry(j);
                Treatment t2 = treatmentList.getEntry(j + 1);

                LocalDate date1 = LocalDate.parse(t1.getTreatmentDate(), formatter);
                LocalDate date2 = LocalDate.parse(t2.getTreatmentDate(), formatter);

                if (date1.isAfter(date2)) {
                    // Swap Treatments
                    treatmentList.replace(j, t2);
                    treatmentList.replace(j + 1, t1);
                }
            }
        }

        System.out.println("Treatment list sorted by date in ascending order.");
        displayAllTreatments();
    }

    public void callSumRep() {
        summaryReportsMenu();
    }

    private void summaryReportsMenu() {
        while (true) {
            System.out.println("\n--- Treatment Summary Reports ---");
            System.out.println("1. Total Treatments per Patient");
            System.out.println("2. Total Revenue from Treatments");
            System.out.println("3. Most Common Diagnosis");
            System.out.println("4. Top Prescribed Medicine");
            System.out.println("Q. Quit");
            System.out.print("Enter choice: ");

            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("Q")) {
                System.out.println("Exiting summary reports menu...");
                break;
            }

            switch (input) {
                case "1" ->
                    manager.displayTotalTreatmentsPerPatient();
                case "2" ->
                    manager.displayTotalRevenue();
                case "3" ->
                    manager.displayMostCommonDiagnosis();
                case "4" ->
                    manager.displayTopPrescribedMedicine(pharmacyManager);
                default ->
                    System.out.println("Invalid choice. Please enter 1-4 or Q to quit.");
            }
        }
    }

}

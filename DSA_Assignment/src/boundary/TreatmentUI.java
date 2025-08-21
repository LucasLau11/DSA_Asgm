package boundary;

import adt.ListInterface;
import control.*;
import entity.*;

import java.time.LocalDate;
import java.util.Scanner;

public class TreatmentUI {
    private final PharmacyManager pharmacyManager;
    private final TreatmentManager manager;
    private final PatientManager patientManager; 
    private final Scanner sc = new Scanner(System.in);

    public TreatmentUI(PharmacyManager pharmacyManager, PatientManager patientManager) {
        this.pharmacyManager = pharmacyManager;
        this.patientManager = patientManager;
        this.manager = new TreatmentManager(patientManager); // Pass shared PatientManager
    }
    
    public void patientMenu() {
        System.out.print("Enter your Patient ID: ");
        String patientId = sc.nextLine();
        System.out.println("\n==== Your Treatments ====");
        manager.searchTreatmentByPatientId(patientId);
    }


    public void menu() {
        // Dummy data
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
                case 1 -> displayAllTreatments();
                case 2 -> addTreatmentUI();
                case 3 -> searchTreatmentUI();
                case 4 -> removeTreatmentUI();
                case 5 -> updateTreatmentUI();
                case 6 -> sortByDate();
                case 7 -> summaryReportsMenu();
                case 0 -> System.out.println("Goodbye.");
                default -> System.out.println("Invalid choice.");
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
            System.out.printf("| %-15s: %-40s |\n", "Patient",t.getPatientName()+"("+ t.getPatientID()+")");
            System.out.printf("| %-15s: %-40s |\n", "Diagnosis", t.getDiagnosis());
            System.out.printf("| %-15s: %-40s |\n", "Doctor ID", t.getDoctorID());
            System.out.printf("| %-15s: %-40s |\n", "Medicine", 
                (med != null ? med.getName() : "Unknown Medicine (ID: " + medID + ")"));
            System.out.printf("| %-15s: %-40s |\n", "Dosage", t.getDosage());
            System.out.printf("| %-15s: %-40s |\n", "Duration", t.getDuration());
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

   private void addTreatmentUI() {
    String treatmentID = manager.generateNewTreatmentID();
    System.out.println("Auto-generated Treatment ID: " + treatmentID);
  
    String patientID;
    while (true) {
        System.out.print("Patient ID (format P001): ");
        patientID = sc.nextLine();
        if (!patientManager.patientExists(patientID)) {
            System.out.println(" Patient not found! Please register the patient first.");
            return; 
        }
        if (patientID.matches("^P\\d{3}$")) {
            break;
        }
        System.out.println("Invalid Patient ID format! Please try again.");
    }

    String patientName = patientManager.getPatientNameById(patientID);
    if (patientName == null) {
        System.out.println("Error: Patient not found.");
        return;
    }

    System.out.println("Patient Name: " + patientName);
    int age = patientManager.getPatientAgeById(patientID);
    System.out.println("Patient Age: " + age);

    // NEW: Display patient's visit reason
    String visitReason = patientManager.getPatientVisitReason(patientID);
    if (visitReason != null) {
        System.out.println("Visit Reason: " + visitReason);
    }

    String[] diagnosisList = {"Fever","Flu","Cough","Cold","Headache","Stomachache","Allergy","Other"};
    
    // NEW: Suggest diagnosis based on visit reason
    String suggestedDiagnosis = patientManager.suggestDiagnosisFromVisitReason(patientID);
    
    System.out.println("Select Diagnosis:");
    for (int i = 0; i < diagnosisList.length; i++) {
        String marker = diagnosisList[i].equals(suggestedDiagnosis) ? " (Suggested)" : "";
        System.out.println((i + 1) + ". " + diagnosisList[i] + marker);
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

    // Rest of the existing code for doctor ID, medicine, etc...
    String doctorID;
    while (true) {
        System.out.print("Doctor ID (format D001): ");
        doctorID = sc.nextLine();
        if (doctorID.matches("^D\\d{3}$")) {
            break;
        }
        System.out.println("Invalid Doctor ID format! Please try again.");
    }

    PharmacyMedicine medicine = pharmacyManager.suggestAndSelectMedicine(diagnosis);
    String medicineID = medicine.getMedicineID();

    while (true) {
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

    double medicineUnitPrice = medicine.getPricePerUnit();
    medicine.setQuantityInStock(medicine.getQuantityInStock() - 1);
    System.out.println("Stock updated. Remaining: " + medicine.getQuantityInStock());

    String suggestedDosage;
    if (age < 12) suggestedDosage = "250mg";
    else if (age < 60) suggestedDosage = "500mg";
    else suggestedDosage = "300mg";

    System.out.println("Suggested Dosage: " + suggestedDosage);
    System.out.print("Dosage (e.g., 500mg): ");
    String dosage = sc.nextLine();
    if (dosage.isEmpty()) dosage = suggestedDosage;
        
    String duration;
    while (true) {
        System.out.print("Duration (e.g., 5 days): ");
        duration = sc.nextLine();
        if (!duration.trim().isEmpty()) break;
        System.out.println("Duration cannot be empty.");
    }

    LocalDate treatmentDate;
    while (true) {
        System.out.print("Date (YYYY-MM-DD) [Enter for today]: ");
        String inputDate = sc.nextLine();
        if (inputDate.isEmpty()) {
            treatmentDate = LocalDate.now();
            break;
        }
        try {
            treatmentDate = LocalDate.parse(inputDate);
            break;
        } catch (Exception e) {
            System.out.println("Invalid date format. Try again.");
        }
    }

    System.out.print("Consultation Fee (e.g. 50.00): ");
    double consultationFee;
    try {
        consultationFee = Double.parseDouble(sc.nextLine());
    } catch (NumberFormatException e) {
        System.out.println("Invalid consultation fee input.");
        return;
    }

    Treatment t = new Treatment(treatmentID, patientID, patientName, age, diagnosis, doctorID, 
                               medicineID, treatmentDate, dosage, duration, medicineUnitPrice, consultationFee);
    manager.addTreatment(t);
    
    // NEW: Integration steps after treatment creation
    // Update patient's visit reason to reflect the final diagnosis
    patientManager.updateVisitReasonFromDiagnosis(patientID, diagnosis);
    
    // Remove patient from queue as treatment is completed
    patientManager.removePatientFromQueueAfterTreatment(patientID);
    
    System.out.println("Treatment added successfully.");
    System.out.println("Patient has been treated and removed from queue.");
}

// NEW: Add method to view patient's queue status before treatment
private void checkPatientQueueStatus(String patientId) {
    if (patientManager.isPatientAwaitingTreatment(patientId)) {
        int position = patientManager.getPatientQueuePosition(patientId);
        System.out.println("Patient is in queue at position: " + position);
        String visitReason = patientManager.getPatientVisitReason(patientId);
        if (visitReason != null) {
            System.out.println("Visit reason: " + visitReason);
        }
    } else {
        System.out.println("Patient is not currently in queue.");
    }
}

    /**
 * Process a specific patient (called from queue processing)
 */
public void processSpecificPatient(String patientId) {
    if (!patientManager.patientExists(patientId)) {
        System.out.println("Patient not found!");
        return;
    }
    
    System.out.println("\n=== PROCESSING PATIENT: " + patientId + " ===");
    
    // Pre-populate treatment form with patient data
    addTreatmentForQueuedPatient(patientId);
}

/**
 * Streamlined treatment addition for queued patients
 */
private void addTreatmentForQueuedPatient(String patientID) {
    String treatmentID = manager.generateNewTreatmentID();
    System.out.println("Auto-generated Treatment ID: " + treatmentID);
    
    String patientName = patientManager.getPatientNameById(patientID);
    int age = patientManager.getPatientAgeById(patientID);
    String visitReason = patientManager.getPatientVisitReason(patientID);
    
    System.out.println("Patient ID: " + patientID);
    System.out.println("Patient Name: " + patientName);
    System.out.println("Patient Age: " + age);
    System.out.println("Visit Reason: " + visitReason);
    
    String[] diagnosisList = {"Fever","Flu","Cough","Cold","Headache","Stomachache","Allergy","Other"};
    String suggestedDiagnosis = patientManager.suggestDiagnosisFromVisitReason(patientID);
    
    System.out.println("\nSelect Diagnosis:");
    for (int i = 0; i < diagnosisList.length; i++) {
        String marker = diagnosisList[i].equals(suggestedDiagnosis) ? " (Suggested based on visit reason)" : "";
        System.out.println((i + 1) + ". " + diagnosisList[i] + marker);
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

    // Continue with rest of treatment process...
    String doctorID;
    while (true) {
        System.out.print("Doctor ID (format D001): ");
        doctorID = sc.nextLine();
        if (doctorID.matches("^D\\d{3}$")) {
            break;
        }
        System.out.println("Invalid Doctor ID format! Please try again.");
    }

    PharmacyMedicine medicine = pharmacyManager.suggestAndSelectMedicine(diagnosis);
    String medicineID = medicine.getMedicineID();

    if (medicine.getQuantityInStock() <= 0) {
        System.out.println("Selected medicine is out of stock. Treatment cannot proceed.");
        return;
    }

    double medicineUnitPrice = medicine.getPricePerUnit();
    medicine.setQuantityInStock(medicine.getQuantityInStock() - 1);
    System.out.println("Medicine: " + medicine.getName());
    System.out.printf("Price per Unit: RM %.2f%n", medicine.getPricePerUnit());
    System.out.println("Stock updated. Remaining: " + medicine.getQuantityInStock());

    String suggestedDosage;
    if (age < 12) suggestedDosage = "250mg";
    else if (age < 60) suggestedDosage = "500mg";
    else suggestedDosage = "300mg";

    System.out.println("Suggested Dosage: " + suggestedDosage);
    System.out.print("Dosage (e.g., 500mg) [Press Enter for suggested]: ");
    String dosage = sc.nextLine();
    if (dosage.isEmpty()) dosage = suggestedDosage;
        
    String duration;
    while (true) {
        System.out.print("Duration (e.g., 5 days): ");
        duration = sc.nextLine();
        if (!duration.trim().isEmpty()) break;
        System.out.println("Duration cannot be empty.");
    }

    LocalDate treatmentDate = LocalDate.now(); // Default to today for queue processing

    System.out.print("Consultation Fee (e.g. 50.00): ");
    double consultationFee;
    try {
        consultationFee = Double.parseDouble(sc.nextLine());
    } catch (NumberFormatException e) {
        System.out.println("Invalid consultation fee input. Using default of 50.00");
        consultationFee = 50.00;
    }

    // Create and add treatment
    Treatment t = new Treatment(treatmentID, patientID, patientName, age, diagnosis, doctorID, 
                               medicineID, treatmentDate, dosage, duration, medicineUnitPrice, consultationFee);
    manager.addTreatment(t);
    
    // Integration: Update patient records and remove from queue
    patientManager.updateVisitReasonFromDiagnosis(patientID, diagnosis);
    patientManager.removePatientFromQueueAfterTreatment(patientID);
    
    System.out.println("\n=== TREATMENT COMPLETED ===");
    System.out.println("Treatment ID: " + treatmentID);
    System.out.println("Patient treated successfully and removed from queue.");
    System.out.printf("Total Cost: RM %.2f%n", t.getTotalCost());
}

/**
 * Enhanced patient menu with queue status
 */
public void enhancedPatientMenu() {
    System.out.print("Enter your Patient ID: ");
    String patientId = sc.nextLine();
    
    if (!patientManager.patientExists(patientId)) {
        System.out.println("Patient ID not found. Please check your ID or register first.");
        return;
    }
    
    System.out.println("\n=== PATIENT INFORMATION ===");
    System.out.println("Patient ID: " + patientId);
    System.out.println("Name: " + patientManager.getPatientNameById(patientId));
    
    // Show queue status
    if (patientManager.isPatientAwaitingTreatment(patientId)) {
        int position = patientManager.getPatientQueuePosition(patientId);
        System.out.println("Queue Status: Position " + position + " (Awaiting treatment)");
        String visitReason = patientManager.getPatientVisitReason(patientId);
        if (visitReason != null) {
            System.out.println("Visit Reason: " + visitReason);
        }
    } else {
        System.out.println("Queue Status: Not in queue");
    }
    
    System.out.println("\n=== YOUR TREATMENT HISTORY ===");
    manager.searchTreatmentByPatientId(patientId);
}

    

    private void searchTreatmentUI() {
        System.out.print("Enter Patient ID to search: ");
        manager.searchTreatmentByPatientId(sc.nextLine());
    }

    private void removeTreatmentUI() {
    System.out.print("Enter Patient ID: ");
    String patientId = sc.nextLine();
    
    boolean found = false;

    System.out.println("Treatments for Patient ID " + patientId + ":");
    for (int i = 1; i <= manager.getTreatmentList().getLength(); i++) {
        Treatment t = manager.getTreatmentList().getEntry(i);
        if (t.getPatientID().equals(patientId)) {
            System.out.println("- " + t.getTreatmentID() + ": " + t.getDiagnosis() + " (" + t.getTreatmentDate() + ")");
            found = true;
        }
    }

    if (!found) {
        System.out.println("No treatments found for Patient ID " + patientId);
        return;
    }

    System.out.print("Enter Treatment ID to delete: ");
    String treatmentId = sc.nextLine();

    boolean removed = manager.removeTreatmentById(treatmentId);

    if (removed) {
        System.out.println("Treatment " + treatmentId + " has been removed.");
    } else {
        System.out.println("Treatment ID not found.");
    }
}

    private void updateTreatmentUI() {
        System.out.print("Enter Patient ID to update: ");
        String uid = sc.nextLine();

        if (!manager.existsPatientId(uid)) {
            System.out.println("No treatment found for Patient ID: " + uid);
            return;
        }

        manager.displayTreatmentsByPatientId(uid);

        int totalRecords = manager.countTreatmentsByPatientId(uid);
        System.out.print("Enter the record number to update (1-" + totalRecords + "): ");
        int recordNumber = sc.nextInt();
        sc.nextLine(); // consume newline

        if (recordNumber < 1 || recordNumber > totalRecords) {
            System.out.println("Invalid record number. Update cancelled.");
            return;
        }
        String patientName = patientManager.getPatientNameById(uid);

if (patientName == null) {
    System.out.println("Error: Patient not found.");
    return;
}

System.out.println("Patient Name: " + patientName);
int age=patientManager.getPatientAgeById(uid);
    while (true) {
        try {
            System.out.println("Patient Age: "+age);
            
            if (age >= 0 && age <= 120) break;
        } catch (NumberFormatException ignored) {}
        System.out.println("Invalid age. Please try again.");
    }
        String[] diagnosisListNew = {"Fever","Flu","Cough","Cold","Headache","Stomachache","Allergy","Other"};

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
    } catch (NumberFormatException e) {
        // Ignore and reprompt
    }
    System.out.println("Invalid choice, please try again.");
}

String newDiagnosis = diagnosisListNew[choice - 1];
String newDoctorID;
         while (true) {
        System.out.print("New Doctor ID (format D001): ");
        newDoctorID = sc.nextLine();
        if (newDoctorID.matches("^D\\d{3}$")) {
            break;
        }
        System.out.println("Invalid Doctor ID format! Please try again.");
    }
    
    PharmacyMedicine newMedicine = null;
    String newMedicineID;
    while (true) {
        System.out.print("New Medicine ID (format M001): ");
        newMedicineID = sc.nextLine();
        newMedicine = pharmacyManager.searchMedicineByID(newMedicineID);

        if (newMedicine != null) {
            if (newMedicine.getQuantityInStock() > 0) {
                System.out.println("Medicine: " + newMedicine.getName());
                System.out.println("Price per Unit: " + newMedicine.getPricePerUnit());
                break;
            } else {
                System.out.println("Medicine is out of stock, please choose another.");
            }
        } else {
            System.out.println("Unknown medicine ID. Please try again.");
        }
    }
        double medicineUnitPrice = 0;
if (newMedicine != null) {
    medicineUnitPrice = newMedicine.getPricePerUnit(); 
}
      
       newMedicine.setQuantityInStock(newMedicine.getQuantityInStock() - 1);
        
            

    String suggestedDosage;
    if (age < 12) suggestedDosage = "250mg";
    else if (age < 60) suggestedDosage = "500mg";
    else suggestedDosage = "300mg";

    System.out.println("Suggested Dosage: " + suggestedDosage);
    System.out.print("New Dosage (e.g., 500mg): ");
    String newDosage = sc.nextLine();
    if (newDosage.isEmpty()) newDosage = suggestedDosage;
    
    String newDuration;
    while (true) {
        System.out.print("New Duration (e.g., 5 days): ");
        newDuration = sc.nextLine();
        if (!newDuration.trim().isEmpty()) break;
        System.out.println("Duration cannot be empty.");
    }

    LocalDate newTreatmentDate;
    while (true) {
        System.out.print("New Date (YYYY-MM-DD) [Enter for today]: ");
        String newInputDate = sc.nextLine();
        if (newInputDate.isEmpty()) {
            newTreatmentDate = LocalDate.now();
            break;
        }
        try {
            newTreatmentDate = LocalDate.parse(newInputDate);
            break;
        } catch (Exception e) {
            System.out.println("Invalid date format. Try again.");
        }
    }
    
System.out.print("New Consultation Fee (e.g. 50.00): ");
double newConsultationFee;
try {
    newConsultationFee = Double.parseDouble(sc.nextLine());
} catch (NumberFormatException e) {
    System.out.println("Invalid consultation fee input.");
    return;
}

        boolean success = manager.updateSpecificTreatment(uid, recordNumber,
                newDiagnosis, newDoctorID, newMedicineID, newDosage, newDuration,newTreatmentDate ,newConsultationFee);

        System.out.println(success ? "Treatment updated successfully." : "Update failed.");
    }
    
    private void sortByDate() {
    ListInterface<Treatment> treatmentList = manager.getTreatmentList();
    int n = treatmentList.getLength();

    for (int i = 1; i < n; i++) {
        for (int j = 1; j <= n - i; j++) {
            Treatment t1 = treatmentList.getEntry(j);
            Treatment t2 = treatmentList.getEntry(j + 1);
            if (t1.getTreatmentDate().isAfter(t2.getTreatmentDate())) {
                treatmentList.replace(j, t2);
                treatmentList.replace(j + 1, t1);
            }
        }
    }

    System.out.println("Treatment list sorted by date in ascending order.");
    displayAllTreatments();
}

    private void summaryReportsMenu() {
    System.out.println("\n--- Treatment Summary Reports ---");
    System.out.println("1. Total Treatments per Patient");
    System.out.println("2. Total Revenue from Treatments");
    System.out.println("3. Most Common Diagnosis");
    System.out.println("4. Top Prescribed Medicine");
    System.out.print("Enter choice: ");
    int choice = sc.nextInt();
    sc.nextLine();
    switch (choice) {
        case 1 -> manager.displayTotalTreatmentsPerPatient();
        case 2 -> manager.displayTotalRevenue();
        case 3 -> manager.displayMostCommonDiagnosis();
        case 4 -> manager.displayTopPrescribedMedicine(pharmacyManager);
        
        default -> System.out.println("Invalid choice.");
    }
}
}

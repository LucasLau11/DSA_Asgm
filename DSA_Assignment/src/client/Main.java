package client;

import boundary.*; 
import control.PharmacyManager;
import control.PatientManager;
import java.util.Scanner;
import entity.Patient;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Create shared managers for the whole program
        PharmacyManager pharmacyManager = new PharmacyManager();
        PatientManager patientManager = new PatientManager(); 
        
        // Pass the same managers to all UIs so they share the same data
        TreatmentUI treatmentUI = new TreatmentUI(pharmacyManager, patientManager);
        PharmacyUI pharmacyUI = new PharmacyUI(pharmacyManager);
        PatientUI patientUI = new PatientUI(patientManager); // Pass shared PatientManager
        
        String choice;
        
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Treatment Management");
            System.out.println("2. Pharmacy Management");
            System.out.println("3. Patient Management");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1" -> {
                    System.out.println("\n=== Treatment Management ===");
                    System.out.println("1. Nurse/Admin - Full Treatment Management");
                    System.out.println("2. Patient - View My Treatments");
                    System.out.println("3. Process Next Patient from Queue");
                    System.out.print("Enter your role: ");
                    String roleChoice = scanner.nextLine().trim();

                    if (roleChoice.equals("1")) {
                        treatmentUI.menu(); // Full admin menu
                    } else if (roleChoice.equals("2")) {
                        treatmentUI.patientMenu(); // View only own treatment
                    } else if (roleChoice.equals("3")) {
                        // NEW: Process next patient from queue
                        processNextPatientFromQueue(patientManager, treatmentUI);
                    } else {
                        System.out.println("Invalid role choice.");
                    }
                }

                case "2" -> pharmacyUI.menu(); // Pharmacy menu
                case "3" -> patientUI.start(); // Patient management
                case "0" -> {
                    System.out.println("Exiting program...");
                    return; // Exit program
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void processNextPatientFromQueue(PatientManager patientManager, TreatmentUI treatmentUI) {
    Patient nextPatient = patientManager.peekNextPatient();
    if (nextPatient == null) {
        System.out.println("No patients in queue.");
        return;
    }
    
    System.out.println("\n=== PROCESSING NEXT PATIENT ===");
    System.out.println("Patient ID: " + nextPatient.getPatientId());
    System.out.println("Name: " + nextPatient.getName());
    System.out.println("Age: " + nextPatient.getAge());
    
    String visitReason = patientManager.getPatientVisitReason(nextPatient.getPatientId());
    if (visitReason != null) {
        System.out.println("Visit Reason: " + visitReason);
    }
    
    System.out.print("Proceed with treatment? (y/n): ");
    Scanner sc = new Scanner(System.in);
    String proceed = sc.nextLine().trim().toLowerCase();
    
    if (proceed.equals("y") || proceed.equals("yes")) {
        // Call the treatment UI with pre-filled patient info
        treatmentUI.processSpecificPatient(nextPatient.getPatientId());
    } else {
        System.out.println("Patient remains in queue.");
    }
}
}
package client;

import entity.*;
import adt.*;
import boundary.*;
import control.*;
import java.time.*;
import java.util.*;
import java.util.Scanner;

public class TW_Main {

    enum BookStatus {
        BOOKED,
        AVAILABLE,
        BREAK
    }

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        // Create shared managers for the whole program
//        PharmacyManager pharmacyManager = new PharmacyManager();
//        PatientManager patientManager = new PatientManager();
//
//        // Pass the same managers to all UIs so they share the same data
//        TreatmentUI treatmentUI = new TreatmentUI(pharmacyManager, patientManager);
//        PharmacyUI pharmacyUI = new PharmacyUI(pharmacyManager);
//        PatientUI patientUI = new PatientUI(patientManager); // Pass shared PatientManager
//        Map<LocalDate, BookStatus[][]> timetablePerDate = new HashMap<>(); //declare a universal timetable for each date
//
//        MyLinkedList<Consultation> bookingList = BookingList.bookingList;
//        bookingList.add(new Consultation("C001", "P001", "30/08/2025", "9:00-10:00", "Clara Wong", 1, 1));
//        bookingList.add(new Consultation("C002", "P001", "02/09/2025", "8:00-9:00", "Bob Lee", 0, 0));
//        bookingList.add(new Consultation("C003", "P001", "02/09/2025", "13:00-14:00", "David Tan", 5, 1));
//
//        String choice;
//
//        while (true) {
//            System.out.println("\n=== Main Menu ===");
//            System.out.println("1. Treatment Management"); //now
//            System.out.println("2. Pharmacy Management");
//            System.out.println("3. Patient Management");
//            System.out.println("0. Exit");
//            System.out.print("Enter your choice: ");
//            choice = scanner.nextLine().trim();
//
//            switch (choice) {
//                case "1" -> {
//                    System.out.println("\n=== Treatment Management ===");
//                    System.out.println("1. Nurse/Admin");
////                    System.out.println("2. Patient"); //done
//                    System.out.print("Enter your role: ");
//                    String roleChoice = scanner.nextLine().trim();
//
//                    if (roleChoice.equals("1")) {
//                        treatmentUI.menu(timetablePerDate); // Full admin menu
//                    } else if (roleChoice.equals("2")) {
////                        treatmentUI.patientMenu(); // DONE
//                    } else {
//                        System.out.println("Invalid role choice.");
//                    }
//                }
//                case "2" ->
//                    pharmacyUI.menu(); // Pharmacy menu
//                case "3" ->
//                    patientUI.start(); // Patient management
//                case "0" -> {
//                    System.out.println("Exiting program...");
//                    return; // Exit program
//                }
//                default ->
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        }
//    }
}

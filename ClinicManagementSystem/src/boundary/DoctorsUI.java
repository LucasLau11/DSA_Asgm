/*@author ng jim shen*/
package boundary;

import control.DoctorManager;
import java.util.Scanner;

public class DoctorsUI {
    public static void main(String[] args) {
        DoctorManager dm = new DoctorManager();
        Scanner sc = new Scanner(System.in);

        int choice;
        do {
            System.out.println("\n--- Clinic Doctor Management ---");
            System.out.println("1. Add Doctor (Manual Input)");
            System.out.println("2. View Doctors");
            System.out.println("3. Edit Doctor");
            System.out.println("4. Delete Doctor");
            System.out.println("5. Search Doctor");
            System.out.println("6. Specialty Report");
            System.out.println("7. Doctor Directory Report");
            System.out.println("8. Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    dm.addDoctorManual();
                    break;
                case 2:
                    dm.listDoctors();
                    break;
                case 3:
                    dm.editDoctor();
                    break;
                case 4:
                    dm.deleteDoctor();
                    break;
                case 5:
                int searchChoice;
                do {
                    System.out.println("\n--- Search Doctor ---");
                    System.out.println("1. By Specialty");
                    System.out.println("2. By Language");
                    System.out.println("3. Back to Main Menu");
                    System.out.print("Enter choice: ");
                    searchChoice = sc.nextInt();
                    sc.nextLine(); // consume newline

                    switch (searchChoice) {
                        case 1:
                            dm.searchBySpecialty();
                            break;
                        case 2:
                            dm.searchByLanguage();
                            break;
                        case 3:
                            System.out.println("Returning to main menu...");
                            break;
                        default:
                            System.out.println("Invalid choice, try again.");
                    }
                } while (searchChoice != 3);
                break;

                case 6:
                    dm.generateSpecialtyReport();
                break;


                case 7:
                    dm.generateDoctorDirectory();
                    break;
                case 8:
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        } while (choice != 8);
    }
    
    public void docSearchUI() {
        DoctorManager dm = new DoctorManager();
        Scanner sc = new Scanner(System.in);
        
        int searchChoice;
        do {
            System.out.println("\n--- Search Doctor ---");
            System.out.println("1. By ID");
            System.out.println("2. By Specialty");
            System.out.println("3. By Language");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");
            searchChoice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (searchChoice) {
                case 1:
                    dm.searchBySpecialty();
                    break;
                case 2:
                    dm.searchByLanguage();
                    break;
                case 3:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        } while (searchChoice != 3);
    }
}

package boundary;

import adt.*;
import client.*;
import control.*;
import entity.*;
import java.util.*;
import utility.*;
import java.time.LocalDate;

public class MainMenuUI {

    public static final String RED = "\u001B[41m";
    public static final String RED_TEXT = "\u001B[31m";
    public static final String GREEN = "\u001B[42m";
    public static final String GREEN_TEXT = "\u001B[32m";
    public static final String BLUE_TEXT = "\u001B[34m";
    public static final String NO_COLOR = "\u001B[0m";

    private int choice;
    private String patientID;

    Map<LocalDate, BookStatus[][]> timetablePerDate = new HashMap<>(); //declare a universal timetable for each date

    ManageAppointmentUser manageAppointment = new ManageAppointmentUser();
    MakeAppointment makeAppointment = new MakeAppointment();
    PharmacyManager pharmacyManager = new PharmacyManager();
    PatientManager patientManager = new PatientManager();
    DoctorManager doctorManager = new DoctorManager();

    AppointmentUI appointmentUI = new AppointmentUI(doctorManager);
    TreatmentUI treatmentUI = new TreatmentUI(pharmacyManager, patientManager);
    PharmacyUI pharmacyUI = new PharmacyUI(pharmacyManager);
    PatientUI patientUI = new PatientUI(patientManager); // Pass shared PatientManager
    DoctorsUI docUI = new DoctorsUI();

    Scanner scan = new Scanner(System.in);

    enum BookStatus {
        BOOKED,
        AVAILABLE,
        BREAK
    }

    public void MainMenuUserUI(String ID) {

        String patientID = ID;
        while (true) {

            //done
            System.out.println("\n<< ======================================================= USER MENU ======================================================= >>\n");

            System.out.printf("%-45s %-45s %-45s%n",
                    "<< ===== Patient Section ===== >>",
                    "<< ===== Consultation Section ===== >>",
                    "<< ===== Treatment Section ===== >>");

            System.out.printf("%-45s %-45s %-45s%n",
                    "1. Register as New Patient / Add New Visit",
                    "5. Make An Appointment",
                    "8. Display All Treatment");

            System.out.printf("%-45s %-45s %-45s%n",
                    "2. View My Medical Records",
                    "6. Check Booked Appointment(s)",
                    "");

            System.out.printf("%-45s %-45s %-45s%n",
                    "3. Check My Queue Position",
                    "7. Check Doctor(s) Timetable",
                    "");

            System.out.printf("%-45s %-45s %-45s%n",
                    "4. View Queue Status",
                    "",
                    "");

            System.out.println("\n    0. LOG OUT");
            System.out.print("\nEnter your choice: ");
            choice = scan.nextInt();

            switch (choice) {

                //Patient
                case 1:
                    scan.nextLine(); //clear buffer
                    patientUI.registerPatientOrAddVisit();
                    break;
                case 2:
                    scan.nextLine(); //clear buffer
                    patientUI.getViewRec();
                    break;
                case 3:
                    scan.nextLine(); //clear buffer
                    patientUI.getMyCheckQueueP();
                    break;
                case 4:
                    scan.nextLine(); //clear buffer
                    patientUI.getViewQueueS();
                    break;

                //Consultation
                case 5:
                    scan.nextLine(); //clear buffer
                    makeAppointment.book(patientID, timetablePerDate);

                    break;
                case 6:
                    scan.nextLine(); //clear buffer
                    manageAppointment.displayAllBookings(patientID, timetablePerDate);

                    break;
                case 7:
                    scan.nextLine(); //clear buffer
                    manageAppointment.showTimetable(patientID, timetablePerDate);
                    break;

                //Treatment
                case 8:
                    scan.nextLine(); //clear buffer

                    treatmentUI.patientMenu();

                    break;

                //Quit
                case 0:
                    System.out.println(BLUE_TEXT + "\n=====================================================" + NO_COLOR);
                    System.out.println(BLUE_TEXT + "|| Logged out successfully! We awaits your return! ||" + NO_COLOR);
                    System.out.println(BLUE_TEXT + "=====================================================\n" + NO_COLOR);
                    System.out.print("Please press ENTER to proceed... ");
                    scan.nextLine();
                    scan.nextLine();
                    ClearScreen.clearScreen();
                    return;
                default:
                    System.out.println("\n" + RED_TEXT + ">> Invalid option. Please Try Again. <<\n" + NO_COLOR);
                    System.out.print("Please press ENTER to proceed... ");
                    scan.nextLine();
                    scan.nextLine();

                    ClearScreen.clearScreen();
                    return;
            }
        }
    }

    public void MainMenuAdminUI() {

        while (true) {

            //done
            System.out.println("\n<< ====================================================================================== ADMIN MENU ====================================================================================== >>\n");
            System.out.printf("%-40s%-35s%-40s%-40s%-40s%n",
                    "<< Patient Section >>",
                    "<< Doctor Section >>",
                    "<< Consultation Section >>",
                    "<< Pharmacy Section >>",
                    "<< Treatment Section >>");

// Menu options line by line
            System.out.printf("%-40s%-35s%-40s%-40s%-40s%n",
                    "1. Register New Patient", "8. Add Doctor", "15. Block a Time Slot",
                    "18. Display All Medicines", "24. Display All Treatments");
            System.out.printf("%-40s%-35s%-40s%-40s%-40s%n",
                    "2. View All Patients", "9. View Doctor(s)", "16. Check Recent/Upcoming Schedule",
                    "19. Add Medicine", "25. Add Treatment");
            System.out.printf("%-40s%-35s%-40s%-40s%-40s%n",
                    "3. Update Patient Information", "10. Edit Doctor", "17. Consultation Summary Report",
                    "20. Search by Medicine", "26. Search by Patient ID");
            System.out.printf("%-40s%-35s%-40s%-40s%-40s%n",
                    "4. Delete Patient Information", "11. Delete Doctor", "",
                    "21. Remove Medicine", "27. Remove by Treatment ID");
            System.out.printf("%-40s%-35s%-40s%-40s%-40s%n",
                    "5. Search Patient", "12. Search Doctor", "",
                    "22. Update Medicine", "28. Update Treatment");
            System.out.printf("%-40s%-35s%-40s%-40s%-40s%n",
                    "6. View Queue", "13. Specialty Report", "",
                    "23. Pharmacy Summary Report", "29. Sort by Date");
            System.out.printf("%-40s%-35s%-40s%-40s%-40s%n",
                    "7. Statistic Report", "14. Doctor Directory Report", "",
                    "", "30. Treatment Summary Reports");

            System.out.println("\n    0. LOG OUT");
            System.out.print("\nEnter your choice: ");
            choice = scan.nextInt();

            switch (choice) {

                //Patient
                case 1:
                    patientUI.getRegNewP();
                    break;
                case 2:
                    patientUI.getViewAllP();
                    break;
                case 3:
                    patientUI.getUpdPM();
                    break;
                case 4:
                    patientUI.getDelP();
                    break;
                case 5:
                    patientUI.viewPD();
                    break;
                case 6:
                    patientUI.viewQP();
                    break;
                case 7:
                    patientUI.getStatRep();
                    break;

                //Doctor
                case 8:
                    doctorManager.addDoctorManual();
                    break;
                case 9:
                    doctorManager.listDoctors();
                    break;
                case 10:
                    doctorManager.editDoctor();
                    break;
                case 11:
                    doctorManager.deleteDoctor();
                    break;
                case 12:
                    docUI.docSearchUI();
                    break;
                case 13:
                    doctorManager.generateSpecialtyReport();
                    break;
                case 14:
                    doctorManager.generateDoctorDirectory();
                    break;

                //Consultation
                case 15:
                    makeAppointment.bookAdmin(timetablePerDate);
                    break;
                case 16:
                    manageAppointment.displayAdmin(timetablePerDate);
                    break;
                case 17:
                    //WIP
                    appointmentUI.getConRep();
                    break;

                //Pharmacy
                case 18:
                    pharmacyUI.getAllMeds();
                    break;
                case 19:
                    pharmacyUI.getAddMeds();
                    break;
                case 20:
                    pharmacyUI.getSearchMeds();
                    break;
                case 21:
                    pharmacyUI.getRemMeds();
                    break;
                case 22:
                    pharmacyUI.getUpdMeds();
                    break;
                case 23:
                    pharmacyUI.getSumRep();
                    break;

                //Treatment
                case 24:
                    treatmentUI.displayAllTreatments();
                    break;
                case 25:
                    treatmentUI.callAdd(timetablePerDate);
                    break;
                case 26:
                    treatmentUI.callSearch();
                    break;
                case 27:
                    treatmentUI.callRemove();
                    break;
                case 28:
                    treatmentUI.callUpd();
                    break;
                case 29:
                    treatmentUI.callSort();
                    break;
                case 30:
                    treatmentUI.callSumRep();
                    break;

                //Quit
                case 0:
                    System.out.println(BLUE_TEXT + "\n=====================================================" + NO_COLOR);
                    System.out.println(BLUE_TEXT + "|| Logged out successfully! We awaits your return! ||" + NO_COLOR);
                    System.out.println(BLUE_TEXT + "=====================================================\n" + NO_COLOR);
                    System.out.print("Please press ENTER to proceed... ");
                    scan.nextLine();
                    scan.nextLine();
                    ClearScreen.clearScreen();
                    return;
                default:
                    System.out.println("\n" + RED_TEXT + ">> Invalid option. Please Try Again. <<\n" + NO_COLOR);
                    System.out.print("Please press ENTER to proceed... ");
                    scan.nextLine();
                    scan.nextLine();

                    ClearScreen.clearScreen();
                    return;
            }
        }
    }

}

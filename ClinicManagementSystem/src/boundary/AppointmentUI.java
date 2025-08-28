package boundary;

import entity.Consultation;
import utility.ClearScreen;
import control.DoctorManager;
import control.BookingList;
import control.ManageAppointmentUser;
import control.MakeAppointment;
import control.Report_Consultation;
import adt.*;
import java.util.*;

public class AppointmentUI {

    public static final String RED = "\u001B[41m";
    public static final String RED_TEXT = "\u001B[31m";
    public static final String GREEN = "\u001B[42m";
    public static final String GREEN_TEXT = "\u001B[32m";
    public static final String BLUE_TEXT = "\u001B[34m";
    public static final String NO_COLOR = "\u001B[0m";

    private MyLinkedList<Consultation> bookingList = BookingList.bookingList;
    private DoctorManager doctorManager;

    // Constructor that takes DoctorManager
    public AppointmentUI(DoctorManager doctorManager) {
        this.doctorManager = doctorManager;
    }

    // Default constructor for backward compatibility
    public AppointmentUI() {
        this.doctorManager = new DoctorManager();
    }

    public void appointmentUI(String patientID, Map timetablePerDate) {
        Scanner scan = new Scanner(System.in);
        MakeAppointment makeAppointment = new MakeAppointment(doctorManager);
        ManageAppointmentUser manageAppointment = new ManageAppointmentUser();

        OUTER:
        while (true) {
            System.out.println("===================");
            System.out.println(" Consultation Mode ");
            System.out.println("===================");
            System.out.println("1. Make An Appointment");
            System.out.println("2. Check Booked Appointment(s)");
            System.out.println("3. Check Doctor(s) Timetable");
            System.out.println("4. Exit");
            System.out.print("\nChoose an option (1/2/..) > ");
            int option = scan.nextInt();
            switch (option) {
                case 1:
                    makeAppointment.book(patientID, timetablePerDate);

                    ClearScreen.clearScreen();
                    break;
                case 2:
                    manageAppointment.displayAllBookings(patientID, timetablePerDate);

                    ClearScreen.clearScreen();
                    break;
                case 3:
                    manageAppointment.showTimetable(patientID, timetablePerDate);

                    System.out.println("\n");

                    ClearScreen.clearScreen();
                    break;
                case 4:
                    System.out.println(BLUE_TEXT + "\n=======================================================================" + NO_COLOR);
                    System.out.println(BLUE_TEXT + "|| Thanks for booking with us! Please proceed back to our Home Menu! ||" + NO_COLOR);
                    System.out.println(BLUE_TEXT + "=======================================================================\n" + NO_COLOR);
                    System.out.print("Please press ENTER to proceed... ");
                    scan.nextLine();
                    scan.nextLine();
                    System.out.println("\n");
                    break OUTER;
                default:
                    System.out.println("\n" + RED_TEXT + ">> Invalid option. Please Try Again. <<\n" + NO_COLOR);
                    System.out.print("Please press ENTER to proceed... ");
                    scan.nextLine();

                    ClearScreen.clearScreen();
                    break;
            }
        }
    }

    public void removeAppointmentUI(String patientID, Map timetablePerDate) {

        ManageAppointmentUser m = new ManageAppointmentUser();
        Scanner scan = new Scanner(System.in);

        System.out.println("1. Cancel an Appointment");
        System.out.println("2. Back");
        System.out.print("Please choose an option (1/2) > ");

        int option = scan.nextInt();
        switch (option) {
            case 1:
                System.out.print("\nWhich appointment you'd like to cancel? (1/2/..) > ");
                int delete = scan.nextInt();

                if (delete >= 1 && delete <= bookingList.getLength()) {

                    Consultation c = bookingList.getEntry(delete);
                    String chosenDate = c.getChosenDate();
                    System.out.println("\n" + BLUE_TEXT + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + NO_COLOR
                            + "\n     Consultation ID > " + c.getConsultationID()
                            + "\n     Patient ID      > " + c.getPatientID()
                            + "\n     Date            > " + c.getChosenDate()
                            + "\n     Time            > " + c.getChosenTime()
                            + "\n     Doctor          > " + c.getChosenDr()
                            + "\n" + BLUE_TEXT + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + NO_COLOR);

                    System.out.print("\nAre you sure you wanted to cancel this booking? (Y/N) > ");
                    String confirmDel = scan.next();
                    switch (confirmDel) {
                        case "Y":
                        case "y":
                            m.removeAppointment(delete, timetablePerDate, chosenDate);

                            scan.nextLine();
                            System.out.println("\n" + RED_TEXT + ">> The appointment has been cancelled!\n" + NO_COLOR);
                            System.out.print("Press ENTER to proceed...");
                            scan.nextLine();

                            break;
                        case "N":
                        case "n":
                            System.out.println("");
                            appointmentUI(patientID, timetablePerDate);
                            break;
                        default:
                            System.out.println("\nInvalid Choice. Please Try Again.");
                    }
                    break;
                } else {
                    System.out.println("\n" + RED_TEXT + ">> No booking was Found. Please Try Again Later. << \n" + NO_COLOR);
                    System.out.print("Press ENTER to proceed...");
                    scan.nextLine();
                }
                break;
            case 2:
                break;
            default:
                System.out.println("\nInvalid Choice. Please Try Again.");
        }

    }

    public void appointmentUIAdmin(Map timetablePerDate) {
        Scanner scan = new Scanner(System.in);
        MakeAppointment makeAppointment = new MakeAppointment(doctorManager);
        ManageAppointmentUser manageAppointment = new ManageAppointmentUser();

        OUTER:
        while (true) {
            System.out.println("===================");
            System.out.println(" Consultation Mode ");
            System.out.println("===================");
            System.out.println("1. Block a Time Slot");
            System.out.println("2. Check Upcoming Schedule");
            System.out.println("3. Generate Summary Report");
            System.out.println("4. Exit");
            System.out.print("\nChoose an option (1/2/..) > ");
            int option = scan.nextInt();
            switch (option) {
                case 1:

                    makeAppointment.bookAdmin(timetablePerDate);

                    ClearScreen.clearScreen();
                    break;
                case 2:
                    manageAppointment.displayAdmin(timetablePerDate);
                    ClearScreen.clearScreen();
                    break;
                case 3:

                    generateConsultationReport();
                    ClearScreen.clearScreen();
                    break;
                case 4:
                    System.out.println(BLUE_TEXT + "\n====================================================================" + NO_COLOR);
                    System.out.println(BLUE_TEXT + "|| Thanks for your service! Please proceed back to our Home Menu! ||" + NO_COLOR);
                    System.out.println(BLUE_TEXT + "====================================================================\n" + NO_COLOR);
                    System.out.print("Please press ENTER to proceed... ");
                    scan.nextLine();
                    scan.nextLine();
                    System.out.println("\n");
                    break OUTER;
                default:
                    System.out.println("\n" + RED_TEXT + ">> Invalid option. Please Try Again. <<\n" + NO_COLOR);
                    System.out.print("Please press ENTER to proceed... ");
                    scan.nextLine();

                    ClearScreen.clearScreen();
                    break;
            }
        }
    }

    public void getConRep(){
        generateConsultationReport();
    }
    
    private void generateConsultationReport() {

        Report_Consultation report = new Report_Consultation(bookingList);
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Appointment(s) per Doctor");
            System.out.println("2. Appointment(s) per Patient");
            System.out.println("3. Peak Time Slot");
            System.out.println("4. Most Popular Doctor");
            System.out.println("5. Daily Appointment Count");
            System.out.println("\n6. Display all Reports");
            System.out.println("0. Back");
            System.out.print("\nChoose an option (1/2/..) > ");
            int option = scan.nextInt();
            switch (option) {
                case 1:
                    report.reportAppointmentsByDoctor();
                    break;
                case 2:
                    report.reportAppointmentsByPatient();
                    break;
                case 3:
                    report.reportPeakTimeSlots();
                    break;
                case 4:
                    report.reportMostPopularDoctor();
                    break;
                case 5:
                    report.reportDailyAppointments();
                    break;
                case 6:
                    report.reportAppointmentsByDoctor();
                    report.reportAppointmentsByPatient();
                    report.reportPeakTimeSlots();
                    report.reportMostPopularDoctor();
                    report.reportDailyAppointments();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\n" + RED_TEXT + ">> Invalid option. Please Try Again. <<\n" + NO_COLOR);
                    System.out.print("Please press ENTER to proceed... ");
                    scan.nextLine();

                    break;
            }

        }
    }
}

package client;

import boundary.AppointmentUI;
import control.DoctorManager;
import java.time.LocalDate;
import java.util.*;

public class DSA_Asgmt {

    public static final String RED = "\u001B[41m";
    public static final String RED_TEXT = "\u001B[31m";
    public static final String GREEN = "\u001B[42m";
    public static final String GREEN_TEXT = "\u001B[32m";
    public static final String BLUE_TEXT = "\u001B[34m";
    public static final String NO_COLOR = "\u001B[0m";

    enum BookStatus {
        BOOKED,
        AVAILABLE,
        BREAK
    }

//    public static void main(String[] args) {
//
//        DoctorManager doctorManager = new DoctorManager();
//        AppointmentUI appointmentUI = new AppointmentUI(doctorManager);
//        Map<LocalDate, BookStatus[][]> timetablePerDate = new HashMap<>(); //declare a universal timetable for each date
//
//        Scanner scan = new Scanner(System.in);
//
//        while (true) {
//            System.out.println("1. User");
//            System.out.println("2. Admin");
//            System.out.println("3. Exit");
//            System.out.print("\nChoose Role > ");
//            int roleOpt = scan.nextInt();
//
//            switch (roleOpt) {
//                case 1:
//                    String patientID = "P001";
//                    //call user side consultation UI
//                    System.out.print("\n");
//                    appointmentUI.appointmentUI(patientID, timetablePerDate);
//                    break;
//                case 2:
//                    //call admin side consultation UI
//                    appointmentUI.appointmentUIAdmin(timetablePerDate);
//                    break;
//                case 3:
//                    System.out.println("\nGoodbye bozo\n");
//                    return;
//                default:
//                    System.out.println("\nInvalid Choice..\n");
//                    break;
//            }
//        }
//    }
}

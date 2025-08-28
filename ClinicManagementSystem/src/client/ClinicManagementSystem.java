package client;

import java.util.*;
import entity.*;
import adt.*;
import boundary.*;
import control.*;
import utility.ClearScreen;

public class ClinicManagementSystem {

    public static final String RED_TEXT = "\u001B[31m";
    public static final String BLUE_TEXT = "\u001B[34m";
    public static final String NO_COLOR = "\u001B[0m";

    public static void main(String[] args) {
        MainMenuUI m_menu = new MainMenuUI();
        MakeAppointment makeAppointment = new MakeAppointment();

        MyLinkedList<Consultation> bookingList = BookingList.bookingList;

        Consultation c1 = new Consultation("C011", "P002", "21/10/2025", "9:00-10:00", "Clara Wong", 1, 1);
        Consultation c2 = new Consultation("C007", "P004", "02/09/2025", "8:00-9:00", "Bob Lee", 0, 0);
        Consultation c3 = new Consultation("C023", "P003", "02/09/2025", "13:00-14:00", "David Tan", 5, 1);
        Consultation c4 = new Consultation("C004", "P001", "20/11/2025", "16:00-17:00", "Bob Lee", 8, 0);

        makeAppointment.addBooking(c1);
        makeAppointment.addBooking(c2);
        makeAppointment.addBooking(c3);
        makeAppointment.addBooking(c4);

        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("<< ==== LOG IN ==== >>");
            System.out.println("1. User");
            System.out.println("2. Admin");
            System.out.println("0. Exit");
            System.out.print("\nChoose Role > ");
            String input = scan.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println("Invalid input. Please enter a number only.");
                continue;
            }

            int roleOpt = Integer.parseInt(input);
            
            switch (roleOpt) {
                case 1:

                    System.out.print("\nEnter your Patient ID (format P001): ");
                    String patientID = scan.nextLine();

                    if (!patientID.matches("P\\d{3}")) {
                        System.out.println("\n>> Invalid Patient ID format! Please try again. <<\n");
                        System.out.print("Press ENTER to proceed... ");
                        scan.nextLine();

                        ClearScreen.clearScreen();
                        continue;
                    } else {

                        m_menu.MainMenuUserUI(patientID);
                        break;
                    }
                case 2:
                    m_menu.MainMenuAdminUI();
                    break;
                case 0:
                    System.out.println(BLUE_TEXT + "\n========================================================" + NO_COLOR);
                    System.out.println(BLUE_TEXT + "|| Thanks for using our System! Hope to see you soon! ||" + NO_COLOR);
                    System.out.println(BLUE_TEXT + "========================================================\n" + NO_COLOR);
                    return;
                default:
                    System.out.println("\n" + RED_TEXT + ">> Invalid Choice <<\n" + NO_COLOR);
                    break;
            }
        }
    }

}
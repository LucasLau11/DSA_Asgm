package control;

import boundary.AppointmentUI;
import entity.Consultation;
import utility.calendar;
import adt.*;
import control.MakeAppointment.BookStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.YearMonth;

public class ManageAppointmentUser {

    public static final String RED = "\u001B[41m";
    public static final String RED_TEXT = "\u001B[31m";
    public static final String GREEN = "\u001B[42m";
    public static final String GREEN_TEXT = "\u001B[32m";
    public static final String BLUE_TEXT = "\u001B[34m";
    public static final String NO_COLOR = "\u001B[0m";

    private MyLinkedList<Consultation> bookingList = BookingList.bookingList;
    AppointmentUI UI = new AppointmentUI();

    public ManageAppointmentUser() {
    }

    // Constructor takes the shared booking list
    public ManageAppointmentUser(MyLinkedList<Consultation> bookingList) {
        this.bookingList = bookingList;
    }

    //remove entry from list
    public void removeAppointment(int delete, Map timetablePerDate, String chosenDate) {

        Consultation c = bookingList.getEntry(delete);
        BookStatus[][] bookingStatus;

        // convert formmated date back to ori LocalDate format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate originalDate = LocalDate.parse(chosenDate, formatter);

        // make the booked time slot available back after cancel booking
        bookingStatus = (BookStatus[][]) timetablePerDate.get(originalDate);

        int row = c.getRow();
        int col = c.getCol();
        bookingStatus[row][col] = BookStatus.AVAILABLE;

        // bye to chosen entry
        bookingList.remove(delete);

    }

    public void displayAllBookings(String patientID, Map timetablePerDate) {

        Scanner scan = new Scanner(System.in);

        //need modify based on patientID
        if (bookingList == null || bookingList.isEmpty()) {
            System.out.printf("\n%-5s %-18s %-15s %-15s %-15s %-15s%n",
                    "No.", "Consultation ID", "Patient ID", "Date", "Time", "Doctor");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("                                 No bookings yet.                                    \n");
            System.out.print("Press ENTER to proceed...");
            scan.nextLine();
        } else {
            System.out.printf("\n%-5s %-18s %-15s %-15s %-15s %-15s%n",
                    "No.", "Consultation ID", "Patient ID", "Date", "Time", "Doctor");
            System.out.println("-------------------------------------------------------------------------------------");

            int count = 0; // track number of matches
            for (int i = 1; i <= bookingList.getLength(); i++) {
                Consultation c = bookingList.getEntry(i);
                if (c.getPatientID().equals(patientID)) {
                    count++;
                    System.out.printf("%-5d %-18s %-15s %-15s %-15s %-15s%n",
                            count, c.getConsultationID(), c.getPatientID(), c.getChosenDate(), c.getChosenTime(), c.getChosenDr());
                }
            }
            if (count == 0) {
                System.out.println("                                 No bookings yet.                                    \n");
                System.out.print("Press ENTER to proceed...");
                scan.nextLine();
                return;
            }

            System.out.println("");
            UI.removeAppointmentUI(patientID, timetablePerDate);
        }
    }

    public void displayAdmin(Map timetablePerDate) {

        Scanner scan = new Scanner(System.in);

        //need modify based on patientID
        if (bookingList == null || bookingList.isEmpty()) {
            System.out.printf("\n%-5s %-18s %-15s %-15s %-15s %-15s %-15s%n",
                    "No.", "Consultation ID", "Patient ID", "Date", "Time", "Doctor", "Reason");
            System.out.println("-----------------------------------------------------------------------------------------------------");
            System.out.println("                                 No bookings yet.                                    \n");
            System.out.print("Press ENTER to proceed...");
            scan.nextLine();
        } else {
            System.out.printf("\n%-5s %-18s %-15s %-15s %-15s %-15s %-15s%n",
                    "No.", "Consultation ID", "Patient ID", "Date", "Time", "Doctor", "Reason");
            System.out.println("-----------------------------------------------------------------------------------------------------");

            int count = 0; // track number of matches
            for (int i = 1; i <= bookingList.getLength(); i++) {
                Consultation c = bookingList.getEntry(i);

                if (c.getPatientID().equals("BLOCKED")) {
                    System.out.printf("%-5d %-18s " + RED_TEXT + "%-15s " + NO_COLOR + "%-15s %-15s %-15s %-15s%n",
                            count, c.getConsultationID(), c.getPatientID(), c.getChosenDate(), c.getChosenTime(), c.getChosenDr(), c.getReason());
                } else {
                    System.out.printf("%-5d %-18s %-15s %-15s %-15s %-15s %-15s%n",
                            count, c.getConsultationID(), c.getPatientID(), c.getChosenDate(), c.getChosenTime(), c.getChosenDr(), "Diagnosis");
                }
            }
            System.out.print("\nPress ENTER to proceed...");
            scan.nextLine();
        }
    }

    public void showTimetable(String patientID, Map timetablePerDate) {

        Scanner scan = new Scanner(System.in);
        YearMonth currentMonth = YearMonth.now();
        ManageAppointmentUser showAppointment = new ManageAppointmentUser(bookingList);
        MakeAppointment book = new MakeAppointment();
        AppointmentUI UI = new AppointmentUI();

        // Replace the hardcoded doctor assignment in ManageAppointmentUser.java with:
        String[] allDoctors = book.getAllDoctors(); // Your method call
        String[] doctors = book.getDoctorsForMonth(allDoctors, currentMonth); // You'll need to add this method there too

        while (true) {

            calendar.displayCalendar(currentMonth);

            // Get doctors for current month dynamically
            String[] monthlyDoctors = book.getDoctorsForMonth(allDoctors, currentMonth);

            // Display available doctors for the month
            System.out.print(BLUE_TEXT + ">> Doctors Available This Month: ");
            for (int i = 0; i < monthlyDoctors.length; i++) {
                if (i == monthlyDoctors.length - 1) {
                    System.out.print(monthlyDoctors[i]);
                } else {
                    System.out.print(monthlyDoctors[i] + ", ");
                }
            }
            System.out.println(" <<" + NO_COLOR + "\n");

            System.out.print("Enter 'next' or 'prev' to navigate through months, or enter a day (1-31) to choose a date > ");
            String input = scan.nextLine();

            if (input.equalsIgnoreCase("next")) {
                currentMonth = currentMonth.plusMonths(1);
            } else if (input.equalsIgnoreCase("prev")) {
                currentMonth = currentMonth.minusMonths(1);
            } else if (input.equalsIgnoreCase("back")) {
                break;
            } else {
                try {
                    int day = Integer.parseInt(input);
                    if (day >= 1 && day <= currentMonth.lengthOfMonth()) {
                        LocalDate selectedDate = currentMonth.atDay(day);
                        LocalDate today = LocalDate.now();

                        if (!selectedDate.isAfter(today)) {

                            System.out.println("\n" + RED_TEXT + ">> You cannot choose a date before or today! Please Try Again. <<" + NO_COLOR);
                            continue;
                        }

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String chosenDate = selectedDate.format(formatter);

                        System.out.println("\n" + BLUE_TEXT + ">> You selected > " + chosenDate + " <<" + NO_COLOR);

                        // simulated timetable based on selected month
                        if (currentMonth.getMonthValue() % 2 == 1) {
                            doctors = book.getDoctorsForMonth(allDoctors, currentMonth);
                        } else {
                            doctors = book.getDoctorsForMonth(allDoctors, currentMonth);
                        }

                        BookStatus[][] bookingStatus;
                        if (timetablePerDate.containsKey(selectedDate)) {
                            bookingStatus = (BookStatus[][]) timetablePerDate.get(selectedDate);
                        } else {
                            bookingStatus = new BookStatus[9][doctors.length];
                            for (int i = 0; i < 9; i++) {
                                for (int j = 0; j < doctors.length; j++) {
                                    bookingStatus[i][j] = BookStatus.AVAILABLE;
                                }
                            }
                            // Set lunch break
                            for (int j = 0; j < doctors.length; j++) {
                                bookingStatus[4][j] = BookStatus.BREAK;
                            }
                            timetablePerDate.put(selectedDate, bookingStatus);
                        }

                        MakeAppointment.printTimetable(doctors, bookingStatus);

                        System.out.print("\nPlease press ENTER to proceed... ");
                        scan.nextLine();

                        break;
                    } else {
                        System.out.println("\n" + RED_TEXT + ">> Invalid day. Please Try Again." + NO_COLOR);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n" + RED_TEXT + ">> Invalid input (next, prev or back). Please Try Again. <<" + NO_COLOR);
                }
            }
        }
    }
}
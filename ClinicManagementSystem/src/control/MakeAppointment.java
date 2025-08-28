package control;

import entity.Consultation;
import utility.calendar;
import adt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MakeAppointment {

    public static final String RED = "\u001B[41m"; // background
    public static final String RED_TEXT = "\u001B[31m"; //foreground
    public static final String YELLOW_TEXT = "\u001B[33m";
    public static final String GREEN = "\u001B[42m";
    public static final String GREEN_TEXT = "\u001B[32m";
    public static final String BLUE_TEXT = "\u001B[34m";
    public static final String NO_COLOR = "\u001B[0m";

    private static int consultationCounter = 0;

    enum BookStatus {
        BOOKED,
        AVAILABLE,
        BREAK
    }

    // Remove the instance variable bookingStatus since it's handled per date in the timetablePerDate map
    private MyLinkedList<Consultation> bookingList = BookingList.bookingList;
    private DoctorManager doctorManager;

    public MakeAppointment(DoctorManager doctorManager) {
        this.doctorManager = doctorManager;
    }

    public MakeAppointment() {
        this.doctorManager = new DoctorManager();
    }

    public void addBooking(Consultation c) {
        bookingList.add(c);
        // Note: bookingStatus updates should be handled in the calling method
        // since it's now managed per date in the timetablePerDate map
    }

    public MyLinkedList<Consultation> getBookingList() {
        return bookingList;
    }

    private String getConsultationID() {
        consultationCounter++;
        return String.format("C%03d", consultationCounter);
    }

    // timetable format row column lines
    public static void printLine(String corner, int width, int columns) {
        for (int i = 0; i < columns; i++) {
            System.out.print(corner + "-".repeat(width));
        }
        System.out.println(corner);
    }

    // doctor's timetable
    public static void printTimetable(String[] doctors, BookStatus[][] bookingStatus) {
        String[] periods = {
            "08:00-09:00",
            "09:00-10:00",
            "10:00-11:00",
            "11:00-12:00",
            "12:00-13:00",
            "13:00-14:00",
            "14:00-15:00",
            "15:00-16:00",
            "16:00-17:00"};
        int colWidth = 12;

        printLine("+", (colWidth + 1), doctors.length + 1);
        System.out.printf("| %-" + colWidth + "s", "Time/Doctor");
        for (String doctor : doctors) {
            System.out.printf("| %-" + colWidth + "s", doctor);
        }
        System.out.println("|");
        printLine("+", (colWidth + 1), doctors.length + 1);

        for (int i = 0; i < periods.length; i++) {
            System.out.printf("| %-" + colWidth + "s", periods[i]);
            for (int j = 0; j < doctors.length; j++) {
                BookStatus status = bookingStatus[i][j];
                String color, label;

                switch (status) {
                    case AVAILABLE:
                        color = GREEN;
                        label = "";
                        break;
                    case BOOKED:
                        color = RED;
                        label = "";
                        break;
                    case BREAK:
                        color = NO_COLOR;
                        label = " BREAK ";
                        break;
                    default:
                        color = NO_COLOR;
                        label = " TBA ";
                }
                System.out.printf("|%s%-" + (colWidth + 1) + "s%s", color, label, NO_COLOR);
            }
            System.out.println("|");
            printLine("+", (colWidth + 1), doctors.length + 1);
        }

        System.out.printf("%s      %s = %s", GREEN, NO_COLOR, "Available");
        System.out.print("  ");
        System.out.printf("%s      %s = %s\n", RED, NO_COLOR, "Booked");
    }

    //store record into Linked List adt
    public void storeBooking(String consultationID, String patientID, String chosenDate, String chosenTime, String chosenDr, int row, int col) {
        Consultation booking = new Consultation(consultationID, patientID, chosenDate, chosenTime, chosenDr, row, col);
        bookingList.add(booking);
        System.out.println(BLUE_TEXT + ">>      Your Appointment on " + chosenDate + " at " + chosenTime + " with " + chosenDr + " has been Confirmed!     <<" + NO_COLOR);
    }

    public void storeAdminBlock(String consultationID, String chosenDate, String chosenTime, String chosenDr, String reason, int row, int col) {
        // Use "ADMIN_BLOCK" as patientID to distinguish from regular appointments
        // Store the reason in a comment or create a new field
        Consultation adminBlock = new Consultation(consultationID, "BLOCKED", chosenDate, chosenTime, chosenDr, reason, row, col);
        bookingList.add(adminBlock);
        System.out.println(RED_TEXT + ">> Time slot for " + chosenDr + " on " + chosenDate + " at " + chosenTime + " has been blocked! <<" + NO_COLOR);
    }

    public String[] getAllDoctors() {
        return doctorManager.getAllDoctorNames();
    }

    public String[] getDoctorsForMonth(YearMonth currentMonth) {
        String[] allDoctors = getAllDoctors();
        return getDoctorsForMonth(allDoctors, currentMonth); // Use your existing method
    }

    public String[] getDoctorsForMonth(String[] allDoctors, YearMonth currentMonth) {
        List<String> oddIndexDoctors = new ArrayList<>();
        List<String> evenIndexDoctors = new ArrayList<>();

        // Separate doctors based on their array indices
        for (int i = 0; i < allDoctors.length; i++) {
            if (i % 2 == 0) {  // Even indices (0, 2, 4...)
                evenIndexDoctors.add(allDoctors[i]);
            } else {  // Odd indices (1, 3, 5...)
                oddIndexDoctors.add(allDoctors[i]);
            }
        }

        // Return doctors based on month number
        if (currentMonth.getMonthValue() % 2 == 1) {  // Odd months
            return oddIndexDoctors.toArray(new String[0]);
        } else {  // Even months
            return evenIndexDoctors.toArray(new String[0]);
        }
    }

    public void book(String patientID, Map timetablePerDate) {

        Scanner scan = new Scanner(System.in);
        YearMonth currentMonth = YearMonth.now();
        ManageAppointmentUser showAppointment = new ManageAppointmentUser(bookingList);

        String[] allDoctors = getAllDoctors();

        // get today's month
        while (true) {

            System.out.println(BLUE_TEXT + "\n===================================================" + NO_COLOR);
            System.out.println(BLUE_TEXT + " ** Appointments must be made 1 day in advance! **" + NO_COLOR);
            System.out.println(BLUE_TEXT + "===================================================" + NO_COLOR);

            calendar.displayCalendar(currentMonth);

            // Get doctors for current month dynamically
            String[] monthlyDoctors = getDoctorsForMonth(allDoctors, currentMonth);

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
                            monthlyDoctors = getDoctorsForMonth(allDoctors, currentMonth);
                        } else {
                            monthlyDoctors = getDoctorsForMonth(allDoctors, currentMonth);
                        }

                        BookStatus[][] bookingStatus;
                        if (timetablePerDate.containsKey(selectedDate)) {
                            bookingStatus = (BookStatus[][]) timetablePerDate.get(selectedDate);
                        } else {
                            bookingStatus = new BookStatus[9][monthlyDoctors.length];
                            for (int i = 0; i < 9; i++) {
                                for (int j = 0; j < monthlyDoctors.length; j++) {
                                    bookingStatus[i][j] = BookStatus.AVAILABLE;
                                }
                            }
                            // Set lunch break
                            for (int j = 0; j < monthlyDoctors.length; j++) {
                                bookingStatus[4][j] = BookStatus.BREAK;
                            }
                            timetablePerDate.put(selectedDate, bookingStatus);
                        }

                        printTimetable(monthlyDoctors, bookingStatus);

                        // Let user choose an appointment by selecting row and column
                        System.out.println("\nSelect an appointment:");
                        System.out.print("Enter time slot row number (1-9) > ");
                        int row = Integer.parseInt(scan.nextLine());
                        row -= 1;

                        System.out.print("Enter doctor column number (1-" + monthlyDoctors.length + ") > ");
                        int col = Integer.parseInt(scan.nextLine());
                        col -= 1;

                        if (row < 0 || row >= 9 || col < 0 || col >= monthlyDoctors.length) {
                            System.out.println("\n\u001B[31m>> Invalid row or column number. <<\n" + NO_COLOR);

                            System.out.print("Please press ENTER to proceed... ");
                            scan.nextLine();
                            return;
                        }

                        // Check if already booked or not available
                        BookStatus status = bookingStatus[row][col];
                        if (status != BookStatus.AVAILABLE || status == BookStatus.BREAK) {
                            System.out.println("\n\u001B[31m>> Slot is not available. Please choose another one. <<\n" + NO_COLOR);
                        } else {
                            // Mark as booked
                            bookingStatus[row][col] = BookStatus.BOOKED;

                            String[] periods = {
                                "08:00-09:00",
                                "09:00-10:00",
                                "10:00-11:00",
                                "11:00-12:00",
                                "12:00-13:00",
                                "13:00-14:00",
                                "14:00-15:00",
                                "15:00-16:00",
                                "16:00-17:00"
                            };

                            String chosenDoctor = monthlyDoctors[col];
                            String chosenTime = periods[row];

                            System.out.println("\n" + BLUE_TEXT + ">> Double Check the Chosen Appointment <<" + NO_COLOR);
                            System.out.println("         Doctor  : " + chosenDoctor);
                            System.out.println("         Date    : " + chosenDate);
                            System.out.println("         Time    : " + chosenTime);
                            System.out.println(BLUE_TEXT + ">> Double Check the Chosen Appointment <<\n" + NO_COLOR);

                            System.out.print("Confirm booking chosen appointment slot? (Y/N) > ");
                            String choose = scan.next();

                            if ("Y".equals(choose) || "y".equals(choose)) {

                                int totalSteps = 5; // 5 steps: 0, 20, 40, 60, 80, 100
                                System.out.println("\nProcessing.....");

                                // processing loading bar
                                for (int i = 0; i <= totalSteps; i++) {
                                    int percent = (i * 100) / totalSteps;
                                    String bar = "#".repeat(i * 10) + "-".repeat(50 - (i * 10));
                                    // 50 chars total, 10 chars per step (20% each)
                                    switch (percent) {
                                        case 0:
                                            System.out.println("[" + RED_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        case 20:
                                            System.out.println("[" + RED_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        case 40:
                                            System.out.println("[" + RED_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        case 60:
                                            System.out.println("[" + YELLOW_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        case 80:
                                            System.out.println("[" + YELLOW_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        case 100:
                                            System.out.println("[" + GREEN_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        default:
                                            break;
                                    }
                                    try {
                                        Thread.sleep(750); // 750(millisecond) => 0.75 seconds delay per step
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                    }
                                }

                                System.out.println("\n" + GREEN_TEXT + ">> Appointment confirmed! <<" + NO_COLOR);
                                System.out.println("   Doctor  : " + chosenDoctor);
                                System.out.println("   Date    : " + chosenDate);
                                System.out.println("   Time    : " + chosenTime);
                                System.out.println(GREEN_TEXT + ">> Appointment confirmed! <<\n" + NO_COLOR);

                                String consultationID = getConsultationID();

                                // After user successfully picks a date, time, and doctor
                                storeBooking(consultationID, patientID, chosenDate, chosenTime, chosenDoctor, row, col);

                                System.out.println(RED_TEXT + "===============================================================================================" + NO_COLOR);
                                System.out.println(RED_TEXT + "|| !!!! PLEASE arrive at the clinic on-time accordingly and report to the front counter !!!! ||" + NO_COLOR);
                                System.out.println(RED_TEXT + "|| !!!!         Consultation will be forfeited after 10 minutes of late arrival         !!!! ||" + NO_COLOR);
                                System.out.println(RED_TEXT + "===============================================================================================\n" + NO_COLOR);

                                System.out.print("Please press ENTER to proceed... ");
                                scan.nextLine();
                                scan.nextLine();
                                return;
                            } else if ("N".equals(choose) || "n".equals(choose)) {

                                System.out.println("\n" + RED_TEXT + ">> Appointment cancelled! <<" + NO_COLOR);
                                System.out.println("   Doctor  : " + chosenDoctor);
                                System.out.println("   Date    : " + chosenDate);
                                System.out.println("   Time    : " + chosenTime);
                                System.out.println(RED_TEXT + ">> Appointment cancelled! <<\n" + NO_COLOR);

                                //release the chosen slot
                                bookingStatus[row][col] = BookStatus.AVAILABLE;
                                scan.nextLine();

                            } else {

                                //release the chosen slot
                                bookingStatus[row][col] = BookStatus.BOOKED;
                                System.out.println("\n" + RED_TEXT + ">> Invalid Choice. Heading back to Menu... <<\n" + NO_COLOR);
                                scan.nextLine();
                            }
                        }

                        System.out.print("Please press ENTER to proceed... ");
                        scan.nextLine();

                        //end of boooking
                        break;
                    } else {
                        System.out.println("\n" + RED_TEXT + ">> Invalid day. Please Try Again." + NO_COLOR);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n" + RED_TEXT + ">> Invalid input (next or prev). Please Try Again. <<" + NO_COLOR);
                }
            }
        }
    }

    public void bookAdmin(Map timetablePerDate) {

        Scanner scan = new Scanner(System.in);
        YearMonth currentMonth = YearMonth.now();

        String[] allDoctors = getAllDoctors();

        // get today's month
        while (true) {

            System.out.println(BLUE_TEXT + "\n=========================================" + NO_COLOR);
            System.out.println(BLUE_TEXT + " ** Admin: Block Doctor Time Slots **" + NO_COLOR);
            System.out.println(BLUE_TEXT + "=========================================" + NO_COLOR);

            calendar.displayCalendar(currentMonth);

            // Get doctors for current month dynamically
            String[] monthlyDoctors = getDoctorsForMonth(allDoctors, currentMonth);

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
            } else {
                try {
                    int day = Integer.parseInt(input);
                    if (day >= 1 && day <= currentMonth.lengthOfMonth()) {
                        LocalDate selectedDate = currentMonth.atDay(day);

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String chosenDate = selectedDate.format(formatter);

                        System.out.println("\n" + BLUE_TEXT + ">> You selected > " + chosenDate + " <<" + NO_COLOR);

                        // Get doctors for selected month
                        monthlyDoctors = getDoctorsForMonth(allDoctors, currentMonth);

                        BookStatus[][] bookingStatus;
                        if (timetablePerDate.containsKey(selectedDate)) {
                            bookingStatus = (BookStatus[][]) timetablePerDate.get(selectedDate);
                        } else {
                            bookingStatus = new BookStatus[9][monthlyDoctors.length];
                            for (int i = 0; i < 9; i++) {
                                for (int j = 0; j < monthlyDoctors.length; j++) {
                                    bookingStatus[i][j] = BookStatus.AVAILABLE;
                                }
                            }
                            // Set lunch break
                            for (int j = 0; j < monthlyDoctors.length; j++) {
                                bookingStatus[4][j] = BookStatus.BREAK;
                            }
                            timetablePerDate.put(selectedDate, bookingStatus);
                        }

                        printTimetable(monthlyDoctors, bookingStatus);

                        // Let admin choose a slot to block
                        System.out.println("\nSelect a time slot to block:");
                        System.out.print("Enter time slot row number (1-9) > ");
                        int row = Integer.parseInt(scan.nextLine());
                        row -= 1;

                        System.out.print("Enter doctor column number (1-" + monthlyDoctors.length + ") > ");
                        int col = Integer.parseInt(scan.nextLine());
                        col -= 1;

                        if (row < 0 || row >= 9 || col < 0 || col >= monthlyDoctors.length) {
                            System.out.println("\n\u001B[31m>> Invalid row or column number. <<\n" + NO_COLOR);
                            System.out.print("Please press ENTER to proceed... ");
                            scan.nextLine();
                            return;
                        }

                        // Check if slot can be blocked
                        BookStatus status = bookingStatus[row][col];
                        if (status == BookStatus.BOOKED) {
                            System.out.println("\n\u001B[31m>> This slot is unavailable. Cannot block this slot. <<\n" + NO_COLOR);
                        } else if (status == BookStatus.BREAK) {
                            System.out.println("\n\u001B[31m>> This is already a break time. <<\n" + NO_COLOR);
                        } else {
                            // Mark as booked (blocked)
                            bookingStatus[row][col] = BookStatus.BOOKED;

                            String[] periods = {
                                "08:00-09:00",
                                "09:00-10:00",
                                "10:00-11:00",
                                "11:00-12:00",
                                "12:00-13:00",
                                "13:00-14:00",
                                "14:00-15:00",
                                "15:00-16:00",
                                "16:00-17:00"
                            };

                            String chosenDoctor = monthlyDoctors[col];
                            String chosenTime = periods[row];

                            // Get reason for blocking
                            System.out.print("Enter reason for blocking this slot (e.g., Emergency Meeting, Training, etc.) > ");
                            String blockReason = scan.nextLine();

                            System.out.println("\n" + BLUE_TEXT + ">> Confirm Time Slot Block <<" + NO_COLOR);
                            System.out.println("    Doctor  : " + chosenDoctor);
                            System.out.println("    Date    : " + chosenDate);
                            System.out.println("    Time    : " + chosenTime);
                            System.out.println("    Reason  : " + blockReason);
                            System.out.println(BLUE_TEXT + ">> Confirm Time Slot Block <<\n" + NO_COLOR);

                            System.out.print("Confirm blocking this time slot? (Y/N) > ");
                            String choose = scan.next();

                            if ("Y".equals(choose) || "y".equals(choose)) {

                                int totalSteps = 5; // 5 steps: 0, 20, 40, 60, 80, 100
                                System.out.println("\nProcessing.....");

                                // processing loading bar
                                for (int i = 0; i <= totalSteps; i++) {
                                    int percent = (i * 100) / totalSteps;
                                    String bar = "#".repeat(i * 10) + "-".repeat(50 - (i * 10));
                                    // 50 chars total, 10 chars per step (20% each)
                                    switch (percent) {
                                        case 0:
                                            System.out.println("[" + RED_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        case 20:
                                            System.out.println("[" + RED_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        case 40:
                                            System.out.println("[" + RED_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        case 60:
                                            System.out.println("[" + YELLOW_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        case 80:
                                            System.out.println("[" + YELLOW_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        case 100:
                                            System.out.println("[" + GREEN_TEXT + bar + NO_COLOR + "] " + percent + "%");
                                            break;
                                        default:
                                            break;
                                    }
                                    try {
                                        Thread.sleep(750); // 750(millisecond) => 0.75 seconds delay per step
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                    }
                                }

                                System.out.println("\n" + RED_TEXT + ">> Time slot blocked successfully! <<" + NO_COLOR);
                                System.out.println("    Doctor  : " + chosenDoctor);
                                System.out.println("    Date    : " + chosenDate);
                                System.out.println("    Time    : " + chosenTime);
                                System.out.println("    Reason  : " + blockReason);
                                System.out.println(RED_TEXT + ">> Time slot blocked successfully! <<\n" + NO_COLOR);

                                String consultationID = getConsultationID();

                                // Store the blocked slot with special admin identifier
                                storeAdminBlock(consultationID, chosenDate, chosenTime, chosenDoctor, blockReason, row, col);

                                System.out.println(RED_TEXT + ">>          This time slot is now unavailable for patient bookings.        <<" + NO_COLOR);

                                System.out.print("\nPlease press ENTER to proceed... ");
                                scan.nextLine();
                                scan.nextLine();
                                return;

                            } else if ("N".equals(choose) || "n".equals(choose)) {

                                System.out.println("\n" + RED_TEXT + ">> Time slot block cancelled! <<" + NO_COLOR);
                                System.out.println("   Doctor  : " + chosenDoctor);
                                System.out.println("   Date    : " + chosenDate);
                                System.out.println("   Time    : " + chosenTime);
                                System.out.println(RED_TEXT + ">> Time slot block cancelled! <<\n" + NO_COLOR);

                                // Release the chosen slot
                                bookingStatus[row][col] = BookStatus.AVAILABLE;
                                scan.nextLine();

                            } else {
                                // Release the chosen slot on invalid input
                                bookingStatus[row][col] = BookStatus.AVAILABLE;
                                System.out.println("\n" + RED_TEXT + ">> Invalid Choice. Heading back to Menu... <<\n" + NO_COLOR);
                                scan.nextLine();
                            }
                        }

                        System.out.print("Please press ENTER to proceed... ");
                        scan.nextLine();

                        // end of blocking
                        break;
                    } else {
                        System.out.println("\n" + RED_TEXT + ">> Invalid day. Please Try Again." + NO_COLOR);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n" + RED_TEXT + ">> Invalid input (next or prev). Please Try Again. <<" + NO_COLOR);
                }
            }
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import java.util.Locale;
import java.time.YearMonth;
import java.time.format.TextStyle; //to translate date to english words
/**
 *
 * @author chinw
 */
public class calendar {
    // monthly calendars
    public static void displayCalendar(YearMonth yearMonth) {
        System.out.println("\n" + yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + yearMonth.getYear());
        System.out.println("Mon Tue Wed Thu Fri Sat Sun");

        int dayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue();
        int daysInMonth = yearMonth.lengthOfMonth();

        for (int i = 1; i < dayOfWeek; i++) {
            System.out.print("    ");
        }

        for (int day = 1; day <= daysInMonth; day++) {
            System.out.printf("%3d ", day);
            if ((day + dayOfWeek - 1) % 7 == 0) {
                System.out.println();
            }
        }
        System.out.println("\n");
    }
}

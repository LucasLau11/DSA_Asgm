/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import entity.Consultation;
import adt.*;
import java.util.*;

public class Report_Consultation {

    private MyLinkedList<Consultation> bookingList = BookingList.bookingList;

    public Report_Consultation(MyLinkedList<Consultation> bookingList) {
        this.bookingList = bookingList;
    }

    // 1. Total appointments per doctor
    public void reportAppointmentsByDoctor() {

        System.out.println("\n========== Report: Appointments Per Doctor ==========");
        Map<String, Integer> doctorCount = new HashMap<>();

        if (bookingList.isEmpty()) {
            System.out.println("      No Booking made yet  for Report Summaries ");
        } else {
            for (int i = 1; i <= bookingList.getLength(); i++) {
                Consultation c = bookingList.getEntry(i);
                doctorCount.put(c.getChosenDr(), doctorCount.getOrDefault(c.getChosenDr(), 0) + 1);
            }

            doctorCount.forEach((doctor, count)
                    -> System.out.printf("         Doctor %-10s  : %d appointments\n", doctor, count));
        }
    }

    // 2. Total appointments per patient
    public void reportAppointmentsByPatient() {

        System.out.println("\n========== Report: Appointments Per Patient =========");
        Map<String, Integer> patientCount = new HashMap<>();

        if (bookingList.isEmpty()) {
            System.out.println("      No Booking made yet  for Report Summaries ");
        } else {
            for (int i = 1; i <= bookingList.getLength(); i++) {
                Consultation c = bookingList.getEntry(i);
                patientCount.put(c.getPatientID(), patientCount.getOrDefault(c.getPatientID(), 0) + 1);
            }

            patientCount.forEach((patient, count)
                    -> System.out.printf("         Patient %-10s  : %d appointments\n", patient, count));
        }
    }

    // 3. Peak booking time slots
    public void reportPeakTimeSlots() {
        System.out.println("\n============== Report: Peak Time Slots ==============");
        Map<String, Integer> timeSlotCount = new LinkedHashMap<>();

        if (bookingList.isEmpty()) {
            System.out.println("      No Booking made yet  for Report Summaries ");
        } else {
            for (int i = 1; i <= bookingList.getLength(); i++) {
                Consultation c = bookingList.getEntry(i);
                timeSlotCount.put(c.getChosenTime(), timeSlotCount.getOrDefault(c.getChosenTime(), 0) + 1);
            }

            timeSlotCount.entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .forEach(entry -> System.out.printf("       Time Slot %-15s  : %d bookings\n", entry.getKey(), entry.getValue()));
        }
    }

    // 4. Most popular doctor
    public void reportMostPopularDoctor() {
        System.out.println("\n============ Report: Most Popular Doctor ============");
        Map<String, Integer> doctorCount = new HashMap<>();

        if (bookingList.isEmpty()) {
            System.out.println("      No Booking made yet  for Report Summaries ");
        } else {
            for (int i = 1; i <= bookingList.getLength(); i++) {
                Consultation c = bookingList.getEntry(i);
                doctorCount.put(c.getChosenDr(), doctorCount.getOrDefault(c.getChosenDr(), 0) + 1);
            }

            String popularDoctor = Collections.max(doctorCount.entrySet(), Map.Entry.comparingByValue()).getKey();
            int maxBookings = doctorCount.get(popularDoctor);

            System.out.printf("    Most popular doctor:  %s with %d bookings\n", popularDoctor, maxBookings);
        }
    }

    // 5. Daily appointment count
    public void reportDailyAppointments() {
        System.out.println("\n========== Report: Daily Appointment Count ==========");
        Map<String, Integer> dailyCount = new TreeMap<>();

        if (bookingList.isEmpty()) {
            System.out.println("      No Booking made yet  for Report Summaries ");
        } else {
            for (int i = 1; i <= bookingList.getLength(); i++) {
                Consultation c = bookingList.getEntry(i);
                dailyCount.put(c.getChosenDate(), dailyCount.getOrDefault(c.getChosenDate(), 0) + 1);
            }

            dailyCount.forEach((date, count)
                    -> System.out.printf("            %-12s : %d appointments\n", date, count));
        }
    }
    
    // 6. Doctor utilization percentage (Assuming 9 slots per day)
    public void reportDoctorUtilization() {
        System.out.println("\n=== Report: Doctor Utilization Percentage ===");
        Map<String, Integer> doctorCount = new HashMap<>();

        if (bookingList.isEmpty()) {
            System.out.println(" No Booking made yet  for Report Summaries ");
        } else {
            for (int i = 1; i <= bookingList.getLength(); i++) {
                Consultation c = bookingList.getEntry(i);
                doctorCount.put(c.getChosenDr(), doctorCount.getOrDefault(c.getChosenDr(), 0) + 1);
            }

            // Assume each doctor works every day of current month with 9 slots
            int slotsPerDay = 9;
            int workingDays = 30; // For simplicity
            int maxSlots = slotsPerDay * workingDays;

            doctorCount.forEach((doctor, count) -> {
                double percentage = (count * 100.0) / maxSlots;
                System.out.printf("Doctor %-10s : %.2f%% utilization\n", doctor, percentage);
            });
        }
    }
}

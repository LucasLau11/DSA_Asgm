/*@author chngthingwei*/
package control;
import adt.*;
import entity.*;
import control.*;
import boundary.*;
import utility.*;
import java.util.*;

import java.time.LocalDate;

public class TreatmentManager {

    private MyLinkedList<Consultation> bookingList = BookingList.bookingList;
    private MyLinkedList<Treatment> treatmentList = new MyLinkedList<>();
    private PharmacyManager pharmacyManager = new PharmacyManager();
    private PatientManager patientManager;
    private int treatmentCounter = 1;

    public TreatmentManager() {
    }

    public TreatmentManager(PatientManager patientManager) {
        this.patientManager = patientManager;
        initDummyData();
    }

    private void initDummyData() {
        if (treatmentList.isEmpty()) {
            addTreatment(new Treatment("T001", "P001", "John Smith", 35, "Flu", "Bob Lee", "M001",
                    "10/07/2025", "500mg", "3", 2.00, 20.0));
            addTreatment(new Treatment("T002", "P002", "Mary Johnson", 28, "Cough", "Clara Wong", "M002",
                    "12/07/2025", "10ml", "5", 5.00, 20.0));
            addTreatment(new Treatment("T003", "P001", "John Smith", 35, "Fever", "Clara Wong", "M003",
                    "08/07/2025", "2 tablets", "5", 5.00, 20.0));
            addTreatment(new Treatment("T004", "P003", "Ahmad Rahman", 45, "Cough", "David Tan", "M008",
                    "08/07/2025", "100ml", "5", 20.50, 20.0));
            addTreatment(new Treatment("T005", "P001", "John Smith", 35, "Cold", "David Tan", "M001",
                    "08/07/2025", "500mg", "5", 2.00, 20.0));

            updateCounterFromExisting();
        }
    }

    public void addTreatment(Treatment t) {
        treatmentList.add(t);
    }

    public PharmacyManager getPharmacyManager() {
        return pharmacyManager;
    }

    public ListInterface<Treatment> getTreatmentList() {
        return treatmentList;
    }

    public String generateNewTreatmentID() {
        if (treatmentList.isEmpty()) {
            return "T001";
        }

        // get the last treatment from your ADT
        Treatment last = treatmentList.getEntry(treatmentList.getLength());
        String lastID = last.getTreatmentID(); // e.g. "T006"

        int lastNum = Integer.parseInt(lastID.substring(1)); // extract 006 -> 6
        return String.format("T%03d", lastNum + 1);
    }

    public void updateCounterFromExisting() {
        for (int i = 1; i <= treatmentList.getLength(); i++) {
            Treatment t = treatmentList.getEntry(i); // index starts from 1
            String id = t.getTreatmentID().substring(1); // skip the 'T'
            int num = Integer.parseInt(id);
            if (num >= treatmentCounter) {
                treatmentCounter = num + 1;
            }
        }
    }

    public void searchTreatmentByPatientId(String patientId) {
        boolean found = false;
        for (int i = 1; i <= treatmentList.getLength(); i++) {
            Treatment t = treatmentList.getEntry(i);
            if (t.getPatientID().equalsIgnoreCase(patientId)) {
                System.out.println(t);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No treatment found for patient ID: " + patientId);
        }
    }

    public boolean removeSpecificTreatment(String patientId, int recordNumber) {
        int count = 0;

        for (int i = 1; i <= treatmentList.getLength(); i++) {
            Treatment t = treatmentList.getEntry(i);
            if (t.getPatientID().equals(patientId)) {
                count++;
                if (count == recordNumber) {
                    treatmentList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean existsPatientId(String patientId) {
        for (int i = 1; i <= treatmentList.getLength(); i++) {
            if (treatmentList.getEntry(i).getPatientID().equals(patientId)) {
                return true;
            }
        }
        return false;
    }

    public void displayTreatmentsByPatientId(String patientId) {
        int count = 0;
        for (int i = 1; i <= treatmentList.getLength(); i++) {
            Treatment t = treatmentList.getEntry(i);
            if (t.getPatientID().equals(patientId)) {
                count++;
                System.out.println(count + ". " + t);
            }
        }
        if (count == 0) {
            System.out.println("No treatments found for Patient ID: " + patientId);
        }
    }

    public boolean updateSpecificTreatment(String patientId, int recordNumber,
            String newDiagnosis, String newDoctorID,
            String newMedicineID, String newDosage,
            String newDuration, String newDate, double newConsultationFee) {
        int count = 0;
        for (int i = 1; i <= treatmentList.getLength(); i++) {
            Treatment t = treatmentList.getEntry(i);
            if (t.getPatientID().equals(patientId)) {
                count++;
                if (count == recordNumber) {
                    t.setDiagnosis(newDiagnosis);
                    t.setDoctorID(newDoctorID);
                    t.setMedicineID(newMedicineID);
                    t.setDosage(newDosage);
                    t.setDuration(newDuration);
                    t.setTreatmentDate(newDate);
                    t.setConsultationFee(newConsultationFee);
                    return true;
                }
            }
        }
        return false;
    }

    public int countTreatmentsByPatientId(String patientId) {
        int count = 0;
        for (int i = 1; i <= treatmentList.getLength(); i++) {
            if (treatmentList.getEntry(i).getPatientID().equals(patientId)) {
                count++;
            }
        }
        return count;
    }
//Summary Report Functions

    public void displayTotalTreatmentsPerPatient() {
        System.out.println("\nPatient ID\tTotal Treatments");

        MyLinkedList<String> countedPatients = new MyLinkedList<>();

        for (int i = 1; i <= treatmentList.getLength(); i++) {
            String patientId = treatmentList.getEntry(i).getPatientID();

            // Only count if not counted yet
            boolean alreadyCounted = false;
            for (int j = 1; j <= countedPatients.getLength(); j++) {
                if (countedPatients.getEntry(j).equals(patientId)) {
                    alreadyCounted = true;
                    break;
                }
            }
            if (alreadyCounted) {
                continue;
            }

            int count = 0;
            for (int j = 1; j <= treatmentList.getLength(); j++) {
                if (treatmentList.getEntry(j).getPatientID().equals(patientId)) {
                    count++;
                }
            }

            System.out.println(patientId + "\t\t" + count);
            countedPatients.add(patientId);
        }
    }

    public void displayTotalRevenue() {
        System.out.println("\n--- Total Revenue from Treatments ---");

        MyLinkedList<String> countedPatients = new MyLinkedList<>();
        MyLinkedList<Double> revenueList = new MyLinkedList<>();
        double totalRevenue = 0;

        for (int i = 1; i <= treatmentList.getLength(); i++) {
            String patientId = treatmentList.getEntry(i).getPatientID();
            double treatmentRevenue = treatmentList.getEntry(i).getMedicineUnitPrice() + treatmentList.getEntry(i).getConsultationFee();
            totalRevenue += treatmentRevenue;

            if (!countedPatients.contains(patientId)) {
                countedPatients.add(patientId);
                double patientRevenue = 0;
                for (int j = 1; j <= treatmentList.getLength(); j++) {
                    if (treatmentList.getEntry(j).getPatientID().equals(patientId)) {
                        patientRevenue += treatmentList.getEntry(j).getMedicineUnitPrice() + treatmentList.getEntry(j).getConsultationFee();
                    }
                }
                revenueList.add(patientRevenue);
            }
        }

        System.out.println("Patient ID\tRevenue (RM)");
        for (int i = 1; i <= countedPatients.getLength(); i++) {
            System.out.printf("%s\t\tRM %.2f%n", countedPatients.getEntry(i), revenueList.getEntry(i));
        }

        System.out.printf("Total Revenue: RM %.2f%n", totalRevenue);
    }

    public void displayMostCommonDiagnosis() {
        System.out.println("\nTop Diagnoses:");

        MyLinkedList<String> countedDiagnosis = new MyLinkedList<>();
        MyLinkedList<Integer> counts = new MyLinkedList<>();

        for (int i = 1; i <= treatmentList.getLength(); i++) {
            String diag = treatmentList.getEntry(i).getDiagnosis();

            // check if already counted
            boolean alreadyCounted = false;
            for (int j = 1; j <= countedDiagnosis.getLength(); j++) {
                if (countedDiagnosis.getEntry(j).equalsIgnoreCase(diag)) {
                    alreadyCounted = true;
                    break;
                }
            }
            if (alreadyCounted) {
                continue;
            }

            int count = 0;
            for (int j = 1; j <= treatmentList.getLength(); j++) {
                if (treatmentList.getEntry(j).getDiagnosis().equalsIgnoreCase(diag)) {
                    count++;
                }
            }

            countedDiagnosis.add(diag);
            counts.add(count);
        }

        // Display top 3
        for (int k = 1; k <= Math.min(3, countedDiagnosis.getLength()); k++) {
            int maxIndex = 1;
            for (int i = 2; i <= counts.getLength(); i++) {
                if (counts.getEntry(i) > counts.getEntry(maxIndex)) {
                    maxIndex = i;
                }
            }
            System.out.println(countedDiagnosis.getEntry(maxIndex) + " - " + counts.getEntry(maxIndex) + " cases");

            // Remove counted max so next iteration finds the next highest
            countedDiagnosis.remove(maxIndex);
            counts.remove(maxIndex);
        }
    }

    public void displayTopPrescribedMedicine(PharmacyManager pharmacyManager) {
        System.out.println("\nTop Prescribed Medicines:");

        MyLinkedList<String> countedMed = new MyLinkedList<>();
        MyLinkedList<Integer> medCounts = new MyLinkedList<>();

        for (int i = 1; i <= treatmentList.getLength(); i++) {
            String medId = treatmentList.getEntry(i).getMedicineID();

            // check if already counted
            boolean alreadyCounted = false;
            for (int j = 1; j <= countedMed.getLength(); j++) {
                if (countedMed.getEntry(j).equals(medId)) {
                    alreadyCounted = true;
                    break;
                }
            }
            if (alreadyCounted) {
                continue;
            }

            int count = 0;
            for (int j = 1; j <= treatmentList.getLength(); j++) {
                if (treatmentList.getEntry(j).getMedicineID().equals(medId)) {
                    count++;
                }
            }

            countedMed.add(medId);
            medCounts.add(count);
        }

        for (int k = 1; k <= Math.min(3, countedMed.getLength()); k++) {
            int maxIndex = 1;
            for (int i = 2; i <= medCounts.getLength(); i++) {
                if (medCounts.getEntry(i) > medCounts.getEntry(maxIndex)) {
                    maxIndex = i;
                }
            }

            PharmacyMedicine med = pharmacyManager.searchMedicineByID(countedMed.getEntry(maxIndex));
            String name = (med != null) ? med.getName() : "Unknown";
            int stock = (med != null) ? med.getQuantityInStock() : 0;
            System.out.println(name + " (ID: " + countedMed.getEntry(maxIndex) + ") - " + medCounts.getEntry(maxIndex) + " times, Stock: " + stock);

            countedMed.remove(maxIndex);
            medCounts.remove(maxIndex);
        }
    }

    public Map<Integer, Integer> displayAllBookingsTreatment(Map timetablePerDate) {

        Scanner scan = new Scanner(System.in);
        Map<Integer, Integer> displayMap = new HashMap<>();
        //need modify based on patientID
        if (bookingList == null || bookingList.isEmpty()) {
            System.out.printf("\n%-5s %-18s %-15s %-15s %-15s %-15s%n",
                    "No.", "Consultation ID", "Patient ID", "Date", "Time", "Doctor");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("                                 No bookings yet.                                    \n");
            System.out.print("Press ENTER to proceed...");
            scan.nextLine();
            return displayMap;
        } else {
            System.out.printf("\n%-5s %-18s %-15s %-15s %-15s %-15s%n",
                    "No.", "Consultation ID", "Patient ID", "Date", "Time", "Doctor");
            System.out.println("-------------------------------------------------------------------------------------");

            int count = 1; // track number of matches
            for (int i = 1; i <= bookingList.getLength(); i++) {
                Consultation c = bookingList.getEntry(i);

                if (!c.isDiagnosed()) { // only show if still pending
                    System.out.printf("%-5d %-18s %-15s %-15s %-15s %-15s%n",
                            count, c.getConsultationID(), c.getPatientID(),
                            c.getChosenDate(), c.getChosenTime(), c.getChosenDr());
                    displayMap.put(count, i);
                    count++;
                }
            }

            if (count == 1) { // means no pending consultation
                System.out.println("No consultations available for treatment.");
            }
            return displayMap;
        }

    }

}

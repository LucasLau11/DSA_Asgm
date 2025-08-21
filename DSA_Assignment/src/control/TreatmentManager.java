package control;

import adt.*;
import entity.*;

import java.time.LocalDate;

public class TreatmentManager {
    private MyLinkedList<Treatment> treatmentList = new MyLinkedList<>();
    private PharmacyManager pharmacyManager = new PharmacyManager();
    private PatientManager patientManager; // Remove = new PatientManager()
    private int treatmentCounter = 1;
    
    // Add constructor that accepts shared PatientManager
    public TreatmentManager(PatientManager patientManager) {
        this.patientManager = patientManager;
        initDummyData();
    }

    private void initDummyData() {
        if (treatmentList.isEmpty()) {
            addTreatment(new Treatment("T001", "P001","Patient 1",35, "Flu", "D001", "M001",
                LocalDate.of(2025, 7, 10), "500mg", "3 days", 2.00, 200));
            addTreatment(new Treatment("T002", "P002","Patient 2",28, "Cough", "D001", "M002",
                LocalDate.of(2025, 7, 12), "10ml", "5 days", 5.00, 100));
            addTreatment(new Treatment("T003", "P001","Patient 1",35, "Fever", "D002", "M003",
                LocalDate.of(2025, 7, 8), "2 tablets", "5 days", 5.00, 100));

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
    return String.format("T%03d", treatmentCounter++);
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

    public boolean removeTreatmentById(String treatmentId) {
    for (int i = 1; i <= treatmentList.getLength(); i++) {
        Treatment t = treatmentList.getEntry(i);
        if (t.getTreatmentID().equals(treatmentId)) {
            treatmentList.remove(i); // remove by index
            return true;
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
                                           String newDuration, LocalDate newDate,double newConsultationFee) {
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
    System.out.println("Patient ID\tTotal Treatments");
    MyLinkedList<String> countedPatients = new MyLinkedList<>();

    for (int i = 1; i <= treatmentList.getLength(); i++) {
        Treatment t = treatmentList.getEntry(i);
        String patientId = t.getPatientID();

        // Only count if this patient hasn't been counted before
        if (!countedPatients.contains(patientId)) {
            int count = 0;

            // Count all treatments for this patient
            for (int j = 1; j <= treatmentList.getLength(); j++) {
                if (treatmentList.getEntry(j).getPatientID().equals(patientId)) {
                    count++;
                }
            }

            System.out.println(patientId + "\t\t" + count);
            countedPatients.add(patientId); // mark as counted
        }
    }
}


public void displayTotalRevenue() {
    double total = 0;
    for (int i = 1; i <= treatmentList.getLength(); i++) {
        Treatment t = treatmentList.getEntry(i);
        total += t.getMedicineUnitPrice() + t.getConsultationFee();
    }
    System.out.printf("Total revenue from all treatments: RM %.2f%n", total);
}

public void displayMostCommonDiagnosis() {
    MyLinkedList<String> countedDiagnosis = new MyLinkedList<>();
    String mostCommon = "";
    int maxCount = 0;

    for (int i = 1; i <= treatmentList.getLength(); i++) {
        String diagnosis = treatmentList.getEntry(i).getDiagnosis();

        if (!countedDiagnosis.contains(diagnosis)) {
            int count = 0;

            for (int j = 1; j <= treatmentList.getLength(); j++) {
                if (treatmentList.getEntry(j).getDiagnosis().equalsIgnoreCase(diagnosis)) {
                    count++;
                }
            }

            if (count > maxCount) {
                maxCount = count;
                mostCommon = diagnosis;
            }

            countedDiagnosis.add(diagnosis);
        }
    }

    if (!mostCommon.isEmpty()) {
        System.out.println("Most Common Diagnosis: " + mostCommon + " (" + maxCount + " cases)");
    } else {
        System.out.println("No diagnosis records found.");
    }
}

public void displayTopPrescribedMedicine(PharmacyManager pharmacyManager) {
    MyLinkedList<String> countedMedicine = new MyLinkedList<>();
    String topMedicineId = "";
    int maxCount = 0;

    for (int i = 1; i <= treatmentList.getLength(); i++) {
        String medId = treatmentList.getEntry(i).getMedicineID();
        if (!countedMedicine.contains(medId)) {
            int count = 0;

            for (int j = 1; j <= treatmentList.getLength(); j++) {
                if (treatmentList.getEntry(j).getMedicineID().equalsIgnoreCase(medId)) {
                    count++;
                }
            }

            if (count > maxCount) {
                maxCount = count;
                topMedicineId = medId;
            }

            countedMedicine.add(medId);
        }
    }

    if (!topMedicineId.isEmpty()) {
        
          PharmacyMedicine medicine = pharmacyManager.searchMedicineByID(topMedicineId);
        String medName = (medicine != null) ? medicine.getName() : "Unknown Medicine";

        System.out.println("Most Prescribed Medicine: " + medName +
                           " (ID: " + topMedicineId + ", " + maxCount + " times)");
    } else {
        System.out.println("No medicine records found.");
    }
}
}

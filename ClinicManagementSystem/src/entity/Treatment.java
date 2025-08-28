/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*@author chngthingwei*/
package entity;

import control.*;

import java.time.LocalDate;

public class Treatment {

    private String treatmentID;
    private String patientID;
    private String patientName;
    private int patientAge;
    private String diagnosis;
    private String doctor;
    private String medicineID; // from PharmacyMedicine
    private String treatmentDate;
    private String dosage;
    private String duration;
    private double medicineUnitPrice;
    private double consultationFee;

    public Treatment(String treatmentID, String patientID, String patientName, int patientAge, String diagnosis, String doctor, String medicineID, String treatmentDate, String dosage, String duration, double medicineUnitPrice, double consultationFee) {
        this.treatmentID = treatmentID;
        this.patientID = patientID;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.diagnosis = diagnosis;
        this.doctor = doctor;
        this.medicineID = medicineID;
        this.treatmentDate = treatmentDate;
        this.dosage = dosage;
        this.duration = duration;
        this.medicineUnitPrice = medicineUnitPrice;
        this.consultationFee = consultationFee;
    }

    // Getters and Setters
    public String getTreatmentID() {
        return treatmentID;
    }

    public void setTreatmentID(String treatmentID) {
        this.treatmentID = treatmentID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDoctorID() {
        return doctor;
    }

    public void setDoctorID(String doctor) {
        this.doctor = doctor;
    }

    public String getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    public String getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(String treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getMedicineUnitPrice() {
        return medicineUnitPrice;
    }

    public void setMedicineUnitPrice(double medicineUnitPrice) {
        this.medicineUnitPrice = medicineUnitPrice;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public double getTotalCost() {
        return consultationFee + medicineUnitPrice;
    }

    @Override
    public String toString() {
        //String doctorName = DoctorManager.getDoctorNameById(doctorID);
        PharmacyManager pharmacyManager = new PharmacyManager();
        PharmacyMedicine medicine = pharmacyManager.searchMedicineByID(medicineID);
        return "Treatment ID: " + treatmentID + ", Patient ID: " + patientID + ", Patient Age: " + patientAge + ", Diagnosis: " + diagnosis
                + ", Doctor: " + doctor + ", Medicine ID: " + medicineID + ", Medicine Name: " + medicine.getName() + ", Date: " + treatmentDate
                + ", Dosage: " + dosage + ", Duration: " + duration + " days " + ", Medicine Unit Price: RM " + String.format("%.2f", medicineUnitPrice) + ", Consulation Fee: RM " + String.format("%.2f", consultationFee);
    }
}

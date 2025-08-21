/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import control.*;

import java.time.LocalDate;

public class Treatment {
    private String treatmentID;
    private String patientID;
    private String patientName;
    private int patientAge;
    private String diagnosis;
    private String doctorID;
    private String medicineID; // from PharmacyMedicine
    private LocalDate treatmentDate;
    private String dosage;
    private String duration;
    private double medicineUnitPrice;
    private double consultationFee;
    public Treatment(String treatmentID, String patientID,String patientName,int patientAge, String diagnosis, String doctorID,
                     String medicineID, LocalDate treatmentDate, String dosage, String duration, double medicineUnitPrice,double consultationFee) {
        this.treatmentID = treatmentID;
        this.patientID = patientID;
        this.patientName=patientName;
        this.patientAge=patientAge;
        this.diagnosis = diagnosis;
        this.doctorID = doctorID;
        this.medicineID = medicineID;
        this.treatmentDate = treatmentDate;
        this.dosage = dosage;
        this.duration = duration;
        this.medicineUnitPrice=medicineUnitPrice;
        this.consultationFee=consultationFee;
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
    
    public String getPatientName(){
        return patientName;
    }
    
    public void setPatientName(String patientName){
        this.patientName=patientName;
    }
    
    public int getPatientAge(){
        return patientAge;
    }
    
    public void setPatientAge(int patientAge){
        this.patientAge=patientAge;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    public LocalDate getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(LocalDate treatmentDate) {
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
    
    public double getMedicineUnitPrice(){
        return medicineUnitPrice;
    }
    
    public void setMedicineUnitPrice(double medicineUnitPrice){
        this.medicineUnitPrice=medicineUnitPrice;
    }
    
    public double getConsultationFee(){
        return consultationFee;
    }
    
    public void setConsultationFee(double consultationFee){
        this.consultationFee=consultationFee;
    }
    
    public double getTotalCost() {
    return consultationFee + medicineUnitPrice;
}
    @Override
    public String toString() {
        //String doctorName = DoctorManager.getDoctorNameById(doctorID);
    PharmacyManager pharmacyManager = new PharmacyManager();
PharmacyMedicine medicine = pharmacyManager.searchMedicineByID(medicineID);
        return "Treatment ID: " + treatmentID + ", Patient ID: " + patientID + ", Patient Age: "+patientAge+", Diagnosis: " + diagnosis +
               ", Doctor: " + doctorID + ", Medicine ID: " + medicineID +", Medicine Name: "+medicine.getName() +", Date: " + treatmentDate +
               ", Dosage: " + dosage + ", Duration: " + duration + ", Medicine Unit Price: RM " + String.format("%.2f",medicineUnitPrice) + ", Consulation Fee: RM "  + String.format("%.2f",consultationFee);
    }
}





//package entity;
//
//import java.time.LocalDate;
//
//public class Treatment {
//    private String treatmentID;
//    private String patientID;
//    private String diagnosis;
//    private String doctorName;
//    private String medication;
//    private LocalDate treatmentDate;
//    private String dosage;
//    private String duration;
//
//    public Treatment(String treatmentID, String patientID, String diagnosis, String doctorName,
//                     String medication, LocalDate treatmentDate, String dosage, String duration) {
//        this.treatmentID = treatmentID;
//        this.patientID = patientID;
//        this.diagnosis = diagnosis;
//        this.doctorName = doctorName;
//        this.medication = medication;
//        this.treatmentDate = treatmentDate;
//        this.dosage = dosage;
//        this.duration = duration;
//    }
//
//    // Getters
//    public String getTreatmentId() {
//        return treatmentID;
//    }
//
//    public String getPatientId() {
//        return patientID;
//    }
//
//    public String getDiagnosis() {
//        return diagnosis;
//    }
//
//    public String getDoctorName() {
//        return doctorName;
//    }
//
//    public String getMedication() {
//        return medication;
//    }
//
//    public LocalDate getTreatmentDate() {
//        return treatmentDate;
//    }
//
//    public String getDosage() {
//        return dosage;
//    }
//
//    public String getDuration() {
//        return duration;
//    }
//// Setters
//    public void setTreatmentId(String treatmentID) {
//        this.treatmentID = treatmentID;
//    }
//
//    public void setPatientId(String patientID) {
//        this.patientID = patientID;
//    }
//
//    public void setDiagnosis(String diagnosis) {
//        this.diagnosis = diagnosis;
//    }
//
//    public void setDoctorName(String doctorName) {
//        this.doctorName = doctorName;
//    }
//
//    public void setMedication(String medication) {
//        this.medication = medication;
//    }
//
//    public void setTreatmentDate(LocalDate treatmentDate) {
//        this.treatmentDate = treatmentDate;
//    }
//
//    public void setDosage(String dosage) {
//        this.dosage = dosage;
//    }
//
//    public void setDuration(String duration) {
//        this.duration = duration;
//    }
//
//    // toString method for display
//    @Override
//    public String toString() {
//        return String.format("%-8s %-8s %-15s %-15s %-15s %-12s %-10s %-10s",
//                treatmentID, patientID, diagnosis, doctorName, medication,
//                treatmentDate, dosage, duration);
//    }
//}

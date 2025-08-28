package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** @author Lucas **/
public class PatientRecord {
    private String recordId;
    private String patientId;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;
    private String address;
    private String illnessDescription;
    private LocalDateTime recordDate;
    private String visitReason;
    private boolean isActive; // true for current record, false for historical

    public PatientRecord(String patientId, String name, int age, String gender,
                        String contactNumber, String address, String illnessDescription, 
                        String visitReason) {
        this.recordId = generateRecordId();
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.address = address;
        this.illnessDescription = illnessDescription;
        this.visitReason = visitReason;
        this.recordDate = LocalDateTime.now();
        this.isActive = true;
    }

    // Generate unique record ID
    private String generateRecordId() {
        return "R" + System.currentTimeMillis();
    }

    // Create Patient object from current record
    public Patient toPatient() {
        return new Patient(patientId, name, age, gender, contactNumber, address, illnessDescription);
    }

    
    // Create PatientRecord from Patient
    public static PatientRecord fromPatient(Patient patient, String visitReason) {
        return new PatientRecord(
            patient.getPatientId(),
            patient.getName(),
            patient.getAge(),
            patient.getGender(),
            patient.getContactNumber(),
            patient.getAddress(),
            patient.getIllnessDescription(),
            visitReason
        );
    }

    // Getters and setters
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIllnessDescription() {
        return illnessDescription;
    }

    public void setIllnessDescription(String illnessDescription) {
        this.illnessDescription = illnessDescription;
    }

    public LocalDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDateTime recordDate) {
        this.recordDate = recordDate;
    }

    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return recordDate.format(formatter);
    }

    @Override
    public String toString() {
        return "PatientRecord{" +
                "recordId='" + recordId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", name='" + name + '\'' +
                ", recordDate=" + getFormattedDate() +
                ", visitReason='" + visitReason + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
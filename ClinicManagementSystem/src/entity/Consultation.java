/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author chinw
 */
public class Consultation {

    private String consultationID;
    private String patientID;
    private String chosenDate;
    private String chosenTime;
    private String chosenDr;
    private String reason;
    private int row;
    private int col;

    public Consultation() {

    }

    public Consultation(String consultationID, String patientID, String chosenDate, String chosenTime, String chosenDr, String reason, int row, int col) {
        this.consultationID = consultationID;
        this.patientID = patientID;
        this.chosenDate = chosenDate;
        this.chosenTime = chosenTime;
        this.chosenDr = chosenDr;
        this.reason = reason;
        this.row = row;
        this.col = col;
    }

    public Consultation(String consultationID, String patientID, String chosenDate, String chosenTime, String chosenDr, int row, int col) {
        this.consultationID = consultationID;
        this.patientID = patientID;
        this.chosenDate = chosenDate;
        this.chosenTime = chosenTime;
        this.chosenDr = chosenDr;
        this.row = row;
        this.col = col;
    }

    public String getConsultationID() {
        return consultationID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getChosenDate() {
        return chosenDate;
    }

    public String getChosenTime() {
        return chosenTime;
    }

    public String getChosenDr() {
        return chosenDr;
    }

    public String getReason() {
        return reason;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setConsultationID(String consultationID) {
        this.consultationID = consultationID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setChosenDate(String chosenDate) {
        this.chosenDate = chosenDate;
    }

    public void setChosenTime(String chosenTime) {
        this.chosenTime = chosenTime;
    }

    public void setChosenDr(String chosenDr) {
        this.chosenDr = chosenDr;
    }

    private boolean diagnosed; // false by default

    public boolean isDiagnosed() {
        return diagnosed;
    }

    public void setDiagnosed(boolean diagnosed) {
        this.diagnosed = diagnosed;
    }

    @Override
    public String toString() {
        return "Consultation{" + "consultationID=" + consultationID + ", patientID=" + patientID + ", chosenDate=" + chosenDate + ", chosenTime=" + chosenTime + ", chosenDr=" + chosenDr + '}';
    }

}

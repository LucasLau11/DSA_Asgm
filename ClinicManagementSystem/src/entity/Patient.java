package entity;

/** @author Lucas **/
public class Patient {
    private String patientId;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;
    private String address;
    private String illnessDescription;

    public Patient(String patientId, String name, int age, String gender,
                   String contactNumber, String address, String illnessDescription) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.address = address;
        this.illnessDescription = illnessDescription;
    }

    // Getters and setters
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

    // Override equals method for proper comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Patient patient = (Patient) obj;
        return patientId != null ? patientId.equals(patient.patientId) : patient.patientId == null;
    }

    // Override hashCode for consistency with equals
    @Override
    public int hashCode() {
        return patientId != null ? patientId.hashCode() : 0;
    }

    // Override toString for better debugging and display
    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", address='" + address + '\'' +
                ", illnessDescription='" + illnessDescription + '\'' +
                '}';
    }
}
package control;

/** @author Lucas **/

import entity.Patient;
import entity.PatientRecord;
import adt.MyLinkedList;

public class PatientManager {
    private final MyLinkedList<PatientRecord> patientRecords;
    private final MyLinkedList<Patient> patientQueue;

    public PatientManager() {
        this.patientRecords = new MyLinkedList<>();
        this.patientQueue = new MyLinkedList<>();
        loadSampleData(); // Load hardcoded data
    }

    // ========== SAMPLE DATA LOADING ==========
    private void loadSampleData() {
        // Sample patients with multiple visits
        addSamplePatient("P001", "John Smith", 35, "Male", "012-3456789", 
                        "123 Main St, KL", "Hypertension", "Regular checkup");
        
        addSamplePatient("P002", "Mary Johnson", 28, "Female", "019-8765432", 
                        "456 Oak Ave, Selangor", "Diabetes Type 2", "Blood sugar monitoring");
        
        addSamplePatient("P003", "Ahmad Rahman", 45, "Male", "013-1234567", 
                        "789 Pine Rd, Penang", "Back pain", "Physical therapy consultation");
        
        addSamplePatient("P004", "Siti Fatimah", 32, "Female", "017-9876543", 
                        "321 Elm St, Johor", "Migraine", "Headache treatment");
        
        addSamplePatient("P005", "David Chen", 52, "Male", "016-5555555", 
                        "654 Maple Dr, Sabah", "Heart condition", "Cardiology checkup");
        
        // Add multiple visits for some patients to create statistics
        addAdditionalVisit("P001", "Follow-up appointment");
        addAdditionalVisit("P001", "Medication review");
        addAdditionalVisit("P002", "Insulin adjustment");
        addAdditionalVisit("P003", "Pain management");
        addAdditionalVisit("P001", "Emergency consultation");
        
        System.out.println("[SYSTEM] Sample patient data loaded successfully.");
    }
    
    private void addSamplePatient(String id, String name, int age, String gender, 
                                 String contact, String address, String illness, String visitReason) {
        Patient patient = new Patient(id, name, age, gender, contact, address, illness);
        PatientRecord record = PatientRecord.fromPatient(patient, visitReason);
        patientRecords.add(record);
        // Note: Not adding to queue automatically for sample data
    }
    
    private void addAdditionalVisit(String patientId, String visitReason) {
        // Find the patient's current info
        Patient patient = getCurrentPatient(patientId);
        if (patient != null) {
            // Deactivate current record
            PatientRecord activeRecord = getActivePatientRecord(patientId);
            if (activeRecord != null) {
                activeRecord.setActive(false);
            }
            
            // Create new record
            PatientRecord newRecord = PatientRecord.fromPatient(patient, visitReason);
            patientRecords.add(newRecord);
        }
    }
public String generateNewPatientID() {
    if (patientRecords.isEmpty()) {
        return "P001";
    }

    int maxNum = 0;

    // Loop through all patients to find the highest number
    for (int i = 1; i <= patientRecords.getLength(); i++) {
        PatientRecord record = patientRecords.getEntry(i);
        String id = record.getPatientId();   // e.g. "P002"
        int num = Integer.parseInt(id.substring(1)); // remove "P" -> 2

        if (num > maxNum) {
            maxNum = num;
        }
    }

    // Generate next ID
    return String.format("P%03d", maxNum + 1);
}
    // ========== STATISTICS METHODS ==========
    
    /**
     * Generate comprehensive statistics report
     */
    public StatisticsReport generateStatisticsReport() {
        StatisticsReport report = new StatisticsReport();
        
        // Basic counts
        report.totalRecords = patientRecords.getLength();
        report.totalUniquePatients = getTotalPatients();
        report.currentQueueSize = patientQueue.getLength();
        
        // Calculate visit frequencies
        calculateVisitFrequencies(report);
        
        // Calculate age demographics
        calculateAgeDemographics(report);
        
        // Calculate gender distribution
        calculateGenderDistribution(report);
        
        // Calculate common visit reasons
        calculateCommonVisitReasons(report);
        
        // Calculate common illnesses
        calculateCommonIllnesses(report);
        
        return report;
    }
    
    private void calculateVisitFrequencies(StatisticsReport report) {
        MyLinkedList<String> patientIds = new MyLinkedList<>();
        MyLinkedList<Integer> visitCounts = new MyLinkedList<>();
        
        // Count visits per patient
        for (int i = 1; i <= patientRecords.getLength(); i++) {
            PatientRecord record = patientRecords.getEntry(i);
            String patientId = record.getPatientId();
            
            // Find if patient already counted
            int existingIndex = -1;
            for (int j = 1; j <= patientIds.getLength(); j++) {
                if (patientIds.getEntry(j).equals(patientId)) {
                    existingIndex = j;
                    break;
                }
            }
            
            if (existingIndex == -1) {
                // New patient
                patientIds.add(patientId);
                visitCounts.add(1);
            } else {
                // Increment existing count
                int currentCount = visitCounts.getEntry(existingIndex);
                visitCounts.replace(existingIndex, currentCount + 1);
            }
        }
        
        // Find patient with most visits
        int maxVisits = 0;
        String mostFrequentPatient = "";
        for (int i = 1; i <= visitCounts.getLength(); i++) {
            int visits = visitCounts.getEntry(i);
            if (visits > maxVisits) {
                maxVisits = visits;
                mostFrequentPatient = patientIds.getEntry(i);
            }
        }
        
        report.mostFrequentPatientId = mostFrequentPatient;
        report.mostFrequentPatientVisits = maxVisits;
        
        // Get patient name
        if (!mostFrequentPatient.isEmpty()) {
            Patient patient = getCurrentPatient(mostFrequentPatient);
            if (patient != null) {
                report.mostFrequentPatientName = patient.getName();
            }
        }
        
        // Calculate average visits per patient
        if (patientIds.getLength() > 0) {
            report.averageVisitsPerPatient = (double) patientRecords.getLength() / patientIds.getLength();
        }
    }
    
    private void calculateAgeDemographics(StatisticsReport report) {
        int totalAge = 0;
        int count = 0;
        int under18 = 0, adults18to65 = 0, seniors65plus = 0;
        
        MyLinkedList<String> processedIds = new MyLinkedList<>();
        
        // Process each unique patient (latest record)
        for (int i = patientRecords.getLength(); i >= 1; i--) {
            PatientRecord record = patientRecords.getEntry(i);
            String patientId = record.getPatientId();
            
            // Check if already processed
            boolean alreadyProcessed = false;
            for (int j = 1; j <= processedIds.getLength(); j++) {
                if (processedIds.getEntry(j).equals(patientId)) {
                    alreadyProcessed = true;
                    break;
                }
            }
            
            if (!alreadyProcessed) {
                processedIds.add(patientId);
                int age = record.getAge();
                totalAge += age;
                count++;
                
                if (age < 18) under18++;
                else if (age <= 65) adults18to65++;
                else seniors65plus++;
            }
        }
        
        if (count > 0) {
            report.averageAge = (double) totalAge / count;
        }
        report.patientsUnder18 = under18;
        report.patientsAdults = adults18to65;
        report.patientsSeniors = seniors65plus;
    }
    
    private void calculateGenderDistribution(StatisticsReport report) {
        int maleCount = 0, femaleCount = 0, otherCount = 0;
        MyLinkedList<String> processedIds = new MyLinkedList<>();
        
        for (int i = patientRecords.getLength(); i >= 1; i--) {
            PatientRecord record = patientRecords.getEntry(i);
            String patientId = record.getPatientId();
            
            boolean alreadyProcessed = false;
            for (int j = 1; j <= processedIds.getLength(); j++) {
                if (processedIds.getEntry(j).equals(patientId)) {
                    alreadyProcessed = true;
                    break;
                }
            }
            
            if (!alreadyProcessed) {
                processedIds.add(patientId);
                String gender = record.getGender().toLowerCase();
                
                if (gender.contains("male") && !gender.contains("female")) {
                    maleCount++;
                } else if (gender.contains("female")) {
                    femaleCount++;
                } else {
                    otherCount++;
                }
            }
        }
        
        report.malePatients = maleCount;
        report.femalePatients = femaleCount;
        report.otherGenderPatients = otherCount;
    }
    
    private void calculateCommonVisitReasons(StatisticsReport report) {
        MyLinkedList<String> reasons = new MyLinkedList<>();
        MyLinkedList<Integer> counts = new MyLinkedList<>();
        
        for (int i = 1; i <= patientRecords.getLength(); i++) {
            PatientRecord record = patientRecords.getEntry(i);
            String reason = record.getVisitReason();
            
            int existingIndex = -1;
            for (int j = 1; j <= reasons.getLength(); j++) {
                if (reasons.getEntry(j).equalsIgnoreCase(reason)) {
                    existingIndex = j;
                    break;
                }
            }
            
            if (existingIndex == -1) {
                reasons.add(reason);
                counts.add(1);
            } else {
                int currentCount = counts.getEntry(existingIndex);
                counts.replace(existingIndex, currentCount + 1);
            }
        }
        
        // Find most common
        int maxCount = 0;
        String mostCommonReason = "";
        for (int i = 1; i <= counts.getLength(); i++) {
            int count = counts.getEntry(i);
            if (count > maxCount) {
                maxCount = count;
                mostCommonReason = reasons.getEntry(i);
            }
        }
        
        report.mostCommonVisitReason = mostCommonReason;
        report.mostCommonVisitReasonCount = maxCount;
    }
    
    private void calculateCommonIllnesses(StatisticsReport report) {
        MyLinkedList<String> illnesses = new MyLinkedList<>();
        MyLinkedList<Integer> counts = new MyLinkedList<>();
        MyLinkedList<String> processedIds = new MyLinkedList<>();
        
        // Only count unique patients for illness statistics
        for (int i = patientRecords.getLength(); i >= 1; i--) {
            PatientRecord record = patientRecords.getEntry(i);
            String patientId = record.getPatientId();
            
            boolean alreadyProcessed = false;
            for (int j = 1; j <= processedIds.getLength(); j++) {
                if (processedIds.getEntry(j).equals(patientId)) {
                    alreadyProcessed = true;
                    break;
                }
            }
            
            if (!alreadyProcessed) {
                processedIds.add(patientId);
                String illness = record.getIllnessDescription();
                
                int existingIndex = -1;
                for (int j = 1; j <= illnesses.getLength(); j++) {
                    if (illnesses.getEntry(j).equalsIgnoreCase(illness)) {
                        existingIndex = j;
                        break;
                    }
                }
                
                if (existingIndex == -1) {
                    illnesses.add(illness);
                    counts.add(1);
                } else {
                    int currentCount = counts.getEntry(existingIndex);
                    counts.replace(existingIndex, currentCount + 1);
                }
            }
        }
        
        // Find most common illness
        int maxCount = 0;
        String mostCommonIllness = "";
        for (int i = 1; i <= counts.getLength(); i++) {
            int count = counts.getEntry(i);
            if (count > maxCount) {
                maxCount = count;
                mostCommonIllness = illnesses.getEntry(i);
            }
        }
        
        report.mostCommonIllness = mostCommonIllness;
        report.mostCommonIllnessCount = maxCount;
    }

    // Register new patient
    public boolean registerPatient(Patient patient, String visitReason) {
        if (patient == null) {
            System.out.println("[ERROR] Patient data cannot be null.");
            return false;
        }
        
        // Check if patient already has an existing record
        PatientRecord activeRecord = getActivePatientRecord(patient.getPatientId());
        if (activeRecord != null) {
            System.out.println("[ERROR] Patient ID '" + patient.getPatientId() + "' already has an active record.");
            return false;
        }
        
        // Create new patient record
        PatientRecord newRecord = PatientRecord.fromPatient(patient, visitReason);
        patientRecords.add(newRecord);
        patientQueue.add(patient);
        
        System.out.println("[SUCCESS] Patient '" + patient.getName() + "' registered successfully.");
        return true;
    }
    
    

    // Update patient's existing record
    public boolean updatePatientField(String patientId, String fieldName, String newValue) {
        PatientRecord activeRecord = getActivePatientRecord(patientId);
        if (activeRecord == null) {
            return false;
        }

        try {
            switch (fieldName.toLowerCase()) {
                case "name":
                    activeRecord.setName(newValue);
                    break;
                case "age":
                    activeRecord.setAge(Integer.parseInt(newValue));
                    break;
                case "gender":
                    activeRecord.setGender(newValue);
                    break;
                case "contact":
                case "contactnumber":
                    activeRecord.setContactNumber(newValue);
                    break;
                case "address":
                    activeRecord.setAddress(newValue);
                    break;
                case "illness":
                case "illnessdescription":
                    activeRecord.setIllnessDescription(newValue);
                    break;
                case "visitreason":
                    activeRecord.setVisitReason(newValue);
                    break;
                default:
                    return false;
            }
            
            // Also update in queue if patient is still there
            updatePatientInQueue(patientId, activeRecord.toPatient());
            return true;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid age format.");
            return false;
        }
    }

    //Create a new visit record for existing patient
    public boolean addNewVisit(String patientId, Patient updatedInfo, String visitReason) {
        // Deactivate current record
        PatientRecord activeRecord = getActivePatientRecord(patientId);
        if (activeRecord != null) {
            activeRecord.setActive(false);
        }
        
        // Create new active record
        PatientRecord newRecord = PatientRecord.fromPatient(updatedInfo, visitReason);
        patientRecords.add(newRecord);
        patientQueue.add(updatedInfo);
        
        return true;
    }

    //Delete a patient 
    public boolean deletePatient(String patientId) {
        if (patientId == null) {
            return false;
        }
        
        boolean removedAny = false;
        
        // Remove all records for this patient
        for (int i = patientRecords.getLength(); i >= 1; i--) {
            PatientRecord record = patientRecords.getEntry(i);
            if (record.getPatientId().equals(patientId)) {
                patientRecords.remove(i);
                removedAny = true;
            }
        }
        
        // Remove from queue
        for (int i = patientQueue.getLength(); i >= 1; i--) {
            Patient patient = patientQueue.getEntry(i);
            if (patient.getPatientId().equals(patientId)) {
                patientQueue.remove(i);
                break;
            }
        }
        
        return removedAny;
    }

    // Get patient information
    public Patient getCurrentPatient(String patientId) {
        PatientRecord activeRecord = getActivePatientRecord(patientId);
        return activeRecord != null ? activeRecord.toPatient() : null;
    }

    // Get all records for a specific patient (history + current)
    public PatientRecord[] getPatientHistory(String patientId) {
        MyLinkedList<PatientRecord> history = new MyLinkedList<>();
        
        for (int i = 1; i <= patientRecords.getLength(); i++) {
            PatientRecord record = patientRecords.getEntry(i);
            if (record.getPatientId().equals(patientId)) {
                history.add(record);
            }
        }
        
        // Convert to array, with most recent first
        PatientRecord[] historyArray = new PatientRecord[history.getLength()];
        for (int i = 1; i <= history.getLength(); i++) {
            historyArray[history.getLength() - i] = history.getEntry(i);
        }
        
        return historyArray;
    }

    /**
     * Get basic info of all patients (ID and Name only)
     */
    public String[][] getAllPatientsBasicInfo() {
        MyLinkedList<String[]> uniquePatients = new MyLinkedList<>();
        MyLinkedList<String> processedIds = new MyLinkedList<>();
        
        // Get unique patients (latest record for each patient)
        for (int i = patientRecords.getLength(); i >= 1; i--) {
            PatientRecord record = patientRecords.getEntry(i);
            String patientId = record.getPatientId();
            
            boolean alreadyProcessed = false;
            for (int j = 1; j <= processedIds.getLength(); j++) {
                if (processedIds.getEntry(j).equals(patientId)) {
                    alreadyProcessed = true;
                    break;
                }
            }
            
            if (!alreadyProcessed) {
                processedIds.add(patientId);
                String[] basicInfo = {patientId, record.getName()};
                uniquePatients.add(basicInfo);
            }
        }
        
        // Convert to array
        String[][] result = new String[uniquePatients.getLength()][2];
        for (int i = 1; i <= uniquePatients.getLength(); i++) {
            result[i - 1] = uniquePatients.getEntry(i);
        }
        
        return result;
    }

    /**
     * Search patients by name (returns basic info)
     */
    public String[][] searchPatientsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new String[0][2];
        }
        
        MyLinkedList<String[]> results = new MyLinkedList<>();
        MyLinkedList<String> processedIds = new MyLinkedList<>();
        String searchName = name.toLowerCase().trim();
        
        for (int i = patientRecords.getLength(); i >= 1; i--) {
            PatientRecord record = patientRecords.getEntry(i);
            String patientId = record.getPatientId();
            
            if (record.getName().toLowerCase().contains(searchName)) {
                boolean alreadyProcessed = false;
                for (int j = 1; j <= processedIds.getLength(); j++) {
                    if (processedIds.getEntry(j).equals(patientId)) {
                        alreadyProcessed = true;
                        break;
                    }
                }
                
                if (!alreadyProcessed) {
                    processedIds.add(patientId);
                    String[] basicInfo = {patientId, record.getName()};
                    results.add(basicInfo);
                }
            }
        }
        
        String[][] resultArray = new String[results.getLength()][2];
        for (int i = 1; i <= results.getLength(); i++) {
            resultArray[i - 1] = results.getEntry(i);
        }
        return resultArray;
    }
    
    public String getPatientNameById(String patientId) {
    for (int i = 1; i <= patientRecords.getLength(); i++) {
        PatientRecord record = patientRecords.getEntry(i);
        if (record.getPatientId().equals(patientId)) {
            return record.getName();
        }
    }
    return null; // not found
}

    public int getPatientAgeById(String patientId) {
    for (int i = 1; i <= patientRecords.getLength(); i++) {
        PatientRecord record = patientRecords.getEntry(i);
        if (record.getPatientId().equals(patientId)) {
            return record.getAge();
        }
    }
    return -1; // not found
}

    // ========== Queue Management ==========
    
    public int getQueueSize() {
        return patientQueue.getLength();
    }

    public Patient getNextPatient() {
        if (patientQueue.isEmpty()) {
            return null;
        }
        return patientQueue.remove(1);
    }

    public Patient peekNextPatient() {
        if (patientQueue.isEmpty()) {
            return null;
        }
        return patientQueue.getEntry(1);
    }

    public Patient[] getQueuedPatients() {
        Patient[] queuedPatients = new Patient[patientQueue.getLength()];
        for (int i = 1; i <= patientQueue.getLength(); i++) {
            queuedPatients[i - 1] = patientQueue.getEntry(i);
        }
        return queuedPatients;
    }

    public boolean isPatientInQueue(String patientId) {
        for (int i = 1; i <= patientQueue.getLength(); i++) {
            Patient current = patientQueue.getEntry(i);
            if (current.getPatientId().equals(patientId)) {
                return true;
            }
        }
        return false;
    }

    public int getPatientQueuePosition(String patientId) {
        for (int i = 1; i <= patientQueue.getLength(); i++) {
            Patient current = patientQueue.getEntry(i);
            if (current.getPatientId().equals(patientId)) {
                return i;
            }
        }
        return -1;
    }

    // ========== Helper Methods ==========
    
    /**
     * Get the active (current) record for a patient
     */
    private PatientRecord getActivePatientRecord(String patientId) {
        for (int i = 1; i <= patientRecords.getLength(); i++) {
            PatientRecord record = patientRecords.getEntry(i);
            if (record.getPatientId().equals(patientId) && record.isActive()) {
                return record;
            }
        }
        return null;
    }

    /**
     * Update patient information in queue
     */
    private void updatePatientInQueue(String patientId, Patient updatedPatient) {
        for (int i = 1; i <= patientQueue.getLength(); i++) {
            Patient current = patientQueue.getEntry(i);
            if (current.getPatientId().equals(patientId)) {
                patientQueue.replace(i, updatedPatient);
                break;
            }
        }
    }

    /**
     * Check if patient ID exists in system
     */
    public boolean patientExists(String patientId) {
        for (int i = 1; i <= patientRecords.getLength(); i++) {
            PatientRecord record = patientRecords.getEntry(i);
            if (record.getPatientId().equals(patientId)) {
                return true;
            }
        }
        return false;
    }
    
    

    /**
     * Get total number of unique patients
     */
    public int getTotalPatients() {
        MyLinkedList<String> uniqueIds = new MyLinkedList<>();
        
        for (int i = 1; i <= patientRecords.getLength(); i++) {
            PatientRecord record = patientRecords.getEntry(i);
            String patientId = record.getPatientId();
            
            boolean found = false;
            for (int j = 1; j <= uniqueIds.getLength(); j++) {
                if (uniqueIds.getEntry(j).equals(patientId)) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                uniqueIds.add(patientId);
            }
        }
        
        return uniqueIds.getLength();
    }

    // ========== STATISTICS REPORT CLASS ==========
    
    /**
     * Inner class to hold statistics data
     */
    public static class StatisticsReport {
        // Basic counts
        public int totalRecords;
        public int totalUniquePatients;
        public int currentQueueSize;
        
        // Visit frequency
        public String mostFrequentPatientId;
        public String mostFrequentPatientName;
        public int mostFrequentPatientVisits;
        public double averageVisitsPerPatient;
        
        // Demographics
        public double averageAge;
        public int patientsUnder18;
        public int patientsAdults;
        public int patientsSeniors;
        public int malePatients;
        public int femalePatients;
        public int otherGenderPatients;
        
        // Common reasons and illnesses
        public String mostCommonVisitReason;
        public int mostCommonVisitReasonCount;
        public String mostCommonIllness;
        public int mostCommonIllnessCount;
        
        public StatisticsReport() {
            // Initialize with default values
            this.totalRecords = 0;
            this.totalUniquePatients = 0;
            this.currentQueueSize = 0;
            this.mostFrequentPatientId = "";
            this.mostFrequentPatientName = "";
            this.mostFrequentPatientVisits = 0;
            this.averageVisitsPerPatient = 0.0;
            this.averageAge = 0.0;
            this.patientsUnder18 = 0;
            this.patientsAdults = 0;
            this.patientsSeniors = 0;
            this.malePatients = 0;
            this.femalePatients = 0;
            this.otherGenderPatients = 0;
            this.mostCommonVisitReason = "";
            this.mostCommonVisitReasonCount = 0;
            this.mostCommonIllness = "";
            this.mostCommonIllnessCount = 0;
        }
    }
}
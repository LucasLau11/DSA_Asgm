package control;

import entity.Doctors;
import adt.MyLinkedList;
import adt.ListInterface;
import java.util.Scanner;

public class DoctorManager {

    private ListInterface<Doctors> doctors;
    private final String[] specialties = {
        "Cardiology", "Dermatology", "Neurology",
        "Pediatrics", "Oncology", "Orthopedics", "General Medicine"
    };

    public DoctorManager() {
        doctors = new MyLinkedList<>();

        // Predefined doctors
        doctors.add(new Doctors("Alice Smith", "Cardiology", "alice@hospital.com", "0123456789", "English, Malay", "101"));
        doctors.add(new Doctors("Bob Lee", "Dermatology", "bob@hospital.com", "0198765432", "English, Chinese", "102"));
        doctors.add(new Doctors("Clara Wong", "Neurology", "clara@hospital.com", "0112233445", "English, Tamil", "103"));
        doctors.add(new Doctors("David Tan", "Pediatrics", "david@hospital.com", "0187654321", "English, Malay, Chinese", "104"));
        doctors.add(new Doctors("David Jaork", "Pediatrics", "david@hospital2.com", "0187654381", "English, Malay, Chinese", "105"));
    }

    public String[] getAllDoctorNames() {
        if (doctors.isEmpty()) {
            return new String[0]; // Return empty array if no doctors
        }

        String[] doctorNames = new String[doctors.getLength()];
        for (int i = 1; i <= doctors.getLength(); i++) {
            doctorNames[i - 1] = doctors.getEntry(i).getDocName();
        }
        return doctorNames;
    }

    // Auto-generate new Doctor ID (D001, D002, ...)
    private String generateNewDoctorID() {
        int max = 0;
        for (int i = 1; i <= doctors.getLength(); i++) {
            String currentId = doctors.getEntry(i).getDocID();
            if (currentId.matches("^D\\d{3}$")) {
                int num = Integer.parseInt(currentId.substring(1));
                if (num > max) {
                    max = num;
                }
            }
        }
        return String.format("D%03d", max + 1);
    }

    // Check if an ID already exists
    private boolean idExists(String id) {
        for (int i = 1; i <= doctors.getLength(); i++) {
            if (doctors.getEntry(i).getDocID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    // Add doctor
    public void addDoctor(Doctors doctor) {
        if (idExists(doctor.getDocID())) {
            System.out.println("Error: Doctor ID already exists. Cannot add duplicate.");
            return;
        }
        doctors.add(doctor);
        System.out.println("Doctor added successfully!");
    }

    // Add doctor manually
    public void addDoctorManual() {
        Scanner sc = new Scanner(System.in);
        String name, specialty = "", email, number, languages, id;

        // Name
        while (true) {
            System.out.print("Enter Doctor Name: ");
            name = sc.nextLine();
            if (name.matches("^[A-Za-z ]+$")) {
                break;
            }
            System.out.println("Invalid name. Only letters and spaces allowed.");
        }

        // Specialty
        System.out.println("Select Doctor Specialty:");
        for (int i = 0; i < specialties.length; i++) {
            System.out.println((i + 1) + ". " + specialties[i]);
        }
        while (true) {
            System.out.print("Enter choice (1-" + specialties.length + "): ");
            if (sc.hasNextInt()) {
                int choice = sc.nextInt();
                sc.nextLine();
                if (choice >= 1 && choice <= specialties.length) {
                    specialty = specialties[choice - 1];
                    break;
                }
            } else {
                sc.nextLine();
            }
            System.out.println("Invalid choice. Please try again.");
        }

        // Email
        while (true) {
            System.out.print("Enter Doctor Email: ");
            email = sc.nextLine();
            if (email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                break;
            }
            System.out.println("Invalid email format.");
        }

        // Phone number
        while (true) {
            System.out.print("Enter Doctor Phone Number (10–11 digits, must start with 0): ");
            number = sc.nextLine().trim();
            if (number.matches("^0[0-9]{9,10}$")) {
                break;
            }
            System.out.println("Invalid phone number. Must start with 0 and be 10–11 digits.");
        }

        // Languages
        String[] availableLanguages = {
            "Chinese", "English", "Malay", "Cantonese", "Hokkien", "Tamil"
        };

        while (true) {
            System.out.println("\nSelect Doctor Spoken Languages (you can choose multiple, separated by commas):");
            for (int i = 0; i < availableLanguages.length; i++) {
                System.out.println((i + 1) + ". " + availableLanguages[i]);
            }
            System.out.print("Your choice(s): ");
            String input = sc.nextLine().trim();

            String[] choices = input.split(",");
            StringBuilder sb = new StringBuilder();
            boolean valid = true;

            for (String choice : choices) {
                choice = choice.trim();
                if (!choice.matches("\\d+")) { // must be number
                    valid = false;
                    break;
                }
                int option = Integer.parseInt(choice);
                if (option >= 1 && option <= availableLanguages.length) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(availableLanguages[option - 1]);
                } else {
                    valid = false;
                    break;
                }
            }

            if (valid && sb.length() > 0) {
                languages = sb.toString();
                break;
            } else {
                System.out.println("Invalid input. Please enter numbers (1-" + availableLanguages.length + ") separated by commas.");
            }
        }

        // Auto-generate ID
        id = generateNewDoctorID();
        System.out.println("Assigned Doctor ID: " + id);

        Doctors newDoctor = new Doctors(name, specialty, email, number, languages, id);
        addDoctor(newDoctor);
    }

    // List all doctors
    public void listDoctors() {
        if (doctors.isEmpty()) {
            System.out.println("No doctors available.");
            return;
        }
        for (int i = 1; i <= doctors.getLength(); i++) {
            System.out.println(doctors.getEntry(i).toString());
            System.out.println("---------------------");
        }
    }

    // Find doctor by ID
    private Doctors findDoctorById(String id) {
        for (int i = 1; i <= doctors.getLength(); i++) {
            if (doctors.getEntry(i).getDocID().equals(id)) {
                return doctors.getEntry(i);
            }
        }
        return null;
    }

    // Edit doctor by ID
    public void editDoctor() {
        Scanner sc = new Scanner(System.in);

        while (true) {  // loop for editing multiple doctors if user wants
            String id = "";
            Doctors doc = null;

            // --- Step 1: Ask for Doctor ID with quit option ---
            while (true) {
                System.out.print("Enter Doctor ID to edit (or Q to quit): ");
                id = sc.nextLine().trim();

                if (id.equalsIgnoreCase("Q")) {
                    System.out.println("Returning to main menu...");
                    return; // exit the whole editDoctor() method
                }

                doc = findDoctorById(id);
                if (doc != null) {
                    break; // valid doctor found, move on
                }
                System.out.println("Doctor not found. Try again.");
            }

            // --- Step 2: Ask which field to edit ---
            int choice;
            while (true) {
                System.out.println("\nSelect what to edit:");
                System.out.println("1. Name");
                System.out.println("2. Specialty");
                System.out.println("3. Email");
                System.out.println("4. Phone Number");
                System.out.println("5. Languages");

                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine(); // consume newline
                    if (choice >= 1 && choice <= 5) {
                        break;
                    }
                } else {
                    sc.nextLine(); // clear invalid input
                }
                System.out.println("Invalid choice. Please enter 1-5.");
            }

            switch (choice) {
                case 1:
                    while (true) {
                        System.out.print("Enter new name: ");
                        String name = sc.nextLine().trim();
                        if (!name.isEmpty() && name.matches("^[A-Za-z ]+$")) {
                            doc.setDocName(name);
                            break;
                        }
                        System.out.println("Invalid name. Only letters and spaces allowed.");
                    }
                    break;

                case 2:
                    while (true) {
                        for (int i = 0; i < specialties.length; i++) {
                            System.out.println((i + 1) + ". " + specialties[i]);
                        }
                        System.out.print("Choose specialty: ");
                        if (sc.hasNextInt()) {
                            int spChoice = sc.nextInt();
                            sc.nextLine(); // consume newline
                            if (spChoice >= 1 && spChoice <= specialties.length) {
                                doc.setDocSpecialty(specialties[spChoice - 1]);
                                break;
                            }
                        } else {
                            sc.nextLine();
                        }
                        System.out.println("Invalid specialty choice. Try again.");
                    }
                    break;

                case 3:
                    while (true) {
                        System.out.print("Enter new email: ");
                        String email = sc.nextLine().trim();
                        if (email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
                            doc.setDocEmail(email);
                            break;
                        }
                        System.out.println("Invalid email format. Try again.");
                    }
                    break;

                case 4:
                    while (true) {
                        System.out.print("Enter new phone (digits only): ");
                        String phone = sc.nextLine().trim();
                        if (phone.matches("^0\\d{9,10}$")) {
                            doc.setDocNumber(phone);
                            break;
                        }
                        System.out.println("Invalid phone number. Must be 10–11 digits and start with 0.");
                    }
                    break;

                case 5:
                    while (true) {
                        System.out.print("Enter new languages: ");
                        String lang = sc.nextLine().trim();
                        if (!lang.isEmpty()) {
                            doc.setDocLanguages(lang);
                            break;
                        }
                        System.out.println("Languages cannot be empty.");
                    }
                    break;
            }

            System.out.println("Doctor details updated successfully!");

            // --- Step 3: Ask if want to continue editing ---
            System.out.print("Do you want to edit another doctor? (Y/N): ");
            String again = sc.nextLine().trim();
            if (!again.equalsIgnoreCase("Y")) {
                System.out.println("Exiting edit mode...");
                break; // exit the while loop, end method
            }
        }
    }

    // Delete doctor
    public void deleteDoctor() {
        if (doctors.isEmpty()) {
            System.out.println("No doctors available.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Doctor ID to delete: ");
        String id = sc.nextLine();

        for (int i = 1; i <= doctors.getLength(); i++) {
            if (doctors.getEntry(i).getDocID().equals(id)) {
                doctors.remove(i);
                System.out.println("Doctor deleted successfully!");
                return;
            }
        }
        System.out.println("Doctor not found.");
    }

    // Search by Specialty
    public void searchBySpecialty() {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < specialties.length; i++) {
            System.out.println((i + 1) + ". " + specialties[i]);
        }
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice > specialties.length) {
            System.out.println("Invalid choice.");
            return;
        }

        String selectedSpecialty = specialties[choice - 1];
        boolean found = false;

        for (int i = 1; i <= doctors.getLength(); i++) {
            if (doctors.getEntry(i).getDocSpecialty().equalsIgnoreCase(selectedSpecialty)) {
                System.out.println(doctors.getEntry(i));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No doctors found with specialty: " + selectedSpecialty);
        }
    }

    // Search by Language
    public void searchByLanguage() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter language [English,Chinese,Tamil,Malay]: ");
        String language = sc.nextLine();

        boolean found = false;
        for (int i = 1; i <= doctors.getLength(); i++) {
            if (doctors.getEntry(i).getDocLanguages().toLowerCase().contains(language.toLowerCase())) {
                System.out.println(doctors.getEntry(i));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No doctors found with language: " + language);
        }
    }

    // Report: Doctor Specialty Report
    public void generateSpecialtyReport() {
        if (doctors.isEmpty()) {
            System.out.println("No doctors in the system.");
            return;
        }

        int[] counts = new int[specialties.length];

        for (int i = 1; i <= doctors.getLength(); i++) {
            String sp = doctors.getEntry(i).getDocSpecialty();
            for (int j = 0; j < specialties.length; j++) {
                if (specialties[j].equalsIgnoreCase(sp)) {
                    counts[j]++;
                    break;
                }
            }
        }

        System.out.println("\n---- Doctor Specialty Report ----");
        for (int j = 0; j < specialties.length; j++) {
            System.out.println(specialties[j] + " : " + counts[j]);
        }
        System.out.println("Total Doctors: " + doctors.getLength());
    }

    // Report: Directory
    public void generateDoctorDirectory() {
        System.out.println("\n---- Doctor Directory ----");
        for (int i = 1; i <= doctors.getLength(); i++) {
            Doctors d = doctors.getEntry(i);
            System.out.println("ID: " + d.getDocID()
                    + " | Name: " + d.getDocName()
                    + " | Specialty: " + d.getDocSpecialty()
                    + " | Phone: " + d.getDocNumber()
                    + " | Email: " + d.getDocEmail());
        }
    }
}

package control;

import entity.Doctors;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;


public class DoctorManagement {
    private Doctors[] doctors;  
    private int count;       
    
    // Predefined list of specialties
    private final String[] specialties = {
        "Cardiology", "Dermatology", "Neurology", 
        "Pediatrics", "Oncology", "Orthopedics", "General Medicine"
    };
    
    public DoctorManagement(int size) {
        doctors = new Doctors[size];
        count = 0;
        
        doctors[count++] = new Doctors("Alice Smith", "Cardiology", "alice@hospital.com", "0123456789", "English, Malay", "101");
        doctors[count++] = new Doctors("Bob Lee", "Dermatology", "bob@hospital.com", "0198765432", "English, Chinese", "102");
        doctors[count++] = new Doctors("Clara Wong", "Neurology", "clara@hospital.com", "0112233445", "English, Tamil", "103");
        doctors[count++] = new Doctors("David Tan", "Pediatrics", "david@hospital.com", "0187654321", "English, Malay, Chinese", "104");
        doctors[count++] = new Doctors("David Jan", "Pediatrics", "david@hospital2.com", "0187654381", "English, Malay, Chinese", "105");
    }

    // Check if an ID already exists
    private boolean idExists(String id) {
        for (int i = 0; i < count; i++) {
            if (doctors[i].getDocID().equals(id)) {
                return true;
            }
        }
        return false;
    }
    
    // Add doctor by passing an object
    public void addDoctor(Doctors doctor) {
        if (count < doctors.length) {
            if (idExists(doctor.getDocID())) {
                System.out.println("Error: Doctor ID already exists. Cannot add duplicate.");
                return;
            }
            doctors[count] = doctor;
            count++;
            System.out.println("Doctor added successfully!");
        } else {
            System.out.println("Doctor list is full, cannot add more.");
        }
    }

    // Add doctor manually (with validation + specialty list)
    public void addDoctorManual() {
        if (count >= doctors.length) {
            System.out.println("Doctor list is full, cannot add more.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        String name, specialty, email, number, languages, id;

        // Name
        while (true) {
            System.out.print("Enter Doctor Name: ");
            name = sc.nextLine();
            if (name.matches("^[A-Za-z ]+$")) break;
            System.out.println("Invalid name. Only letters and spaces allowed.");
        }

        // Specialty - selection from list
        System.out.println("Select Doctor Specialty:");
        for (int i = 0; i < specialties.length; i++) {
            System.out.println((i + 1) + ". " + specialties[i]);
        }
        int choice;
        while (true) {
            System.out.print("Enter choice (1-" + specialties.length + "): ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); // consume newline
                if (choice >= 1 && choice <= specialties.length) {
                    specialty = specialties[choice - 1];
                    break;
                }
            } else {
                sc.nextLine(); // consume invalid input
            }
            System.out.println("Invalid choice. Please select a valid specialty number.");
        }

        // Email
        while (true) {
            System.out.print("Enter Doctor Email: ");
            email = sc.nextLine();
            if (email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) break;
            System.out.println("Invalid email format. Please use format X@X.X");
        }

        // Phone number (must be 10–11 digits)
        while (true) {
            System.out.print("Enter Doctor Phone Number (10–11 digits): ");
            number = sc.nextLine();
            if (number.matches("^[0-9]{10,11}$")) break;
            System.out.println("Invalid phone number. Must be 10 or 11 digits only.");
        }

        // Languages (letters, commas, spaces allowed)
        while (true) {
            System.out.print("Enter Doctor Spoken Languages (e.g. English, Malay, Chinese): ");
            languages = sc.nextLine();
            if (languages.matches("^[A-Za-z, ]+$")) break;
            System.out.println("Invalid languages. Only letters, commas, and spaces allowed.");
        }

        // ID with duplicate check
        while (true) {
            System.out.print("Enter Doctor ID: ");
            id = sc.nextLine();
            if (!id.matches("^[0-9]+$")) {
                System.out.println("Invalid ID. Only digits allowed.");
                continue;
            }
            if (idExists(id)) {
                System.out.println("Error: Doctor ID already exists. Please enter a unique ID.");
                continue;
            }
            break;
        }

        // Create doctor object
        Doctors newDoctor = new Doctors(name, specialty, email, number, languages, id);

        // Add to array
        addDoctor(newDoctor);
    }

    public void listDoctors() {
        if (count == 0) {
            System.out.println("No doctors available.");
            return;
        }
        for (int i = 0; i < count; i++) {
            System.out.println(doctors[i].toString());
            System.out.println("---------------------");
        }
    }
    
    private Doctors findDoctorById(String id) {
    for (int i = 0; i < count; i++) {
        if (doctors[i].getDocID().equals(id)) {
            return doctors[i]; // found
        }
    }
    return null; // not found
}
    
// Edit doctor by ID
public void editDoctor() {
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter Doctor ID to edit: ");
    String id = sc.nextLine();   // <-- ID is String

    Doctors doc = findDoctorById(id);
    if (doc == null) {
        System.out.println("Doctor not found.");
        return;
    }

    System.out.println("Select what to edit:");
    System.out.println("1. Name");
    System.out.println("2. Specialty");
    System.out.println("3. Email");
    System.out.println("4. Phone Number");
    System.out.println("5. Languages");
    System.out.println("6. ID");

    int choice = sc.nextInt();
    sc.nextLine(); // consume newline

    String newValue = null; // temporary storage for new input

    switch (choice) {
        case 1:
            while (true) {
                System.out.print("Enter new name: ");
                newValue = sc.nextLine();
                if (newValue.matches("^[A-Za-z ]+$")) break;
                System.out.println("Invalid name. Only letters and spaces allowed.");
            }
            break;

        case 2:
            System.out.println("Select Doctor Specialty:");
            for (int i = 0; i < specialties.length; i++) {
                System.out.println((i + 1) + ". " + specialties[i]);
            }
            while (true) {
                System.out.print("Enter choice (1-" + specialties.length + "): ");
                if (sc.hasNextInt()) {
                    int spChoice = sc.nextInt();
                    sc.nextLine(); // consume newline
                    if (spChoice >= 1 && spChoice <= specialties.length) {
                        newValue = specialties[spChoice - 1];
                        break;
                    }
                } else {
                    sc.nextLine(); // consume invalid input
                }
                System.out.println("Invalid choice. Please select a valid specialty number.");
            }
            break;

        case 3:
            while (true) {
                System.out.print("Enter new email: ");
                newValue = sc.nextLine();
                if (newValue.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) break;
                System.out.println("Invalid email format. Please use format X@X.X");
            }
            break;

        case 4:
            while (true) {
                System.out.print("Enter new phone number (10–11 digits): ");
                newValue = sc.nextLine();
                if (newValue.matches("^[0-9]{10,11}$")) break;
                System.out.println("Invalid phone number. Must be 10 or 11 digits only.");
            }
            break;

        case 5:
            while (true) {
                System.out.print("Enter new languages (e.g. English, Malay, Chinese): ");
                newValue = sc.nextLine();
                if (newValue.matches("^[A-Za-z, ]+$")) break;
                System.out.println("Invalid languages. Only letters, commas, and spaces allowed.");
            }
            break;

        case 6:
            while (true) {
                System.out.print("Enter new ID: ");
                newValue = sc.nextLine();
                if (!newValue.matches("^[0-9]+$")) {
                    System.out.println("Invalid ID. Only digits allowed.");
                    continue;
                }
                if (idExists(newValue)) {
                    System.out.println("Error: Doctor ID already exists. Please enter a unique ID.");
                    continue;
                }
                break;
            }
            break;

        default:
            System.out.println("Invalid choice.");
            return;
    }

    // ✅ Confirmation step before applying changes
    System.out.print("Are you sure you want to update this field? (Y/N): ");
    String confirm = sc.nextLine().trim().toUpperCase();

    if (confirm.equals("Y")) {
        switch (choice) {
            case 1: doc.setDocName(newValue); break;
            case 2: doc.setDocSpecialty(newValue); break;
            case 3: doc.setDocEmail(newValue); break;
            case 4: doc.setDocNumber(newValue); break;
            case 5: doc.setDocLanguages(newValue); break;
            case 6: doc.setDocID(newValue); break;
        }
        System.out.println("✅ Doctor details updated!");
    } else {
        System.out.println("❌ Edit cancelled.");
    }
}

   
// Delete doctor by ID
public void deleteDoctor() {
    if (count == 0) {
        System.out.println("No doctors available.");
        return;
    }

    Scanner sc = new Scanner(System.in);
    System.out.print("Enter Doctor ID to delete: ");
    String id = sc.nextLine();

    for (int i = 0; i < count; i++) {
        if (doctors[i].getDocID().equals(id)) {
            // Shift array left
            for (int j = i; j < count - 1; j++) {
                doctors[j] = doctors[j + 1];
            }
            doctors[count - 1] = null;
            count--;
            System.out.println("Doctor deleted successfully!");
            return;
        }
    }
    System.out.println("Doctor not found.");
}

// Search by ID
public void searchByID() {
    if (count == 0) {
        System.out.println("No doctors available.");
        return;
    }

    Scanner sc = new Scanner(System.in);
    System.out.print("Enter Doctor ID to search: ");
    String id = sc.nextLine();

    boolean found = false;
    for (int i = 0; i < count; i++) {
        if (doctors[i].getDocID().equalsIgnoreCase(id)) {
            System.out.println("Doctor found:");
            System.out.println(doctors[i]);
            found = true;
            break;
        }
    }
    if (!found) System.out.println("Doctor not found.");
}

// Search by Specialty (from preset list)
public void searchBySpecialty() {
    if (count == 0) {
        System.out.println("No doctors available.");
        return;
    }

    Scanner sc = new Scanner(System.in);

    // Show preset list
    System.out.println("\n--- Select Specialty to Search ---");
    for (int i = 0; i < specialties.length; i++) {
        System.out.println((i + 1) + ". " + specialties[i]);
    }
    System.out.print("Enter choice: ");
    int choice = sc.nextInt();
    sc.nextLine(); // consume newline

    if (choice < 1 || choice > specialties.length) {
        System.out.println("Invalid choice.");
        return;
    }

    String selectedSpecialty = specialties[choice - 1];
    boolean found = false;

    System.out.println("\nSearching for doctors with specialty: " + selectedSpecialty);
    for (int i = 0; i < count; i++) {
        if (doctors[i].getDocSpecialty().equalsIgnoreCase(selectedSpecialty)) {
            System.out.println(doctors[i]);
            found = true;
        }
    }

    if (!found) {
        System.out.println("No doctors found with specialty: " + selectedSpecialty);
    }
}

// Search by Language
public void searchByLanguage() {
    if (count == 0) {
        System.out.println("No doctors available.");
        return;
    }

    Scanner sc = new Scanner(System.in);
    System.out.print("Enter language to search: ");
    String language = sc.nextLine();

    boolean found = false;
    for (int i = 0; i < count; i++) {
        if (doctors[i].getDocLanguages().toLowerCase().contains(language.toLowerCase())) {
            System.out.println(doctors[i]);
            found = true;
        }
    }
    if (!found) System.out.println("No doctors found with language: " + language);
}



// Report: Doctor Specialty Report
public void generateSpecialtyReport() {
    if (count == 0) {
        System.out.println("No doctors in the system.");
        return;
    }

    System.out.println("\n---- Doctor Specialty Report ----");

    // Use a HashMap to count doctors by specialty
    java.util.HashMap<String, Integer> specialtyCount = new java.util.HashMap<>();

    for (int i = 0; i < count; i++) {
        String specialty = doctors[i].getDocSpecialty();
        specialtyCount.put(specialty, specialtyCount.getOrDefault(specialty, 0) + 1);
    }

    // Print report
    for (String specialty : specialtyCount.keySet()) {
        System.out.println(specialty + " : " + specialtyCount.get(specialty));
    }

    System.out.println("Total Doctors: " + count);
}

// Report: Doctor Directory Report
public void generateDoctorDirectory() {
    if (count == 0) {
        System.out.println("No doctors in the system.");
        return;
    }

    System.out.println("\n---- Doctor Directory ----");
    for (int i = 0; i < count; i++) {
        Doctors d = doctors[i];
        System.out.println("ID: " + d.getDocID()
                + " | Name: " + d.getDocName()
                + " | Specialty: " + d.getDocSpecialty()
                + " | Phone: " + d.getDocNumber()
                + " | Email: " + d.getDocEmail());
    }
}

// Sort submenu
public void sortDoctors() {
    if (count == 0) {
        System.out.println("No doctors available to sort.");
        return;
    }

    Scanner sc = new Scanner(System.in);
    int choice;

    do {
        System.out.println("\n--- Sort Doctors ---");
        System.out.println("1. Sort by Specialty");
        System.out.println("2. Sort by Languages");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter choice: ");
        choice = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (choice) {
            case 1:
                sortBySpecialty();
                break;
            case 2:
                sortByLanguages();
                break;
            case 3:
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }

    } while (choice != 3);
}

// Sort doctors by specialty
public void sortBySpecialty() {
    if (count == 0) {
        System.out.println("No doctors available.");
        return;
    }

    Scanner sc = new Scanner(System.in);

    // Show preset list
    System.out.println("\n--- Select Specialty ---");
    for (int i = 0; i < specialties.length; i++) {
        System.out.println((i + 1) + ". " + specialties[i]);
    }
    System.out.print("Enter choice: ");
    int choice = sc.nextInt();
    sc.nextLine(); // consume newline

    if (choice < 1 || choice > specialties.length) {
        System.out.println("Invalid choice.");
        return;
    }

    String selectedSpecialty = specialties[choice - 1];
    boolean found = false;

    System.out.println("\nDoctors with specialty: " + selectedSpecialty);
    for (int i = 0; i < count; i++) {
        if (doctors[i].getDocSpecialty().equalsIgnoreCase(selectedSpecialty)) {
            System.out.println(doctors[i]);
            found = true;
        }
    }

    if (!found) {
        System.out.println("No doctors found with specialty: " + selectedSpecialty);
    }
}


// Sort doctors by languages
public void sortByLanguages() {
    for (int i = 0; i < count - 1; i++) {
        for (int j = 0; j < count - i - 1; j++) {
            if (doctors[j].getDocLanguages().compareToIgnoreCase(doctors[j + 1].getDocLanguages()) > 0) {
                Doctors temp = doctors[j];
                doctors[j] = doctors[j + 1];
                doctors[j + 1] = temp;
            }
        }
    }
    System.out.println("\nDoctors sorted by languages:");
    listDoctors();
}


}
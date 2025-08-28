package control;

import adt.ListInterface;
import adt.MyLinkedList;
import entity.PharmacyMedicine;

import java.time.LocalDate;
import java.util.Scanner;
public class PharmacyManager {
    
    private MyLinkedList<PharmacyMedicine> medicineList = new MyLinkedList<>();
private int medicineCounter = 1;
    public PharmacyManager() {
   
        medicineList.add(new PharmacyMedicine("M001", "Paracetamol", "Tablet", 10, LocalDate.of(2025, 12, 31), 2.00));
        medicineList.add(new PharmacyMedicine("M002", "Panadol", "Tablet", 100, LocalDate.of(2025, 12, 31), 5.00));
        medicineList.add(new PharmacyMedicine("M003", "Cetirizine", "Tablet", 100, LocalDate.of(2025, 12, 31), 5.00));
        medicineList.add(new PharmacyMedicine("M004", "Bilastine", "Tablet", 100, LocalDate.of(2025, 12, 31), 5.00));
        medicineList.add(new PharmacyMedicine("M005", "Gaviscon", "Liquid", 100, LocalDate.of(2025, 12, 31), 10.00));
        medicineList.add(new PharmacyMedicine("M006", "Pantoprazole", "Tablet", 100, LocalDate.of(2025, 12, 31), 20.00));
        medicineList.add(new PharmacyMedicine("M007", "Counterpain Cool Gel", "Ointment", 100, LocalDate.of(2025, 12, 31), 25.50));
        medicineList.add(new PharmacyMedicine("M008", "Cough Syrup", "Liquid", 100, LocalDate.of(2025, 12, 31), 20.50));
        
        updateCounterFromExisting();
}
    // Add new medicine
    public void addMedicine(PharmacyMedicine medicine) {
        medicineList.add(medicine);
    }
    
    //auto generate medicine id
    public String generateNewMedicineID() {
    return String.format("M%03d", medicineCounter++);
}
    public void updateCounterFromExisting() {
    for (int i = 1; i <= medicineList.getLength(); i++) {
        PharmacyMedicine t = medicineList.getEntry(i); // index starts from 1
        String id = t.getMedicineID().substring(1); 
        int num = Integer.parseInt(id);
        if (num >= medicineCounter) {
            medicineCounter = num + 1;
        }
    }
}

    // Display all medicines
    public void displayAllMedicines() {
    if (medicineList.isEmpty()) {
        System.out.println("No medicines available.");
        return;
    }

    int threshold = 10; 
    System.out.println("\n--- Pharmacy Medicine List ---");

    for (int i = 1; i <= medicineList.getLength(); i++) {
        PharmacyMedicine med = medicineList.getEntry(i);

        // Check low stock
        if (med.getQuantityInStock() <= threshold) {
            // Print in RED
            System.out.printf("%d. \u001B[31m%s\u001B[0m (ID: %s, Type: %s, Price: RM %.2f, \u001B[31mStock: %d\u001B[0m) <-- \u001B[31mLOW STOCK\u001B[0m\n",
        i, med.getName(), med.getMedicineID(), med.getType(),
        med.getPricePerUnit(), med.getQuantityInStock());

        } else {
            // Normal display
            System.out.printf("%d. %s (ID: %s, Type: %s, Price: RM %.2f, Stock: %d)\n",
                    i, med.getName(), med.getMedicineID(), med.getType(),
                    med.getPricePerUnit(), med.getQuantityInStock());
        }
    }
}


    // Search by Medicine ID
    public PharmacyMedicine searchMedicineByID(String id) {
        for (int i = 1; i <= medicineList.getLength(); i++) {
            PharmacyMedicine med = medicineList.getEntry(i);
            if (med.getMedicineID().trim().equalsIgnoreCase(id.trim())) {
                return med;
            }
        }
        return null;
    }

    // Remove by Medicine ID
    public boolean removeMedicineByID(String id) {
        for (int i = 1; i <= medicineList.getLength(); i++) {
            if (medicineList.getEntry(i).getMedicineID().equalsIgnoreCase(id)) {
                medicineList.remove(i);
                return true;
            }
        }
        return false;
    }

    // Update stock quantity
    public boolean updateStock(String id, int newQuantity) {
        PharmacyMedicine med = searchMedicineByID(id);
        if (med != null) {
            med.setQuantityInStock(newQuantity);
            return true;
        }
        return false;
    }

    // Update price
    public boolean updatePrice(String id, double newPrice) {
        PharmacyMedicine med = searchMedicineByID(id);
        if (med != null) {
            med.setPricePerUnit(newPrice);
            return true;
        }
        return false;
    }
    public boolean updateMedicine(String id, int newQty, double newPrice) {
    for (int i = 1; i <= medicineList.getLength(); i++) {
        PharmacyMedicine med = medicineList.getEntry(i);
        if (med.getMedicineID().equalsIgnoreCase(id)) {
            if (newQty >= 0) { // only update if valid
                med.setQuantityInStock(newQty);
            }
            if (newPrice >= 0) { // only update if valid
                med.setPricePerUnit(newPrice);
            }
            return true;
        }
    }
    return false;
}
    public PharmacyMedicine suggestAndSelectMedicine(String diagnosis) {
        Scanner sc = new Scanner(System.in);

        // Suggestion based on diagnosis
        System.out.println("\n--- Suggested Medicine Based on Diagnosis ---");
        if (diagnosis.toLowerCase().contains("fever") || diagnosis.toLowerCase().contains("flu")) {
            System.out.println("Suggested: Paracetamol / Panadol");
        } else if (diagnosis.toLowerCase().contains("allergy")) {
            System.out.println("Suggested: Cetirizine");
        } else {
            System.out.println("No specific suggestion. Please choose from the list.");
        }

        // Display all medicines
        displayAllMedicines();

        // User selection
        PharmacyMedicine selectedMedicine = null;
        while (true) {
            System.out.print("\nEnter the number of the medicine you want: ");
            int choice = sc.nextInt();

            if (choice >= 1 && choice <= medicineList.getLength()) {
                selectedMedicine = medicineList.getEntry(choice);
                if (selectedMedicine.getQuantityInStock() > 0) {
                    System.out.println("You selected: " + selectedMedicine.getName());
                    return selectedMedicine;
                } else {
                    System.out.println("Selected medicine is out of stock. Please choose another.");
                }
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }
public int getMedicineCount() {
    return medicineList.getLength();
}

public PharmacyMedicine getMedicineAt(int index) {
    return medicineList.getEntry(index);
}


public ListInterface<PharmacyMedicine> getMedicineList() {
    return medicineList;
}


}

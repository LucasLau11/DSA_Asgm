package boundary;

import control.PharmacyManager;
import entity.PharmacyMedicine;

import java.time.LocalDate;
import java.util.Scanner;

public class PharmacyUI {
    private PharmacyManager manager = new PharmacyManager();
    private Scanner sc = new Scanner(System.in);
public PharmacyUI(PharmacyManager manager) {
        this.manager = manager;  // shared instance
    }
    public void menu() {

        

        int choice = -1;
        do {
            System.out.println("\n==== Pharmacy Management ====");
            System.out.println("1. Display All Medicines");
            System.out.println("2. Add Medicine");
            System.out.println("3. Search by Medicine ID");
            System.out.println("4. Remove Medicine");
            System.out.println("5. Update Stock");
            System.out.println("6. Update Price");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine(); 
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> manager.displayAllMedicines();
                case 2 -> addMedicineUI();
                case 3 -> searchMedicineUI();
                case 4 -> removeMedicineUI();
                case 5 -> updateStockUI();
                case 6 -> updatePriceUI();
                case 0 -> System.out.println("Goodbye.");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void addMedicineUI() {
        System.out.print("Medicine ID (format M001): ");
        String id = sc.nextLine();
        if (!id.matches("^M\\d{3}$")) {
            System.out.println("Invalid ID format.");
            return;
        }

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Type (Tablet/Liquid/etc): ");
        String type = sc.nextLine();

        System.out.print("Quantity In Stock: ");
        int qty = sc.nextInt();
        sc.nextLine();

        System.out.print("Expiry Date (YYYY-MM-DD): ");
        LocalDate expiry;
        try {
            expiry = LocalDate.parse(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }

        System.out.print("Price Per Unit: ");
        double price = sc.nextDouble();
        sc.nextLine();

        manager.addMedicine(new PharmacyMedicine(id, name, type, qty, expiry, price));
        System.out.println("Medicine added successfully.");
    }

    private void searchMedicineUI() {
        System.out.print("Enter Medicine ID: ");
        PharmacyMedicine med = manager.searchMedicineByID(sc.nextLine());
        if (med != null)
            System.out.println("Found: " + med);
        else
            System.out.println("Medicine not found.");
    }

    private void removeMedicineUI() {
        System.out.print("Enter Medicine ID to remove: ");
        boolean removed = manager.removeMedicineByID(sc.nextLine());
        System.out.println(removed ? "Medicine removed." : "Medicine not found.");
    }

    private void updateStockUI() {
        System.out.print("Enter Medicine ID: ");
        String id = sc.nextLine();
        System.out.print("New Quantity: ");
        int qty = sc.nextInt();
        sc.nextLine();
        boolean success = manager.updateStock(id, qty);
        System.out.println(success ? "Stock updated." : "Medicine not found.");
    }

    private void updatePriceUI() {
        System.out.print("Enter Medicine ID: ");
        String id = sc.nextLine();
        System.out.print("New Price: ");
        double price = sc.nextDouble();
        sc.nextLine();
        boolean success = manager.updatePrice(id, price);
        System.out.println(success ? "Price updated." : "Medicine not found.");
    }
}

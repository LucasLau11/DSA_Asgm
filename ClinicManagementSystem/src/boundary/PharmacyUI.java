/*@author woo yee ping*/
package boundary;

import adt.ListInterface;
import control.PharmacyManager;
import entity.PharmacyMedicine;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class PharmacyUI {

    private PharmacyManager manager = new PharmacyManager();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

    private Scanner sc = new Scanner(System.in);

    public PharmacyUI(PharmacyManager manager) {
        this.manager = manager;  // shared instance
    }

    public void menu() {
        int choice = -1;
        do {
            System.out.println("\n==== Pharmacy Management ====");
            System.out.println("1. Display All Medicines");//done
            System.out.println("2. Add Medicine");//done validation
            System.out.println("3. Search by Medicine");//done
            System.out.println("4. Remove Medicine");//done
            System.out.println("5. Update Medicine");//done
            System.out.println("6. Pharmacy Summary Report");
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
                case 1 ->
                    displayAllMedicines();
                case 2 ->
                    addMedicineUI();
                case 3 ->
                    searchMedicineUI();
                case 4 ->
                    removeMedicineUI();
                case 5 ->
                    updateMedicineUI();
                case 6 ->
                    pharmacySummaryUI();
                case 0 ->
                    System.out.println("Goodbye.");
                default ->
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    public void getAllMeds() {
        displayAllMedicines();
    }

    private void displayAllMedicines() {
        ListInterface<PharmacyMedicine> medicineList = manager.getMedicineList();
        if (medicineList.isEmpty()) {
            System.out.println("No medicines available.");
            return;
        }

        final int PAGE_SIZE = 5; // items per page
        int totalItems = medicineList.getLength();
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        int currentPage = 1;

        while (true) {
            System.out.println("\n=== Pharmacy Medicine List (Page " + currentPage + "/" + totalPages + ") ===");

            // Header row
            System.out.printf("%-4s %-20s %-10s %-12s %-12s %-10s\n",
                    "No.", "Name", "ID", "Type", "Price(RM)", "Stock");
            System.out.println("--------------------------------------------------------------------------");

            int start = (currentPage - 1) * PAGE_SIZE + 1;   // 1-based index
            int end = Math.min(start + PAGE_SIZE - 1, totalItems);

            for (int i = start; i <= end; i++) {
                PharmacyMedicine med = medicineList.getEntry(i);

                if (med.getQuantityInStock() <= 10) {
                    // Highlight low stock in red
                    System.out.printf("%-4d \u001B[31m%-20s\u001B[0m %-10s %-12s RM %-10.2f \u001B[31m%-10d LOW STOCK\u001B[0m\n",
                            i, med.getName(), med.getMedicineID(), med.getType(),
                            med.getPricePerUnit(), med.getQuantityInStock());
                } else {
                    System.out.printf("%-4d %-20s %-10s %-12s RM %-10.2f %-10d\n",
                            i, med.getName(), med.getMedicineID(), med.getType(),
                            med.getPricePerUnit(), med.getQuantityInStock());
                }
            }

            // Pagination controls
            if (totalPages > 1) {
                System.out.println("--------------------------------------------------------------------------");
                System.out.println("[N] Next Page | [P] Previous Page | [Q] Quit");
                System.out.print("Choose an option: ");
                String input = sc.nextLine().trim().toUpperCase();

                if (input.equals("N") && currentPage < totalPages) {
                    currentPage++;
                } else if (input.equals("P") && currentPage > 1) {
                    currentPage--;
                } else if (input.equals("Q")) {
                    break;
                } else {
                    System.out.println("Invalid option or no more pages.");
                }
            } else {
                break; // Only one page
            }
        }
    }

    public void getAddMeds(){
        addMedicineUI();
    }
    
    private void addMedicineUI() {
        String medicineID = manager.generateNewMedicineID();
        System.out.println("Auto-generated Medicine ID: " + medicineID);

        System.out.print("Name: ");
        String name = sc.nextLine();

        String[] types = {"Tablet", "Liquid", "Capsule", "Injection", "Ointment"};
        System.out.println("Select Medicine Type:");
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Enter choice (1-" + types.length + "): ");

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
            if (choice < 1 || choice > types.length) {
                System.out.println("Invalid choice.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        String type = types[choice - 1];

        int qty;
        while (true) {
            try {
                System.out.print("Quantity In Stock: ");
                qty = Integer.parseInt(sc.nextLine());
                if (qty < 0) {
                    System.out.println("Quantity cannot be negative. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        LocalDate expiry;
        while (true) {
            System.out.print("Expiry Date (YYYY-MM-DD): ");
            String input = sc.nextLine().trim();

            try {
                expiry = LocalDate.parse(input, formatter);

                if (expiry.isBefore(LocalDate.now())) {
                    System.out.println("Expiry date must be in the future. Try again.");
                } else {
                    break;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }

        }

        double price;
        while (true) {
            try {
                System.out.print("Price Per Unit (RM): ");
                price = Double.parseDouble(sc.nextLine());
                if (price < 0) {
                    System.out.println("Price cannot be negative. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        System.out.println("\nPlease confirm the details:");
        System.out.println("ID: " + medicineID);
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Quantity: " + qty);
        System.out.println("Expiry Date: " + expiry);
        System.out.println("Price (RM): " + price);

        System.out.print("Confirm add? (Y/N): ");
        String confirm = sc.nextLine().trim().toUpperCase();

        if (!confirm.equals("Y")) {
            System.out.println("Medicine addition cancelled.");
            return;
        }
        manager.addMedicine(new PharmacyMedicine(medicineID, name, type, qty, expiry, price));
        System.out.println("Medicine added successfully.");
    }

    public void getSearchMeds(){
        searchMedicineUI();
    }
    
    private void searchMedicineUI() {
        while (true) {
            System.out.print("Enter Medicine ID (or type 'exit' to go back): ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Returning to main menu...");
                break;
            }

            PharmacyMedicine med = manager.searchMedicineByID(input);
            if (med != null) {
                System.out.println("Found: " + med);
            } else {
                System.out.println("Medicine not found. Try again.");
            }
        }
    }

    public void getRemMeds(){
        removeMedicineUI();
    }
    
    private void removeMedicineUI() {
        while (true) {
            ListInterface<PharmacyMedicine> medicineList = manager.getMedicineList();

            if (medicineList.isEmpty()) {
                System.out.println("No medicines available to remove.");
                return;
            }

            // Display all medicines with numbering
            System.out.println("\n--- Remove Medicine ---");
            for (int i = 1; i <= medicineList.getLength(); i++) {
                PharmacyMedicine med = medicineList.getEntry(i);
                System.out.printf("%d. %s (ID: %s, Type: %s, Price: RM %.2f, Stock: %d)\n",
                        i, med.getName(), med.getMedicineID(), med.getType(),
                        med.getPricePerUnit(), med.getQuantityInStock());
            }
            System.out.println("0. Cancel");

            // Prompt user for choice
            System.out.print("\nEnter the number of the medicine to remove: ");
            String input = sc.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (choice == 0) {
                System.out.println("Remove cancelled.");
                break;
            }

            if (choice < 1 || choice > medicineList.getLength()) {
                System.out.println("Invalid selection. Try again.");
                continue;
            }

            PharmacyMedicine med = medicineList.getEntry(choice);

            // Confirm removal
            System.out.println("You are about to remove: " + med.getName() + " (ID: " + med.getMedicineID() + ")");
            System.out.print("Are you sure? (Y/N): ");
            String confirm = sc.nextLine().trim().toUpperCase();

            if (confirm.equals("Y")) {
                boolean removed = manager.removeMedicineByID(med.getMedicineID());
                System.out.println(removed ? "Medicine removed." : "Failed to remove medicine.");
            } else {
                System.out.println("Remove cancelled.");
            }

            // Ask if user wants to remove another
            System.out.print("\nDo you want to remove another medicine? (Y/N): ");
            String again = sc.nextLine().trim().toUpperCase();
            if (!again.equals("Y")) {
                break;
            }
        }
    }

    public void getUpdMeds(){
        updateMedicineUI();
    }
    
    private void updateMedicineUI() {
        if (manager.getMedicineCount() == 0) {
            System.out.println("No medicines available.");
            return;
        }

        while (true) {
            // Display medicines with numbers
            System.out.println("\n--- Available Medicines ---");
            for (int i = 1; i <= manager.getMedicineCount(); i++) {
                PharmacyMedicine med = manager.getMedicineAt(i);
                System.out.println(i + ". " + med.getMedicineID() + " - " + med.getName());
            }
            System.out.println("0. Cancel");

            System.out.print("Select medicine by number: ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
                if (choice == 0) {
                    System.out.println("Update cancelled.");
                    return;
                }
                if (choice < 1 || choice > manager.getMedicineCount()) {
                    System.out.println("Invalid choice. Try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            PharmacyMedicine med = manager.getMedicineAt(choice);
            System.out.println("\nUpdating " + med.getName() + " (ID: " + med.getMedicineID() + ")");

            try {
                int qty;
                while (true) {
                    System.out.print("Enter new stock (or -1 to skip): ");
                    try {
                        qty = Integer.parseInt(sc.nextLine());
                        if (qty < -1) {
                            System.out.println("Stock cannot be negative (except -1 to skip). Try again.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                }

                double price;
                while (true) {
                    System.out.print("Enter new price (or -1 to skip): ");
                    try {
                        price = Double.parseDouble(sc.nextLine());
                        if (price < -1) {
                            System.out.println("Price cannot be negative (except -1 to skip). Try again.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                }

                // Confirmation
                System.out.print("Confirm update? (Y/N): ");
                String confirm = sc.nextLine().trim().toUpperCase();
                if (!confirm.equals("Y")) {
                    System.out.println("Update cancelled.");
                    return;
                }

                boolean success = manager.updateMedicine(med.getMedicineID(), qty, price);
                System.out.println(success ? "Medicine updated successfully." : "Update failed.");

            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }

        }
    }

    public void getSumRep(){
        pharmacySummaryUI();
    }
    
    private void pharmacySummaryUI() {
        while (true) {
            System.out.println("\n=======================================");
            System.out.println("       Pharmacy Summary Report Menu");
            System.out.println("=======================================");
            System.out.println("1. Total Medicines");
            System.out.println("2. Total Stock Available");
            System.out.println("3. Highest Value Medicine");
            System.out.println("4. Low Stock Medicines (<10 units)");
            System.out.println("5. Exit Report");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("Total Medicines: " + manager.getMedicineCount());
                    break;

                case "2":
                    int totalStock = 0;
                    for (int i = 1; i <= manager.getMedicineCount(); i++) {
                        totalStock += manager.getMedicineAt(i).getQuantityInStock();
                    }
                    System.out.println("Total Stock Available: " + totalStock);
                    break;

                case "3":
                    PharmacyMedicine highest = null;
                    double highestValue = 0;
                    for (int i = 1; i <= manager.getMedicineCount(); i++) {
                        PharmacyMedicine med = manager.getMedicineAt(i);
                        double value = med.getQuantityInStock() * med.getPricePerUnit();
                        if (value > highestValue) {
                            highestValue = value;
                            highest = med;
                        }
                    }
                    if (highest != null) {
                        System.out.println("Highest Value Medicine: " + highest.getName()
                                + " (Value: RM " + String.format("%.2f", highestValue) + ")");
                    } else {
                        System.out.println("No medicines available.");
                    }
                    break;

                case "4":
                    System.out.println("Low Stock Medicines (<10 units):");
                    boolean found = false;
                    for (int i = 1; i <= manager.getMedicineCount(); i++) {
                        PharmacyMedicine med = manager.getMedicineAt(i);
                        if (med.getQuantityInStock() < 10) {
                            System.out.println("- " + med.getName() + " (ID: " + med.getMedicineID()
                                    + ", Stock: " + med.getQuantityInStock() + ")");
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println("No low stock medicines.");
                    }
                    break;

                case "5":
                    System.out.println("Exiting Pharmacy Summary Report...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

            // Nested confirmation switch
            System.out.print("\nDo you want to view another summary? (Y/N): ");
            String again = sc.nextLine().trim().toUpperCase();
            switch (again) {
                case "Y":
                    continue; // back to report menu
                case "N":
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid input, returning to main menu...");
                    return;
            }
        }
    }

}

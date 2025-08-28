/*author wooyeeping*/
package entity;

import java.time.LocalDate;

public class PharmacyMedicine {
    private String medicineID;
    private String name;
    private String type;
    private int quantityInStock;
    private LocalDate expiryDate;
    private double pricePerUnit;

    public PharmacyMedicine(String medicineID, String name, String type, int quantityInStock, LocalDate expiryDate, double pricePerUnit) {
        this.medicineID = medicineID;
        this.name = name;
        this.type = type;
        this.quantityInStock = quantityInStock;
        this.expiryDate = expiryDate;
        this.pricePerUnit = pricePerUnit;
    }

    // --- Getters and Setters ---
    public String getMedicineID() {
        return medicineID; 
    }
    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID; 
    }

    public String getName() {
        return name; 
    }
    public void setName(String name) {
        this.name = name; 
    }

    public String getType() {
        return type; 
    }
    public void setType(String type) {
        this.type = type; 
    }

    public int getQuantityInStock() {
        return quantityInStock; 
    }
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock; 
    }

    public LocalDate getExpiryDate() {
        return expiryDate; 
    }
    public void setExpiryDate(LocalDate expiryDate) { 
        this.expiryDate = expiryDate; 
    }

    public double getPricePerUnit() { 
        return pricePerUnit; 
    }
    public void setPricePerUnit(double pricePerUnit) { 
        this.pricePerUnit = pricePerUnit; 
    }

    @Override
    public String toString() {
        return String.format("%-8s %-15s %-10s %-5d %-12s RM%.2f",
                medicineID, name, type, quantityInStock, expiryDate, pricePerUnit);
    }
}

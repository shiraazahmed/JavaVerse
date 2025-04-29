package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
// A class to represent a single bank transaction (deposit or payment)
public class transaction {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;
    // Constructor to initialize a new transaction
    public transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }
    // Converts a CSV line into a Transaction object
    public static transaction FromCsv(String csvLine) {
        String[] parts = csvLine.split("\\|");
        return new transaction(
                LocalDate.parse(parts[0]), LocalTime.parse(parts[1]), parts[2], parts[3], Double.parseDouble(parts[4])
        );
    }
    // Converts this transaction into a CSV-formatted string
    public String toCsv() {
        return String.format("%s %s | %-20s | %-10s | %10.2f", date, time, description, vendor, amount);
    }
    // Returns a nicely formatted string for CLI display
    @Override
    public String toString() {
        return "com.pluralsight.transaction{" +
                "amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", time=" + time +
                ", vendor='" + vendor + '\'' +
                '}';
    }
// Getters to introduce what we need
    public LocalDate getDate() {
        return date;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

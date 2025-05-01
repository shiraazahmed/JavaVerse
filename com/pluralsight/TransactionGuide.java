package com.pluralsight;
import java.io.*;
import java.util.*;

public class TransactionGuide {
    private static final String fileName = "transactions.csv";

    // Reads all transactions from the CSV file into a list
    public static List<Transaction> readTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return transactions;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    transactions.add(Transaction.fromCsv(line));
                } catch (Exception e) {
                    System.out.println("Saved: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }
        return transactions;
    }

    // Appends a new transaction to the CSV file
    public static void saveTransaction(Transaction t) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(t.toCsv());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing transaction: " + e.getMessage());
        }
    }
}


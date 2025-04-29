package com.pluralsight;

import java.io.*;
import java.util.*;

// This class handles loading and saving transactions to/from a CSV file
public class transactionGuide {
    private static final String fileName = "transactions.csv";

    // Reads all transactions from the CSV file into a list
    public static List<transaction> readTransactions() {
        List<transaction> transactions = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return transactions;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Read each line until we reach the end of the file
            while ((line = reader.readLine()) != null) {
                // Convert the CSV line into a Transaction object
                transactions.add(transaction.FromCsv(line));
            }
        } catch (IOException e) {
            // Handle any error that occurs while trying to read the file
            System.out.println("Error reading transactions: " + e.getMessage());
        }

        return transactions;
    }

    // Appends a new transaction to the CSV file
    public static void saveTransaction(transaction t) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(t.toCsv());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing transaction: " + e.getMessage());
        }
    }

    public static void writeTransaction(transaction transaction) {
    }
}

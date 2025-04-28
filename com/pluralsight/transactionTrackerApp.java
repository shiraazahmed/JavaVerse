package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
// The main CLI application class
public class transactionTrackerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) { // Main loop
            System.out.println("Bank CLI | D=Deposit P=Payment L=Ledger E=Exit");
            System.out.print("Enter your choice: ");
            switch (scanner.nextLine().trim().toUpperCase()) {
                //allow you to pass behavior (functions) as parameters.
                case "D" -> addTransaction(scanner, true);
                case "P" -> addTransaction(scanner, false);
                case "L" -> showLedger(scanner);
                case "E" -> { System.out.println("Thank you, Please come again!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    // Prompts the user for transaction data and saves it
    private static void addTransaction(Scanner scanner, boolean isDeposit) {
        System.out.print("Description: "); String desc = scanner.nextLine();
        System.out.print("Vendor: "); String vendor = scanner.nextLine();
        System.out.print("Amount: "); double amount = Double.parseDouble(scanner.nextLine());
        if (!isDeposit) amount = -Math.abs(amount); // Ensure withdrawals are negative
        transaction transaction = new transaction(LocalDate.now(), LocalTime.now(), desc, vendor, amount);
        transactionGuide.saveTransaction(transaction);
        System.out.println("Saved!");
    }
    // Displays the ledger menu and handles filter options
    private static void showLedger(Scanner scanner) {
        List<transaction> all = transactionGuide.readTransactions();
        // Sort transactions from newest to oldest
        all.sort(Comparator.comparing(transaction::getDate).reversed().thenComparing(transaction::getTime).reversed());
        while (true) {
            System.out.println("Ledger: A=All D=Deposits P=Payments R=Reports H=Home");
            System.out.print("Choice: ");
            switch (scanner.nextLine().trim().toUpperCase()) {
                case "A" -> print(all);
                case "D" -> print(all.stream().filter(transaction -> transaction.getAmount() > 0).toList());
                case "P" -> print(all.stream().filter(transaction -> transaction.getAmount() < 0).toList());
                case "R" -> showReports(scanner, all);
                case "H" -> { return; }
                default -> System.out.println("Not available.");
            }
        }
    }

    private static void showReports(Scanner scanner, List<transaction> transactions) {
        // Handles the reports submenu
        System.out.println("\nReports: 1=MTD 2=PrevMonth 3=YTD 4=PrevYear 5=ByVendor 0=Back");
        String c = scanner.nextLine();
        LocalDate now = LocalDate.now();
        switch (c) {
            case "1" -> print(transactions.stream().filter(transaction -> transaction.getDate().getMonth() == now.getMonth() & transaction.getDate().getYear() == now.getYear()).toList());
            case "2" -> {
                LocalDate first = now.withDayOfMonth(1), lastMonth = first.minusMonths(1);
                print(transactions.stream().filter(transaction -> !transaction.getDate().isBefore(lastMonth) && transaction.getDate().isBefore(first)).toList());
            }
            case "3" -> print(transactions.stream().filter(transaction -> transaction.getDate().getYear() == now.getYear()).toList());
            case "4" -> print(transactions.stream().filter(transaction -> transaction.getDate().getYear() == now.getYear() - 1).toList());
            case "5" -> {
                //-> defines anonymous functions
                System.out.print("Vendor: ");
                String variable = scanner.nextLine().toLowerCase();
                print(transactions.stream().filter(transaction -> transaction.getVendor().toLowerCase().contains(variable)).toList());
            }
        }
    }
// Prints a list of transactions in the CLI
    private static void print(List<transaction> transactions) {
        if (transactions.isEmpty()) System.out.println("No transactions.");
        else transactions.forEach(System.out::println); //colons help me pass the function around as reference operator
    }
}

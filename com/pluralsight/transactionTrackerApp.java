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
    private static void addTransaction(Scanner sc, boolean isDeposit) {
        System.out.print("Description: "); String desc = sc.nextLine();
        System.out.print("Vendor: "); String vendor = sc.nextLine();
        System.out.print("Amount: "); double amt = Double.parseDouble(sc.nextLine());
        if (!isDeposit) amt = -Math.abs(amt); // Ensure withdrawals are negative
        transaction t = new transaction(LocalDate.now(), LocalTime.now(), desc, vendor, amt);
        transactionGuide.saveTransaction(t);
        System.out.println("Saved!");
    }
    // Displays the ledger menu and handles filter options
    private static void showLedger(Scanner sc) {
        List<transaction> all = transactionGuide.readTransactions();
        // Sort transactions from newest to oldest
        all.sort(Comparator.comparing(transaction::getDate).reversed().thenComparing(transaction::getTime).reversed());
        while (true) {
            System.out.println("Ledger: A=All D=Deposits P=Payments R=Reports H=Home");
            System.out.print("Choice: ");
            switch (sc.nextLine().trim().toUpperCase()) {
                case "A" -> print(all);
                case "D" -> print(all.stream().filter(t -> t.getAmount() > 0).toList());
                case "P" -> print(all.stream().filter(t -> t.getAmount() < 0).toList());
                case "R" -> showReports(sc, all);
                case "H" -> { return; }
                default -> System.out.println("Not available.");
            }
        }
    }

    private static void showReports(Scanner sc, List<transaction> txns) {
        // Handles the reports submenu
        System.out.println("\nReports: 1=MTD 2=PrevMonth 3=YTD 4=PrevYear 5=ByVendor 0=Back");
        String c = sc.nextLine();
        LocalDate now = LocalDate.now();
        switch (c) {
            case "1" -> print(txns.stream().filter(t -> t.getDate().getMonth() == now.getMonth() && t.getDate().getYear() == now.getYear()).toList());
            case "2" -> {
                LocalDate first = now.withDayOfMonth(1), lastMonth = first.minusMonths(1);
                print(txns.stream().filter(t -> !t.getDate().isBefore(lastMonth) && t.getDate().isBefore(first)).toList());
            }
            case "3" -> print(txns.stream().filter(t -> t.getDate().getYear() == now.getYear()).toList());
            case "4" -> print(txns.stream().filter(t -> t.getDate().getYear() == now.getYear() - 1).toList());
            case "5" -> {
                System.out.print("Vendor: ");
                String v = sc.nextLine().toLowerCase();
                print(txns.stream().filter(t -> t.getVendor().toLowerCase().contains(v)).toList());
            }
        }
    }
// Prints a list of transactions in the CLI
    private static void print(List<transaction> txns) {
        if (txns.isEmpty()) System.out.println("No transactions.");
        else txns.forEach(System.out::println); //colons help me pass the function around as reference operator
        //txns transactions
    }
}

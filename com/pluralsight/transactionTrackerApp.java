package com.pluralsight;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

public class transactionTrackerApp {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        showMainMenu();
    }
    private static void showMainMenu() {
        while (true) {
            System.out.println("====Main Menu:====");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("E) Exit");
            System.out.print("Choose Choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "D":
                    addTransaction(true);
                    break;
                case "P":
                    addTransaction(false);
                    break;
                case "L":
                    showLedger();
                    break;
                case "E":
                    System.exit(0);
                default:
                    System.out.println("Invalid Choice, Try Again!.");
            }
        }
    }
    private static void addTransaction(boolean addDeposit) {
        System.out.println(addDeposit ? "Add Deposit" : "Make Payment");
        System.out.print(" Provide Description: ");
        String description = scanner.nextLine();

        System.out.print("Provide Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Provide Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        if (!addDeposit && amount > 0) {
            amount = -amount;
        }// Ensure negative for payments

        transaction transaction = new transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);
        transactionGuide.saveTransaction(transaction);
        System.out.println("Saved Successfully!");
    }

    private static void showLedger() {
        List<transaction> transactions = transactionGuide.readTransactions();

        while (true) {
            System.out.println("Ledger Menu:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose Choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A": displayTransactions(transactions, t -> true);
                break;
                case "D": displayTransactions(transactions, t -> t.getAmount() > 0);
                break;
                case "P": displayTransactions(transactions, t -> t.getAmount() < 0);
                break;
                case "R": showReports(transactions);
                break;
                case "H":
                    return;
                default: System.out.println("Invalid choice, Try Again!");
            }
        }
    }

    private static void displayTransactions(List<transaction> list, java.util.function.Predicate<transaction> filter) {
        System.out.println("\n--- Transactions ---");
        list.stream()
                .filter(filter)
                .sorted(Comparator.comparing(transaction::getDate).reversed())
                .forEach(System.out::println);
    }

    private static void showReports(List<transaction> transactions) {
        while (true) {
            System.out.println("Reports");
            System.out.println("1 - Month To Date");
            System.out.println("2 - Previous Month");
            System.out.println("3 - Year To Date");
            System.out.println("4 - Previous Year");
            System.out.println("5 - Search by Vendor");
            System.out.println("0 - Back to Ledger");
            System.out.print("Enter your choice: ");

            String choice = transactionTrackerApp.scanner.nextLine().trim();
            LocalDate today = LocalDate.now();

            switch (choice) {
                case "1":
                    displayTransactions(transactions, t ->
                            t.getDate().getMonth() == today.getMonth() &&
                                    t.getDate().getYear() == today.getYear());
                    break;
                case "2":
                    YearMonth prevMonth = YearMonth.now().minusMonths(1);
                    displayTransactions(transactions, t ->
                            YearMonth.from(t.getDate()).equals(prevMonth));
                    break;
                case "3":
                    displayTransactions(transactions, t ->
                            t.getDate().getYear() == today.getYear());
                    break;
                case "4":
                    displayTransactions(transactions, t ->
                            t.getDate().getYear() == today.getYear() - 1);
                    break;
                case "5":
                    System.out.print("Search for Vendor Name: ");
                    String vendor = scanner.nextLine().toLowerCase();
                    displayTransactions(transactions, t ->
                            t.getVendor().toLowerCase().contains(vendor));
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid.");
            }
        }
    }
}
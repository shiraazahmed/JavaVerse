package com.pluralsight;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

public class transactionTrackerApp {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Home Menu:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("E) Exit");
            System.out.print("Choose Choice: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "D": addTransaction(true); break;
                case "P": addTransaction(false); break;
                case "L": showLedger(); break;
                case "E": System.exit(0);
                default: System.out.println("Invalid Choice, Try Again!.");
            }
        }
    }

    private static void addTransaction(boolean isDeposit) {
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        if (!isDeposit) amount = -Math.abs(amount);  // Ensure negative for payments

        transaction transaction = new transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);
        transactionGuide.writeTransaction(transaction);
        System.out.println("Transaction Saved.");
    }

    private static void showLedger() {
        List<transaction> transactions = transactionGuide.readTransactions();
        transactions.sort(Comparator.comparing(transaction::getDate).thenComparing(t -> t.getDate()).reversed());

        while (true) {
            System.out.println("Ledger:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A": showFiltered(transactions, t -> true); break;
                case "D": showFiltered(transactions, t -> t.getAmount() > 0); break;
                case "P": showFiltered(transactions, t -> t.getAmount() < 0); break;
                case "R": showReports(transactions); break;
                case "H": return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void showFiltered(List<transaction> transactions, java.util.function.Predicate<transaction> filter) {
        transactions.stream()
                .filter(filter)
                .sorted(Comparator.comparing(transaction::getDate).reversed())
                .forEach(System.out::println);
    }

    private static void showReports(List<transaction> transactions) {
        while (true) {
            System.out.println("Reports:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            LocalDate now = LocalDate.now();

            switch (choice) {
                case "1": // Month to date
                    showFiltered(transactions, t -> t.getDate().getMonth() == now.getMonth() && t.getDate().getYear() == now.getYear());
                    break;
                case "2": // Previous month
                    YearMonth prevMonth = YearMonth.now().minusMonths(1);
                    showFiltered(transactions, t -> YearMonth.from(t.getDate()).equals(prevMonth));
                    break;
                case "3": // Year to date
                    showFiltered(transactions, t -> t.getDate().getYear() == now.getYear());
                    break;
                case "4": // Previous year
                    showFiltered(transactions, t -> t.getDate().getYear() == now.getYear() - 1);
                    break;
                case "5": // Search by vendor
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine().toLowerCase();
                    showFiltered(transactions, t -> t.getVendor().toLowerCase().contains(vendor));
                    break;
                case "0": return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
}


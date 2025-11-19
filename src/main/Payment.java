package main;

import config.config;
import java.util.*;

public class Payment {

    public static void addPayment(config db) {
        Scanner sc = new Scanner(System.in);
        Delivery.viewDeliveries(db);

        System.out.print("Enter Delivery ID: ");
        int id = main.getIntInput(sc);

        System.out.println("\nPayment Methods: 1.Cash 2.GCash 3.Card");
        int type = main.getIntInput(sc);

        System.out.print("Amount Paid: ");
        double amount = sc.nextDouble();

        String sql = "INSERT INTO payment (d_id, pm_id, amount_paid) VALUES (?, ?, ?)";
        db.addRecord(sql, id, type, amount);
        System.out.println("✔ Payment Recorded!");
    }

    public static void viewPayments(config db) {
        String qry = "SELECT * FROM payment";
        String[] headers = {"Pay ID", "Delivery ID", "Method", "Amount"};
        String[] cols = {"p_id", "d_id", "pm_id", "amount_paid"};
        db.viewRecords(qry, headers, cols);
    }
}


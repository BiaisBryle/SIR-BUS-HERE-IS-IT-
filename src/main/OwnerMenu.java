package main;

import config.config;
import java.util.*;

public class OwnerMenu {

    public static void ownerMenu(config db) {
        Scanner sc = new Scanner(System.in);
        boolean exitMenu = false;

        while (!exitMenu) {
            System.out.println("\n=== OWNER MENU ===");
            System.out.println("1. Add Delivery");
            System.out.println("2. View Deliveries");
            System.out.println("3. Update Delivery");
            System.out.println("4. Delete Delivery");
            System.out.println("5. Add Payment");
            System.out.println("6. View Payments");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choice: ");
            int c = main.getIntInput(sc);
            sc.nextLine(); // consume leftover newline

            switch (c) {
                case 1:
                    addDeliveryWithWorker(db, sc);
                    break;
                case 2:
                    Delivery.viewDeliveries(db);
                    break;
                case 3:
                    Delivery.updateDelivery(db, sc);
                    break;
                case 4:
                    Delivery.deleteDelivery(db, sc);
                    break;
                case 5:
                    Payment.addPayment(db);
                    break;
                case 6:
                    Payment.viewPayments(db);
                    break;
                case 7:
                    System.out.println("Returning to Main Menu...");
                    exitMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    public static void addDeliveryWithWorker(config db, Scanner sc) {
        System.out.print("Receiver Name: ");
        String name = sc.nextLine();

        System.out.print("Address: ");
        String address = sc.nextLine();

        System.out.print("Contact Number: ");
        String contact = sc.nextLine();

        System.out.print("Item: ");
        String item = sc.nextLine();

        System.out.print("Delivery Date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        System.out.println("\nAvailable Workers:");
        List<Map<String, Object>> workers = db.fetchRecords(
                "SELECT * FROM tbl_user WHERE u_type='Worker' AND u_status='Approved'");
        for (Map<String, Object> w : workers) {
            System.out.println(w.get("u_id") + ". " + w.get("u_name"));
        }

        System.out.print("Assign to Worker ID: ");
        int workerId = main.getIntInput(sc);
        sc.nextLine(); // consume leftover newline

        String sql = "INSERT INTO deliver (d_name, d_address, d_contact, d_item, d_date, d_status, worker_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        db.addRecord(sql, name, address, contact, item, date, "Pending", workerId);

        System.out.println("✔ Delivery added and assigned to Worker ID " + workerId);
    }
}

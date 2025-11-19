package main;

import config.config;
import java.util.*;

public class WorkerMenu {

    public static void workerMenu(config db, int workerId) {
        Scanner sc = new Scanner(System.in);
        boolean exitMenu = false;

        while (!exitMenu) {
            System.out.println("\n=== WORKER MENU ===");
            System.out.println("1. View My Deliveries");
            System.out.println("2. Mark Delivery as Delivered");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choice: ");
            int c = main.getIntInput(sc);
            sc.nextLine(); // consume leftover newline

            switch (c) {
                case 1:
                    viewMyDeliveries(db, workerId);
                    break;
                case 2:
                    markMyDelivery(db, workerId, sc);
                    break;
                case 3:
                    System.out.println("Returning to Main Menu...");
                    exitMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    public static void viewMyDeliveries(config db, int workerId) {
        String qry = "SELECT * FROM deliver WHERE worker_id=" + workerId;
        String[] headers = {"ID", "Receiver", "Address", "Contact", "Item", "Date", "Status"};
        String[] cols = {"d_id", "d_name", "d_address", "d_contact", "d_item", "d_date", "d_status"};
        db.viewRecords(qry, headers, cols);
    }

    public static void markMyDelivery(config db, int workerId, Scanner sc) {
        System.out.print("Enter Delivery ID to Mark Delivered: ");
        int deliveryId = main.getIntInput(sc);
        sc.nextLine(); // consume leftover newline

        // Check if the delivery is assigned to this worker
        List<Map<String, Object>> res = db.fetchRecords(
                "SELECT * FROM deliver WHERE d_id=? AND worker_id=?", deliveryId, workerId);

        if (res.isEmpty()) {
            System.out.println("This delivery is not assigned to you!");
            return;
        }

        // Update the status to Delivered
        db.updateRecord(
                "UPDATE deliver SET d_status='Delivered' WHERE d_id=? AND worker_id=?",
                deliveryId, workerId);

        System.out.println("✔ Delivery marked as Delivered!");
    }
}

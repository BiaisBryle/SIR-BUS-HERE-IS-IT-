    package main;

    import config.config;
    import java.util.*;

    public class Delivery {

        public static void viewDeliveries(config db) {
            String qry = "SELECT * FROM deliver ORDER BY d_id";
            String[] headers = {"ID", "Receiver", "Address", "Contact", "Item", "Date", "Status", "Worker ID"};
            String[] cols = {"d_id", "d_name", "d_address", "d_contact", "d_item", "d_date", "d_status", "worker_id"};
            db.viewRecords(qry, headers, cols);
        }

        public static void updateDelivery(config db, Scanner sc) {
            viewDeliveries(db);

            System.out.print("Enter Delivery ID to Update: ");
            int id = main.getIntInput(sc);
            sc.nextLine();

            System.out.print("New Receiver Name: ");
            String n1 = sc.nextLine();
            System.out.print("New Address: ");
            String n2 = sc.nextLine();
            System.out.print("New Contact: ");
            String n3 = sc.nextLine();
            System.out.print("New Item: ");
            String n4 = sc.nextLine();

            String sql = "UPDATE deliver SET d_name=?, d_address=?, d_contact=?, d_item=? WHERE d_id=?";
            db.updateRecord(sql, n1, n2, n3, n4, id);
            System.out.println("✔ Delivery Updated!");
        }

        public static void deleteDelivery(config db, Scanner sc) {
            viewDeliveries(db);

            System.out.print("Enter Delivery ID to Delete: ");
            int id = main.getIntInput(sc);
            db.deleteRecord("DELETE FROM deliver WHERE d_id=?", id);
            System.out.println("✔ Delivery Deleted!");
        }
    }

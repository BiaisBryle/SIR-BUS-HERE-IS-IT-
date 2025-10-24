package main;

import config.config;
import java.util.Scanner;

public class main {
    
    // VIEW DELIVERIES
    public static void viewDeliveries(config db) {
        String qry = "SELECT * FROM deliver";
        String[] headers = {"ID", "Receiver Name", "Address", "Contact", "Item", "Delivery Date", "Status"};
        String[] columns = {"d_id", "d_name", "d_address", "d_contact", "d_item", "d_date", "d_status"};
        db.viewRecords(qry, headers, columns);
    }

    // ADD DELIVERY
    public static void addDelivery(config db) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Receiver's Name: ");
        String name = sc.nextLine();

        System.out.print("Address: ");
        String address = sc.nextLine();

        System.out.print("Contact Number: ");
        String contact = sc.nextLine();

        System.out.print("Item to Deliver: ");
        String item = sc.nextLine();

        System.out.print("Delivery Date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        String sql = "INSERT INTO deliver (d_name, d_address, d_contact, d_item, d_date, d_status) VALUES (?, ?, ?, ?, ?, ?)";
        db.addRecord(sql, name, address, contact, item, date, "Pending");

        System.out.println("Delivery added successfully.");
    }

    // UPDATE DELIVERY
    public static void updateDelivery(config db) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Delivery ID (d_id) to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Receiver Name: ");
        String newName = sc.nextLine();

        System.out.print("Enter New Address: ");
        String newAddress = sc.nextLine();

        System.out.print("Enter New Contact Number: ");
        String newContact = sc.nextLine();

        System.out.print("Enter New Item Description: ");
        String newItem = sc.nextLine();

        String updateQry = "UPDATE deliver SET d_name = ?, d_address = ?, d_contact = ?, d_item = ? WHERE d_id = ?";
        db.updateRecord(updateQry, newName, newAddress, newContact, newItem, id); 

        System.out.println("Delivery updated successfully.");
    }

    // DELETE DELIVERY
    public static void deleteDelivery(config db) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Delivery ID (d_id) to delete: ");
        int deleteID = sc.nextInt();
        sc.nextLine();

        String delQry = "DELETE FROM deliver WHERE d_id = ?";
        db.deleteRecord(delQry, deleteID);

        System.out.println("Delivery deleted successfully.");
    }

    // ============================
    // 💳 ADD PAYMENT METHOD
    // ============================
    public static void addPayment(config db) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Delivery ID (d_id): ");
        int deliveryId = sc.nextInt();

        System.out.println("Select Payment Method: ");
        System.out.println("1. Cash");
        System.out.println("2. GCash");
        System.out.println("3. Credit/Debit Card");
        System.out.print("Enter choice: ");
        int pmId = sc.nextInt();

        System.out.print("Enter Amount Paid: ");
        double amount = sc.nextDouble();

        String sql = "INSERT INTO payment (d_id, pm_id, amount_paid) VALUES (?, ?, ?)";
        db.addRecord(sql, deliveryId, pmId, amount);

        System.out.println("✅ Payment recorded successfully!");
    }

    // VIEW PAYMENTS
    public static void viewPayments(config db) {
        String qry = "SELECT * FROM payment";
        String[] headers = {"Payment ID", "Delivery ID", "Payment Method ID", "Amount Paid"};
        String[] columns = {"p_id", "d_id", "pm_id", "amount_paid"};
        db.viewRecords(qry, headers, columns);
    }

    // MAIN MENU
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        config db = new config();
        db.connectDB();

        int mainChoice;
        char contMain;

        do {
            System.out.println("\n===== DELIVERY MANAGEMENT SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            mainChoice = sc.nextInt();

            switch (mainChoice) {
                case 1:
                    loginMenu(db);
                    break;
                case 2:
                    registerMenu(db);
                    break;
                case 3:
                    System.out.println("Exiting program.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            System.out.print("Do you want to return to MAIN MENU? (Y/N): ");
            contMain = sc.next().charAt(0);

        } while (contMain == 'Y' || contMain == 'y');

        System.out.println("Thank you! Program ended.");
    }

    // LOGIN MENU
    public static void loginMenu(config db) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Email: ");
        String email = sc.next();
        System.out.print("Enter Password: ");
        String password = sc.next();

        String qry = "SELECT * FROM tbl_user WHERE u_email = ? AND u_password = ?";
        java.util.List<java.util.Map<String, Object>> result = db.fetchRecords(qry, email, password);

        if (result.isEmpty()) {
            System.out.println("Invalid Credentials.");
            return;
        }

        java.util.Map<String, Object> user = result.get(0);
        String stat = user.get("u_status").toString();
        String type = user.get("u_type").toString();

        if (stat.equalsIgnoreCase("Pending")) {
            System.out.println("Account is pending approval. Please contact the admin.");
            return;
        }

        System.out.println("Login successful!");

        if (type.equalsIgnoreCase("SuperAdmin")) {
            superAdminMenu(db);
        } else if (type.equalsIgnoreCase("Owner")) {
            ownerMenu(db);
        } else if (type.equalsIgnoreCase("Worker")) {
            workerMenu(db);
        } else {
            System.out.println("Invalid user type.");
        }
    }

    // SUPER ADMIN MENU
    public static void superAdminMenu(config db) {
        Scanner sc = new Scanner(System.in);
        char cont;
        do {
            System.out.println("\n=== SUPER ADMIN MENU ===");
            System.out.println("1. Approve Account");
            System.out.println("2. View Users");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewUsers();
                    System.out.print("Enter ID to Approve: ");
                    int id = sc.nextInt();
                    String sql = "UPDATE tbl_user SET u_status = ? WHERE u_id = ?";
                    db.updateRecord(sql, "Approved", id);
                    System.out.println("User approved successfully!");
                    break;
                case 2:
                    viewUsers();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            System.out.print("Return to Super Admin Menu? (Y/N): ");
            cont = sc.next().charAt(0);

        } while (cont == 'Y' || cont == 'y');
    }

    // OWNER MENU (UPDATED ✅)
    public static void ownerMenu(config db) {
        Scanner sc = new Scanner(System.in);
        char cont;
        do {
            System.out.println("\n=== OWNER MENU ===");
            System.out.println("1. Add Delivery");
            System.out.println("2. View Deliveries");
            System.out.println("3. Update Delivery");
            System.out.println("4. Delete Delivery");
            System.out.println("5. Add Payment");
            System.out.println("6. View Payments");
            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addDelivery(db);
                    break;
                case 2:
                    viewDeliveries(db);
                    break;
                case 3: 
                    viewDeliveries(db);
                    updateDelivery(db);
                    break;
                case 4:
                    viewDeliveries(db);
                    deleteDelivery(db);
                    break;
                case 5:
                    viewDeliveries(db);
                    addPayment(db);
                    break;
                case 6:
                    viewPayments(db);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            System.out.print("Return to Owner Menu? (Y/N): ");
            cont = sc.next().charAt(0);
        } while (cont == 'Y' || cont == 'y');
    }

    // WORKER MENU
    public static void workerMenu(config db) {
        Scanner sc = new Scanner(System.in);
        char cont;
        do {
            System.out.println("\n=== WORKER MENU ===");
            System.out.println("1. View Deliveries");
            System.out.println("2. Update Delivery Status");
            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewDeliveries(db);
                    break;
                case 2: 
                    viewDeliveries(db);
                    System.out.print("Enter Delivery ID to Update Status: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter New Status (Pending/In Transit/Delivered): ");
                    String status = sc.nextLine();
                    String sql = "UPDATE deliver SET d_status = ? WHERE d_id = ?";
                    db.updateRecord(sql, status, id);
                    System.out.println("Delivery status updated!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            System.out.print("Return to Worker Menu? (Y/N): ");
            cont = sc.next().charAt(0);
        } while (cont == 'Y' || cont == 'y');
    }

    // REGISTER MENU
    public static void registerMenu(config db) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Name:");
        String name = sc.next();
        System.out.print("Enter Email:");
        String newEmail = sc.next();

        String checkQry = "SELECT * FROM tbl_user WHERE u_email = ?";
        java.util.List<java.util.Map<String, Object>> exists = db.fetchRecords(checkQry, newEmail);
        if (!exists.isEmpty()) {
            System.out.println("Email already exists!");
            return;
        }

        System.out.print("Enter Password:");
        String newPassword = sc.next();

        // Check if there’s already a Super Admin
        String countQry = "SELECT COUNT(*) AS cnt FROM tbl_user";
        java.util.List<java.util.Map<String, Object>> countRes = db.fetchRecords(countQry);
        int count = Integer.parseInt(countRes.get(0).get("cnt").toString());

        String userType;
        String status;

        if (count == 0) {
            userType = "SuperAdmin";
            status = "Approved";
            System.out.println("You are the first registered user. You are now the Super Admin!");
        } else {
            System.out.println("Enter User Type (1 - Owner, 2 - Worker):");
            int typeChoice = sc.nextInt();

            switch (typeChoice) {
                case 1:
                    userType = "Owner";
                    break;
                case 2:
                    userType = "Worker";
                    break;
                default:
                    userType = "Worker";
            }
            status = "Pending";
        }

        String regSql = "INSERT INTO tbl_user (u_name, u_email, u_type, u_status, u_password) VALUES (?, ?, ?, ?, ?)";
        db.addRecord(regSql, name, newEmail, userType, status, newPassword);
        System.out.println("Registration successful!");
    }

    // VIEW USERS
    public static void viewUsers() {
        String qry = "SELECT * FROM tbl_user";
        String[] headers = {"ID", "Name", "Email", "Type", "Status"};
        String[] columns = {"u_id", "u_name", "u_email", "u_type", "u_status"};
        config db = new config();
        db.viewRecords(qry, headers, columns);
    }
}

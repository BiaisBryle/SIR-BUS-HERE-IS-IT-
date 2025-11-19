package main;

import config.config;
import java.util.*;

public class main {

    public static int getIntInput(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    public static void loginMenu(config db) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        List<Map<String, Object>> res = db.fetchRecords(
                "SELECT * FROM tbl_user WHERE u_email=? AND u_password=?", email, pass);

        if (res.isEmpty()) {
            System.out.println("Invalid credentials!");
            return;
        }

        Map<String, Object> user = res.get(0);
        if (!user.get("u_status").toString().equals("Approved")) {
            System.out.println("Account pending approval.");
            return;
        }

        String type = user.get("u_type").toString();
        System.out.println("✔ Login Successful!");

        switch (type) {
            case "SuperAdmin": superAdminMenu.superAdminMenu(db); break;
            case "Owner": OwnerMenu.ownerMenu(db); break;
            case "Worker":
                int workerId = Integer.parseInt(user.get("u_id").toString());
                WorkerMenu.workerMenu(db, workerId);
                break;
        }
    }

    public static void registerMenu(config db) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        List<Map<String, Object>> exists = db.fetchRecords("SELECT * FROM tbl_user WHERE u_email=?", email);
        if (!exists.isEmpty()) {
            System.out.println("Email already exists!");
            return;
        }

        System.out.print("Password: ");
        String pass = sc.nextLine();

        List<Map<String, Object>> cntRes = db.fetchRecords("SELECT COUNT(*) AS cnt FROM tbl_user");
        int count = Integer.parseInt(cntRes.get(0).get("cnt").toString());

        String type, status;
        if (count == 0) {
            type = "SuperAdmin";
            status = "Approved";
            System.out.println("✔ You are now the Super Admin!");
        } else {
            System.out.println("User Type: 1.Owner 2.Worker");
            int t = getIntInput(sc);
            type = (t == 1) ? "Owner" : "Worker";
            status = "Pending";
        }

        db.addRecord("INSERT INTO tbl_user (u_name, u_email, u_type, u_status, u_password) VALUES (?, ?, ?, ?, ?)",
                name, email, type, status, pass);

        System.out.println("✔ Registration Complete!");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        config db = new config();
        db.connectDB();

        char again;
        do {
            System.out.println("\n===== DELIVERY MANAGEMENT SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            int c = getIntInput(sc);

            switch (c) {
                case 1: loginMenu(db); break;
                case 2: registerMenu(db); break;
                case 3: System.exit(0); break;
                default: System.out.println("Invalid!");
            }

            System.out.print("Return to Main Menu? (Y/N): ");
            again = sc.next().charAt(0);

        } while (again == 'Y' || again == 'y');

        System.out.println("Program Ended.");
    }
}

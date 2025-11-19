package main;

import config.config;
import java.util.*;

public class superAdminMenu {

    public static void superAdminMenu(config db) {
        Scanner sc = new Scanner(System.in);
        char again;

        do {
            System.out.println("\n=== SUPER ADMIN MENU ===");
            System.out.println("1. Approve Account");
            System.out.println("2. View Users");
            System.out.println("3. Delete User");
            System.out.println("4. Exit");
            System.out.print("Choice: ");
            int c = main.getIntInput(sc);
            sc.nextLine(); // consume leftover newline

            switch (c) {
                case 1:
                    viewUsers(db);
                    System.out.print("Enter User ID to Approve: ");
                    int id = main.getIntInput(sc);
                    sc.nextLine(); // consume leftover newline
                    db.updateRecord("UPDATE tbl_user SET u_status='Approved' WHERE u_id=?", id);
                    System.out.println("✔ User Approved!");
                    break;

                case 2:
                    viewUsers(db);
                    break;

                case 3:
                    viewUsers(db);
                    System.out.print("Enter User ID to Delete: ");
                    int delId = main.getIntInput(sc);
                    sc.nextLine(); // consume leftover newline
                    System.out.print("Are you sure you want to delete this user? (Y/N): ");
                    char confirm = sc.next().charAt(0);
                    sc.nextLine(); // consume leftover newline
                    if (confirm == 'Y' || confirm == 'y') {
                        db.updateRecord("DELETE FROM tbl_user WHERE u_id=?", delId);
                        System.out.println("✔ User Deleted!");
                    } else {
                        System.out.println("❌ Deletion canceled.");
                    }
                    break;

                case 4:
                    System.out.println("Exiting Super Admin Menu...");
                    return; // exit the menu

                default:
                    System.out.println("Invalid choice!");
            }

            if(c != 4) {
                System.out.print("Return to menu? (Y/N): ");
                again = sc.next().charAt(0);
                sc.nextLine(); // consume leftover newline
            } else {
                again = 'N'; // exit loop
            }

        } while (again == 'Y' || again == 'y');
    }

    public static void viewUsers(config db) {
        String qry = "SELECT * FROM tbl_user";
        String[] headers = {"ID", "Name", "Email", "Type", "Status"};
        String[] cols = {"u_id", "u_name", "u_email", "u_type", "u_status"};
        db.viewRecords(qry, headers, cols);
    }
}

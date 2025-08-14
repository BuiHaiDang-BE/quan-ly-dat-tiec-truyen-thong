package view;
import model.customer;
import model.order;
import model.setMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class consoleView {
    private Scanner sc = new Scanner(System.in);

    // ===== Menu chính =====
    public int mainMenu() {
        System.out.println("\n===== TRADITIONAL FEAST ORDER MANAGEMENT =====");
        System.out.println("1. Register Customers");
        System.out.println("2. Update Customer Information");
        System.out.println("3. Search for Customer by Name");
        System.out.println("4. Display Feast Menus");
        System.out.println("5. Place a Feast Order");
        System.out.println("6. Update Order Information");
        System.out.println("7. Save Data to File");
        System.out.println("8. Display Customer or Order Lists");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
        return Integer.parseInt(sc.nextLine().trim());
    }

    // ===== Input khách hàng mới =====
    public customer inputCustomerInfo() {
        System.out.println("\n--- Enter Customer Information ---");
        System.out.print("Customer Code: ");
        String code = sc.nextLine().trim();

        System.out.print("Customer Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Phone Number: ");
        String phone = sc.nextLine().trim();

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        return new customer(code, name, phone, email);
    }

    // ===== Input cho cập nhật khách hàng =====
    public String inputCustomerCode() {
        System.out.print("Enter Customer Code: ");
        return sc.nextLine().trim();
    }

    public String inputUpdateName(String oldName) {
        System.out.print("Enter new Name (leave blank to keep '" + oldName + "'): ");
        return sc.nextLine().trim();
    }

    public String inputUpdatePhone(String oldPhone) {
        System.out.print("Enter new Phone (leave blank to keep '" + oldPhone + "'): ");
        return sc.nextLine();
    }

    public String inputUpdateEmail(String oldEmail) {
        System.out.print("Enter new Email (leave blank to keep '" + oldEmail + "'): ");
        return sc.nextLine().trim();
    }

    // ===== Input tìm kiếm khách hàng =====
    public String inputSearchName() {
        System.out.print("Enter name to search: ");
        return sc.nextLine().trim();
    }

    // ===== Thông báo chung =====
    public void showMessage(String msg) {
        System.out.println(msg);
    }

    // ===== Order Input Methods =====
    public String inputMenuCode() {
        System.out.print("Enter Set Menu Code: ");
        return sc.nextLine().trim();
    }

    public int inputNumberOfTables() {
        System.out.print("Enter Number of Tables: ");
        return Integer.parseInt(sc.nextLine().trim());
    }

    public String inputEventDate() {
        System.out.print("Enter Event Date (dd/MM/yyyy): ");
        return sc.nextLine().trim();
    }

    public String inputOrderId() {
        System.out.print("Enter Order ID: ");
        return sc.nextLine().trim();
    }

    // ===== Optional Input Methods for Update =====
    public String inputMenuCodeOptional() {
        System.out.print("Enter new Set Menu Code (press Enter to keep current): ");
        return sc.nextLine();
    }

    public int inputNumberOfTablesOptional() {
        System.out.print("Enter new Number of Tables (0 to keep current): ");
        try {
            int tables = Integer.parseInt(sc.nextLine().trim());
            return tables;
        } catch (NumberFormatException e) {
            return 0; // Giữ nguyên
        }
    }

    public String inputEventDateOptional() {
        System.out.print("Enter new Event Date (dd/MM/yyyy) (press Enter to keep current): ");
        return sc.nextLine();
    }

    // ===== Display Methods =====
    public void displayCustomerList(ArrayList<customer> list) {
        System.out.println("\n--- CUSTOMER LIST ---");
        System.out.printf("%-6s | %-20s | %-10s | %-25s\n", "Code", "Name", "Phone", "Email");
        System.out.println("---------------------------------------------------------------");
        for (customer c : list) {
            System.out.println(c);
        }
        System.out.println("---------------------------------------------------------------");
    }

    public void displayOrderList(ArrayList<order> list) {
        System.out.println("\n--- ORDER LIST ---");
        System.out.printf("%-6s | %-10s | %-6s | %-6s | %-10s | %3s | %10s\n",
                "ID", "Date", "Cust", "Menu", "Price", "Tbl", "Total");
        for (order o : list) {
            System.out.println(o);
        }
    }

    public void displayOrderInfo(order order, customer customer, setMenu menu) {
        System.out.println("\n=== ORDER INFORMATION ===");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Customer: " + customer.getName() + " (" + customer.getCustomer_code() + ")");
        System.out.println("Set Menu: " + menu.getName() + " (" + menu.getCode() + ")");
        System.out.println("Number of Tables: " + order.getNumberOfTables());
        System.out.println("Event Date: " + order.getEventDate());
        System.out.println("Price per Table: " + String.format("%,.0f VND", menu.getPrice()));
        System.out.println("Total Cost: " + String.format("%,.0f VND", order.getTotalCost()));
        System.out.println("===========================");
    }

    public void displaySearchResults(ArrayList<customer> customers, String searchName) {
        System.out.println("\nCustomers found with keyword: \"" + searchName + "\"");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-8s | %-25s | %-12s | %-20s%n", "Code", "Customer Name", "Phone", "Email");
        System.out.println("------------------------------------------------------------------");

        for (customer c : customers) {
            System.out.printf("%-8s | %-25s | %-12s | %-20s%n",
                    c.getCustomer_code(),
                    c.getName(),
                    c.getPhone(),
                    c.getEmail());
        }
        System.out.println("------------------------------------------------------------------");
    }

    public void displayMenuList(ArrayList<setMenu> menuList) {
        System.out.println("\n=== FEAST MENU LIST ===");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Menu ID  | Menu Name                 | Price (VND)     | Description");
        System.out.println("------------------------------------------------------------------------");

        for (setMenu menu : menuList) {
            try {
                String menuId = menu.getCode();
                String menuName = menu.getName();
                String description = menu.getIngredients();
                double price = menu.getPrice();

                String line = String.format("%s | %s | %.0f | %s",
                        padRight(menuId, 8),
                        padRight(menuName, 25),
                        price,
                        description);

                System.out.println(line);

            } catch (Exception e) {
                System.err.println("Error displaying menu: " + e.getMessage());
                System.out.println(menu.getCode() + " - " + menu.getName() + " - " + menu.getPrice());
            }
        }
        System.out.println("------------------------------------------------------------------------");
    }

    // Helper method để pad string
    private String padRight(String str, int length) {
        if (str == null) str = "";
        if (str.length() >= length) {
            return str.substring(0, length);
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < length) {
            sb.append(" ");
        }
        return sb.toString();
    }

    // ===== Ask Methods =====
    public boolean askToContinue(String actionName) {
        System.out.println("\n--- " + actionName.toUpperCase() + " COMPLETED ---");
        System.out.println("1. Continue " + actionName.toLowerCase());
        System.out.println("2. Return to main menu");
        System.out.print("Your choice (1/2): ");

        try {
            int choice = Integer.parseInt(sc.nextLine().trim());
            if (choice == 1) return true;
            else if (choice == 2) return false;
            else {
                System.out.println("Invalid choice! Returning to main menu...");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number! Returning to main menu...");
            return false;
        }
    }
    public void waitForEnter() {
        System.out.println("Press Enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            // Handle exception silently
        }
    }

    // ===== Getter =====
    public Scanner getScanner() {
        return sc;
    }
}
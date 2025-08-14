package view;

import controller.customerController;
import controller.menuController;
import controller.orderController;
import model.customerService;
import model.orderService;

public class Main {
    public static void main(String[] args) {
        // ===== Khởi tạo các thành phần =====
        consoleView view = new consoleView();
        customerService customerService = new customerService();
        orderService orderService = new orderService();

        customerController customerController = new  customerController(customerService, view);
        menuController menuController = new menuController(view);
        orderController orderController = new orderController(orderService, customerService, menuController, view);

        boolean running = true;


        // ===== Menu tổng =====
        while (running) {

            int choice = view.mainMenu();

            switch (choice) {
                case 1:
                    customerController.registerCustomer();
                    break;
                case 2:
                    customerController.updateCustomer();
                    break;
                case 3:
                    customerController.searchCustomerByName();
                    break;
                case 4:
                    menuController.displayFeastMenus();
                    break;
                case 5:
                    orderController.placeOrder();
                    break;
                case 6:
                    orderController.updateOrder();
                    break;
                case 7:
                    // Save customers & orders
                    customerController.saveCustomersToFile("customers.dat");
                    orderController.saveOrdersToFile("feast_order_service.dat");
                    break;
                case 8:
                    System.out.println("1. Display Customers");
                    System.out.println("2. Display Orders");
                    System.out.print("Choose: ");
                    int subChoice = Integer.parseInt(view.getScanner().nextLine().trim());
                    if (subChoice == 1) {
                        customerController.displayCustomers();
                    } else if (subChoice == 2) {
                        orderController.displayOrders();
                    } else {
                        view.showMessage(" Invalid choice!");
                    }
                    break;
                case 0:
                    running = false;
                    System.out.println(" Exiting program...");
                    break;
                default:
                    view.showMessage(" Invalid choice!");
            }
        }
    }
}

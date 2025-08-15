package controller;

import model.*;
import model.valid.customerValid;
import view.consoleView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class orderController {
    private orderService orderService;
    private customerService customerService;
    private menuController menuController;
    private consoleView view;


    public orderController() {
    }

    public orderController(orderService orderService, customerService customerService, menuController menuController, consoleView view) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.menuController = menuController;
        this.view = view;
    }


    public void placeOrder() {
        customerValid validator = new customerValid(view, customerService);
        do {
            String customerCode = view.inputCustomerCode();
            if (customerService.searchByCode(customerCode) == null) {
                view.showMessage(" Customer code does not exist!");
                continue;
            }

            String menuCode = view.inputMenuCode();
            setMenu menu = menuController.findMenuByCode(menuCode);
            if (menu == null) {
                view.showMessage(" Set Menu code does not exist!");
                continue;
            }

            int tables = view.inputNumberOfTables();
            if (tables <= 0) {
                view.showMessage(" Number of tables must be greater than 0!");
                continue;
            }

            String eventDate = view.inputEventDate();
            if (!validator.isValidFutureDate(eventDate)) {
                view.showMessage(" Event date must be in the future!");
                continue;
            }

            if (orderService.isDuplicateOrder(customerCode, menuCode, eventDate)) {
                view.showMessage(" Duplicate data!");
                continue;
            }

            String orderId = orderService.generateOrderId();
            double totalCost = menu.getPrice() * tables;
            order order = new order(orderId, customerCode, menuCode, eventDate, tables, totalCost);
            orderService.addOrder(order);

            view.showMessage(" Order placed successfully!");
            view.displayOrderInfo(order, customerService.searchByCode(customerCode), menu);

        } while (view.askToContinue("Order"));
    }

    public void updateOrder() {
        customerValid validator = new customerValid(view, customerService);

        do {
            String orderId = view.inputOrderId();
            order existingOrder = orderService.searchByOrderId(orderId);
            if (existingOrder == null) {
                view.showMessage(" Order does not exist.");
                continue;
            }

            if (isEventDatePassed(existingOrder.getEventDate())) {
                view.showMessage(" Cannot update an order with a past event date!");
                continue;
            }

            view.showMessage("\n=== CURRENT ORDER INFORMATION ===");
            customer cust = customerService.searchByCode(existingOrder.getCustomerCode());
            setMenu currentMenu = menuController.findMenuByCode(existingOrder.getSetMenuCode());
            view.displayOrderInfo(existingOrder, cust, currentMenu);

            String newMenuCode = existingOrder.getSetMenuCode();
            int newTables = existingOrder.getNumberOfTables();
            String newDate = existingOrder.getEventDate();
            boolean updated = false;

            String inputMenuCode = view.inputMenuCodeOptional();
            if (!inputMenuCode.trim().isEmpty()) {
                setMenu newMenu = menuController.findMenuByCode(inputMenuCode.trim());
                if (newMenu == null || inputMenuCode.length() != 4) {
                    view.showMessage(" Set Menu code is invalid!");
                    continue;
                }
                newMenuCode = inputMenuCode.trim();
                updated = true;
            }

            int inputTables = view.inputNumberOfTablesOptional();
            if (inputTables > 0) {
                if (!validator.validateNumberOfTables(inputTables)) {
                    continue;
                }
                newTables = inputTables;
                updated = true;
            }

            String inputDate = view.inputEventDateOptional();
            if (!inputDate.trim().isEmpty()) {
                if (!validator.isValidFutureDate(inputDate.trim())) {
                    view.showMessage(" Event date must be in the future!");
                    continue;
                }
                newDate = inputDate.trim();
                updated = true;
            }

            if (updated && orderService.isDuplicateOrderExceptCurrent(orderId, existingOrder.getCustomerCode(), newMenuCode, newDate)) {
                view.showMessage(" Order is duplicated!");
                continue;
            }

            if (updated) {
                setMenu menuForPrice = menuController.findMenuByCode(newMenuCode);
                double newTotalCost = menuForPrice != null ? menuForPrice.getPrice() * newTables : existingOrder.getTotalCost();
                orderService.updateOrder(orderId, newMenuCode, newTables, newDate, newTotalCost);
                view.showMessage(" Order updated successfully!");
                view.displayOrderInfo(orderService.searchByOrderId(orderId), cust, menuForPrice);
            } else {
                view.showMessage(" No information has been changed.");
            }

        } while (view.askToContinue("Update Order"));
    }

    private boolean isEventDatePassed(String eventDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date eventDateObj = sdf.parse(eventDate);
            Date currentDate = new Date(); // 04:21 PM +07, 15/08/2025
            return eventDateObj.before(currentDate);
        } catch (ParseException e) {
            return false;
        }
    }


    public void saveOrdersToFile(String filename) {
        try {
            fileHandler.saveToFile(filename, orderService.getOrders());
            view.showMessage(" Order data has been saved to " + filename);
        } catch (Exception e) {
            view.showMessage(" Error saving order data: " + e.getMessage());
        }
    }


    public void displayOrders() {
        ArrayList<order> list = orderService.getOrders();
        if (list.isEmpty()) {
            view.showMessage(" No orders available!");
        } else {
            view.displayOrderList(list);
        }
    }
}
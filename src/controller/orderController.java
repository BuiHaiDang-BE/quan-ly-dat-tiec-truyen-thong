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

    public orderController(orderService orderService, customerService customerService, menuController menuController, consoleView view) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.menuController = menuController;
        this.view = view;
    }

    // Đặt tiệc
    public void placeOrder() {
        customerValid validator = new customerValid(view, customerService);
        do {
            String customerCode = view.inputCustomerCode();
            if (customerService.searchByCode(customerCode) == null) {
                view.showMessage(" Mã khách hàng không tồn tại!");
                continue;
            }

            String menuCode = view.inputMenuCode();
            setMenu menu = menuController.findMenuByCode(menuCode);
            if (menu == null) {
                view.showMessage(" Mã Set Menu không tồn tại!");
                continue;
            }

            int tables = view.inputNumberOfTables();
            if (tables <= 0) {
                view.showMessage(" Số bàn phải lớn hơn 0!");
                continue;
            }

            String eventDate = view.inputEventDate();
            if (!validator.isValidFutureDate(eventDate)) {
                view.showMessage(" Ngày tổ chức phải là ngày trong tương lai!");
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

            view.showMessage("Đặt tiệc thành công!");
            view.displayOrderInfo(order, customerService.searchByCode(customerCode), menu);

        } while (view.askToContinue("Order "));
    }

    public void updateOrder() {
        customerValid validator = new customerValid(view, customerService);

        do {
            String orderId = view.inputOrderId();
            order existingOrder = orderService.searchByOrderId(orderId);
            if (existingOrder == null) {
                view.showMessage(" Đơn hàng không tồn tại.");
                continue;
            }

            if (isEventDatePassed(existingOrder.getEventDate())) {
                view.showMessage(" Không thể cập nhật đơn hàng đã qua ngày tổ chức!");
                continue;
            }

            view.showMessage("\n=== THÔNG TIN ĐƠN HÀNG HIỆN TẠI ===");
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
                    view.showMessage(" Mã Set Menu không hợp lệ!");
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
                    view.showMessage(" Ngày tổ chức phải là ngày trong tương lai!");
                    continue;
                }
                newDate = inputDate.trim();
                updated = true;
            }

            if (updated && orderService.isDuplicateOrderExceptCurrent(orderId, existingOrder.getCustomerCode(), newMenuCode, newDate)) {
                view.showMessage(" Đơn hàng trùng lặp!");
                continue;
            }

            if (updated) {
                setMenu menuForPrice = menuController.findMenuByCode(newMenuCode);
                double newTotalCost = menuForPrice != null ? menuForPrice.getPrice() * newTables : existingOrder.getTotalCost();
                orderService.updateOrder(orderId, newMenuCode, newTables, newDate, newTotalCost);
                view.showMessage(" Cập nhật đơn hàng thành công!");
                view.displayOrderInfo(orderService.searchByOrderId(orderId), cust, menuForPrice);
            } else {
                view.showMessage(" Không có thông tin nào được thay đổi.");
            }

        } while (view.askToContinue("Cập nhật đơn hàng"));
    }

    private boolean isEventDatePassed(String eventDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date eventDateObj = sdf.parse(eventDate);
            Date currentDate = new Date();
            return eventDateObj.before(currentDate);
        } catch (ParseException e) {
            return false;
        }
    }

    // Lưu đơn hàng xuống file
    public void saveOrdersToFile(String filename) {
        try {
            fileHandler.saveToFile(filename, orderService.getOrders());
            view.showMessage(" Dữ liệu đơn hàng đã lưu vào " + filename);
        } catch (Exception e) {
            view.showMessage(" Lỗi khi lưu đơn hàng: " + e.getMessage());
        }
    }

    // Hiển thị danh sách đơn hàng
    public void displayOrders() {
        ArrayList<order> list = orderService.getOrders();
        if (list.isEmpty()) {
            view.showMessage(" Chưa có đơn hàng nào!");
        } else {
            view.displayOrderList(list);
        }
    }
}
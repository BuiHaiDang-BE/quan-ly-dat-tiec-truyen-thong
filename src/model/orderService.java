package model;

import java.util.ArrayList;

public class orderService {
    private ArrayList<order> orders;

    public orderService() {
        orders = new ArrayList<>();
    }

    public ArrayList<order> getOrders() {
        return orders;
    }

    public void addOrder(order order) {
        orders.add(order);
    }

    public order searchByOrderId(String orderId) {
        for (order o : orders) {
            if (o.getOrderId().equalsIgnoreCase(orderId)) {
                return o;
            }
        }
        return null;
    }

    public String generateOrderId() {
        String orderId;
        int counter = 1;

        do {
            orderId = String.format("%03d", counter); // O001, O002, ...
            counter++;
        } while (isOrderIdExists(orderId));

        return orderId;
    }

    private boolean isOrderIdExists(String orderId) {
        return orders.stream()
                .anyMatch(order -> order.getOrderId().equals(orderId));
    }

    public boolean isDuplicateOrder(String customerCode, String setMenuCode, String eventDate) {
        for (order o : orders) {
            if (o.getCustomerCode().equalsIgnoreCase(customerCode) &&
                    o.getSetMenuCode().equalsIgnoreCase(setMenuCode) &&
                    o.getEventDate().equals(eventDate)) {
                return true;
            }
        }
        return false;
    }

    public void updateOrder(String orderId, String newSetMenuCode, int newTables, String newDate, double newPrice ) {
        order o = searchByOrderId(orderId);
        if (o != null) {
            if (!newSetMenuCode.isEmpty()) o.setSetMenuCode(newSetMenuCode);
            if (newPrice > 0) o.setUnitPrice(newPrice);
            if (newTables > 0) o.setNumberOfTables(newTables);
            if (!newDate.isEmpty()) o.setEventDate(newDate);
        }
    }
    public boolean isDuplicateOrderExceptCurrent(String orderId, String customerCode, String setMenuCode, String eventDate) {
        for (order o : orders) {
            if (!o.getOrderId().equals(orderId) &&
                    o.getCustomerCode().equalsIgnoreCase(customerCode) &&
                    o.getSetMenuCode().equalsIgnoreCase(setMenuCode) &&
                    o.getEventDate().equals(eventDate)) {
                return true;
            }
        }
        return false;
    }

}

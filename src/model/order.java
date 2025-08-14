package model;

import java.io.Serializable;

public class order implements Serializable {
    private String orderId;
    private String customerCode;
    private String setMenuCode;
    private String eventDate;
    private int numberOfTables;
    private double unitPrice;

    private static final long serialVersionUID = 1L;

    public order() {
    }

    public order(String orderId, String customerCode, String setMenuCode, String eventDate, int numberOfTables, double unitPrice) {
        this.orderId = orderId;
        this.customerCode = customerCode;
        this.setMenuCode = setMenuCode;
        this.eventDate = eventDate;
        this.numberOfTables = numberOfTables;
        this.unitPrice = unitPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getSetMenuCode() {
        return setMenuCode;
    }

    public void setSetMenuCode(String setMenuCode) {
        this.setMenuCode = setMenuCode;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public int getNumberOfTables() {
        return numberOfTables;
    }

    public void setNumberOfTables(int numberOfTables) {
        this.numberOfTables = numberOfTables;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalCost() {
        return numberOfTables * unitPrice;
    }

    @Override
    public String toString() {
        return "order{" +
                "orderId='" + orderId + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", setMenuCode='" + setMenuCode + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", numberOfTables=" + numberOfTables +
                ", unitPrice=" + unitPrice +
                '}';
    }
}

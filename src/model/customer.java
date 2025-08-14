package model;

import java.io.Serializable;

public class customer implements Serializable {
    private String Customer_code;
    private String Name;
    private String Phone;
    private String Email;

    private static final long serialVersionUID = 1L;

    public customer() {
    }

    public customer(String customer_code, String name, String phone, String email) {
        this.Customer_code = customer_code;
        this.Name = name;
        this.Phone = phone;
        this.Email = email;
    }

    public String getCustomer_code() {
        return Customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.Customer_code = customer_code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    @Override
    public String toString() {
        return "customer{" +
                "Customer_code= '" + Customer_code + '\'' +
                ", Name='" + Name + '\'' +
                ", Phone=" + Phone +
                ", Email='" + Email + '\'' +
                '}';
    }
}

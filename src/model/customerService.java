package model;

import java.util.ArrayList;

public class customerService {
    private ArrayList<customer> customers;

    public customerService() {
        customers = new ArrayList<>();
    }

    public ArrayList<customer> getCustomers() {
        return customers;
    }

    public void addCustomer(customer c) {
        customers.add(c);
    }

    public customer searchByCode(String code) {
        for (customer c : customers) {
            if (c.getCustomer_code().equalsIgnoreCase(code)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<customer> searchByName(String name) {
        ArrayList<customer> results = new ArrayList<>();
        String searchName = name.toLowerCase().trim();

        for (customer c : customers) {
            if (c.getName() != null &&
                    c.getName().toLowerCase().contains(searchName)) {
                results.add(c);
            }
        }

        return results;
    }

    public boolean isCodeUnique(String code) {
        return searchByCode(code) == null;
    }
    public void updateCustomer(String code, String newName, String newPhone, String newEmail) {
        customer c = searchByCode(code);
        if (c != null) {
            if (!newName.isEmpty()) c.setName(newName);
            if (!newPhone.isEmpty()) c.setPhone(newPhone);
            if (!newEmail.isEmpty()) c.setEmail(newEmail);
        }
    }
}

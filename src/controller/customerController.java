package controller;

import model.customer;
import view.consoleView;
import model.fileHandler;
import model.customerService;
import model.valid.customerValid;
import java.io.IOException;
import java.util.ArrayList;

public class customerController {
    private customerService customerService;
    private consoleView view;

    public customerController(customerService customerService, consoleView view) {
        this.customerService = customerService;
        this.view = view;
    }

    // 1. registering
    public void registerCustomer() {
        boolean continueRegistration = true;
        customerValid validator = new customerValid(view, customerService);

        while (continueRegistration) {
            try {
                customer c = view.inputCustomerInfo();

                // Check valid input
                if (!validator.validateCustomerInput(c)) {
                    continue;
                }

                // Save if valid
                customerService.addCustomer(c);
                view.showMessage("Customer added successfully!");

                continueRegistration = view.askToContinue("register");

            } catch (Exception e) {
                view.showMessage("An error occurred: " + e.getMessage());
                continueRegistration = view.askToContinue("register");
            }
        }
    }

    public void updateCustomer() {
        boolean continueUpdate = true;
        customerValid validator = new customerValid(view, customerService);

        while (continueUpdate) {
            try {
                String code = view.inputCustomerCode();

                customer c = customerService.searchByCode(code);
                if (c == null) {
                    view.showMessage("Customer not found with code: " + code);
                    continueUpdate = view.askToContinue("update");
                    continue;
                }

                String newName = view.inputUpdateName(c.getName());
                if (newName == null || newName.trim().isEmpty()) {
                    newName = c.getName(); // Keep the old name
                }

                String newPhone = view.inputUpdatePhone(c.getPhone());
                if (newPhone == null || newPhone.trim().isEmpty()) {
                    newPhone = c.getPhone(); // Keep the old phone number
                }

                String newEmail = view.inputUpdateEmail(c.getEmail());
                if (newEmail == null || newEmail.trim().isEmpty()) {
                    newEmail = c.getEmail(); // Keep the old email
                }

                // Validate only the newly entered fields
                customer tempCustomer = new customer();
                tempCustomer.setCustomer_code(code);
                tempCustomer.setName(newName);
                tempCustomer.setPhone(newPhone);
                tempCustomer.setEmail(newEmail);

                boolean isNameNew = !(newName.equals(c.getName()) || (newName == null && c.getName() != null));
                boolean isPhoneNew = !(newPhone.equals(c.getPhone()) || (newPhone == null && c.getPhone() != null));
                boolean isEmailNew = !(newEmail.equals(c.getEmail()) || (newEmail == null && c.getEmail() != null));

                if (!validator.validateCustomerInputForUpdate(tempCustomer, isNameNew, isPhoneNew, isEmailNew)) {
                    view.showMessage("Invalid information, please try again!");
                    continueUpdate = view.askToContinue("update");
                    continue;
                }

                customerService.updateCustomer(code, newName, newPhone, newEmail);
                view.showMessage("Customer information updated successfully!");

                continueUpdate = view.askToContinue("update");

            } catch (Exception e) {
                view.showMessage("An error occurred: " + e.getMessage());
                continueUpdate = view.askToContinue("update");
            }
        }
    }

    // 3. searching by name
    public void searchCustomerByName() {
        boolean continueSearch = true;

        while (continueSearch) {
            try {

                view.showMessage("--- SEARCH CUSTOMER BY NAME ---");
                String searchName = view.inputSearchName();


                if (searchName == null || searchName.trim().isEmpty()) {
                    view.showMessage("Search name cannot be empty!");
                    continueSearch = view.askToContinue("search");
                    continue;
                }

                // Normalize search name (remove extra whitespace)
                searchName = searchName.trim();

                view.showMessage("Searching for customer with name: \"" + searchName + "\"...");

                // Perform search
                ArrayList<customer> searchResults = customerService.searchByName(searchName);

                // Handle search results
                if (searchResults == null || searchResults.isEmpty()) {

                    view.showMessage("No customers found matching the search criteria!");
                } else {

                    view.showMessage("Found " + searchResults.size() + " matching customers:");

                    // Sort results alphabetically by name
                    sortCustomersByName(searchResults);


                    view.displaySearchResults(searchResults, searchName);

                    // Show statistics
                    view.showMessage("Total: " + searchResults.size() + " customers found.");
                }

                // Ask if user wants to continue searching
                continueSearch = view.askToContinue("search");

            } catch (Exception e) {
                view.showMessage("An error occurred during search: " + e.getMessage());
                view.showMessage("Please try again!");
                continueSearch = view.askToContinue("search");
            }
        }
    }

    // Helper method: Sort customer list by name (alphabet)
    private void sortCustomersByName(ArrayList<customer> customers) {
        customers.sort((c1, c2) -> {
            if (c1.getName() == null && c2.getName() == null) {
                return 0;
            }
            if (c1.getName() == null) {
                return 1;
            }
            if (c2.getName() == null) {
                return -1;
            }
            return c1.getName().compareToIgnoreCase(c2.getName());
        });
    }

    // Display customer list
    public void displayCustomers() {
        ArrayList<customer> list = customerService.getCustomers();
        if (list.isEmpty()) {
            view.showMessage("Customer list is empty!");
        } else {
            view.displayCustomerList(list);
        }
    }

    // Save customers to file
    public void saveCustomersToFile(String filename) {
        try {
            fileHandler.saveToFile(filename, customerService.getCustomers());
            view.showMessage("Customer data saved successfully to " + filename);
        } catch (IOException e) {
            view.showMessage("Error saving data: " + e.getMessage());
        }
    }
}
package controller;

import model.customer;
import view.consoleView;
import  model.fileHandler;
import  model.customerService;
import model.valid.customerValid;
import  java.io.IOException;
import  java.util.ArrayList;

public class customerController {
    private customerService customerService;
    private consoleView view;

    public customerController(customerService CustomerService, consoleView view) {
        this.customerService = CustomerService;
        this.view = view;
    }
    // 1. Đăng ký khách hàng
    public void registerCustomer() {

        boolean continueRegistration = true;
        customerValid validator = new customerValid(view, customerService);

        while (continueRegistration) {
            try {
                // 1. Prompt user to input customer details
                customer c = view.inputCustomerInfo();

                // 2. Validate all inputs
                if (!validator.validateCustomerInput(c)) {
                    continue; // If validation fails, ask for input again
                }

                // 3. Save the registration record if all inputs are valid
                customerService.addCustomer(c);
                view.showMessage("Thêm khách hàng thành công!");

                // 4. Prompt user to continue or return to main menu
                continueRegistration = view.askToContinue("đăng Ký ");

            } catch (Exception e) {
                view.showMessage("Có lỗi xảy ra: " + e.getMessage());
                continueRegistration = view.askToContinue("đăng ký ");
            }
        }
    }


    public void updateCustomer() {
        boolean continueUpdate = true;
        customerValid validator = new customerValid(view, customerService); // Khởi tạo validator

        while (continueUpdate) {
            try {
                // 1. Nhập mã khách hàng để tìm
                String code = view.inputCustomerCode();

                // 2. Kiểm tra xem khách hàng có tồn tại không
                customer c = customerService.searchByCode(code);
                if (c == null) {
                    view.showMessage("Không tìm thấy khách hàng với mã: " + code);
                    continueUpdate = view.askToContinue("cập nhật");
                    continue;
                }

                // 3. Nhập thông tin cập nhật
                String newName = view.inputUpdateName(c.getName());
                if (newName == null || newName.trim().isEmpty()) {
                    newName = c.getName(); // Giữ nguyên tên cũ
                }

                String newPhone = view.inputUpdatePhone(c.getPhone());
                if (newPhone == null || newPhone.trim().isEmpty()) {
                    newPhone = c.getPhone(); // Giữ nguyên số điện thoại cũ
                }

                String newEmail = view.inputUpdateEmail(c.getEmail());
                if (newEmail == null || newEmail.trim().isEmpty()) {
                    newEmail = c.getEmail(); // Giữ nguyên email cũ
                }

                // 4. Validate chỉ các trường được nhập mới
                customer tempCustomer = new customer();
                tempCustomer.setCustomer_code(code);
                tempCustomer.setName(newName);
                tempCustomer.setPhone(newPhone);
                tempCustomer.setEmail(newEmail);

                boolean isNameNew = !(newName.equals(c.getName()) || (newName == null && c.getName() != null));
                boolean isPhoneNew = !(newPhone.equals(c.getPhone()) || (newPhone == null && c.getPhone() != null));
                boolean isEmailNew = !(newEmail.equals(c.getEmail()) || (newEmail == null && c.getEmail() != null));

                if (!validator.validateCustomerInputForUpdate(tempCustomer, isNameNew, isPhoneNew, isEmailNew)) {
                    view.showMessage("Thông tin không hợp lệ, vui lòng nhập lại!");
                    continueUpdate = view.askToContinue("cập nhật ");
                    continue;
                }

                // 5. Cập nhật thông tin khách hàng
                customerService.updateCustomer(code, newName, newPhone, newEmail);
                view.showMessage("Cập nhật thông tin khách hàng thành công!");

                // 6. Hỏi người dùng có muốn tiếp tục cập nhật không
                continueUpdate = view.askToContinue("cập nhật ");

            } catch (Exception e) {
                view.showMessage("Có lỗi xảy ra: " + e.getMessage());
                continueUpdate = view.askToContinue("cập nhật ");
            }
        }
    }

    // 3. Tìm kiếm khách hàng theo tên
    public void searchCustomerByName() {
        boolean continueSearch = true;

        while (continueSearch) {
            try {
                // Nhập tên hoặc tên một phần để tìm kiếm
                view.showMessage("--- TÌM KIẾM KHÁCH HÀNG THEO TÊN ---");
                String searchName = view.inputSearchName();

                // Kiểm tra input không được để trống
                if (searchName == null || searchName.trim().isEmpty()) {
                    view.showMessage("Tên tìm kiếm không được để trống!");
                    continueSearch = view.askToContinue("tìm kiếm ");
                    continue;
                }

                // Chuẩn hóa tên tìm kiếm (loại bỏ khoảng trắng thừa)
                searchName = searchName.trim();

                view.showMessage("Đang tìm kiếm khách hàng với tên: \"" + searchName + "\"...");

                // Thực hiện tìm kiếm
                ArrayList<customer> searchResults = customerService.searchByName(searchName);

                // Xử lý kết quả tìm kiếm
                if (searchResults == null || searchResults.isEmpty()) {
                    // Trường hợp không tìm thấy
                    view.showMessage("Không tìm thấy khách hàng nào phù hợp với tiêu chí tìm kiếm!");
                } else {
                    // Trường hợp tìm thấy khách hàng
                    view.showMessage("Tìm thấy " + searchResults.size() + " khách hàng phù hợp:");

                    // Sắp xếp kết quả theo thứ tự alphabet (theo tên)
                    sortCustomersByName(searchResults);

                    // Hiển thị danh sách khách hàng tìm được
                    view.displaySearchResults(searchResults, searchName);

                    // Hiển thị thống kê
                    view.showMessage("Tổng cộng: " + searchResults.size() + " khách hàng được tìm thấy.");
                }

                // 4. Hỏi người dùng có muốn tiếp tục tìm kiếm không
                continueSearch = view.askToContinue("tìm kiếm ");

            } catch (Exception e) {
                view.showMessage("Có lỗi xảy ra khi tìm kiếm: " + e.getMessage());
                view.showMessage("Vui lòng thử lại!");
                continueSearch = view.askToContinue("tìm kiếm ");
            }
        }
    }

    // Helper method: Sắp xếp danh sách khách hàng theo tên (alphabet)
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

    //  Hiển thị danh sách khách hàng
    public void displayCustomers() {
        ArrayList<customer> list = customerService.getCustomers();
        if (list.isEmpty()) {
            view.showMessage("Danh sách khách hàng trống!");
        } else {
            view.displayCustomerList(list);
        }
    }

    //  Lưu khách hàng xuống file
    public void saveCustomersToFile(String filename) {
        try {
            fileHandler.saveToFile(filename, customerService.getCustomers());
            view.showMessage("Lưu dữ liệu khách hàng thành công vào " + filename);
        } catch (IOException e) {
            view.showMessage("Lỗi khi lưu dữ liệu: " + e.getMessage());
        }
    }

}

package model.valid;

import model.customerService;
import view.consoleView;
import model.customer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class customerValid {
    private consoleView view;
    private customerService customerService;

    public customerValid(consoleView view, customerService customerService) {
        this.view = view;
        this.customerService = customerService;
    }

    // ===== customer =====
    // Validate tất cả các trường (dùng cho đăng ký)
    public boolean validateCustomerInput(customer c) {
        return validateCustomerCode(c.getCustomer_code()) &&
                validateCustomerName(c.getName()) &&
                validatePhoneNumber(c.getPhone()) &&
                validateEmail(c.getEmail());
    }

    // Validate tùy chọn cho update (KHÔNG kiểm tra uniqueness của customer code)
    public boolean validateCustomerInputForUpdate(customer c, boolean validateName, boolean validatePhone, boolean validateEmail) {
        boolean isValid = true;

        // Chỉ validate format của customer code, không check uniqueness
        if (!validateCustomerCodeFormat(c.getCustomer_code())) {
            isValid = false;
        }

        // Validate name nếu yêu cầu
        if (validateName && !validateCustomerName(c.getName())) {
            isValid = false;
        }

        // Validate phone nếu yêu cầu
        if (validatePhone && !validatePhoneNumber(c.getPhone())) {
            isValid = false;
        }

        // Validate email nếu yêu cầu
        if (validateEmail && !validateEmail(c.getEmail())) {
            isValid = false;
        }

        return isValid;
    }

    // Validate customer code với uniqueness check (dùng cho đăng ký)
    private boolean validateCustomerCode(String code) {
        // Kiểm tra format trước
        if (!validateCustomerCodeFormat(code)) {
            return false;
        }

        // Kiểm tra tính duy nhất
        if (!customerService.isCodeUnique(code)) {
            view.showMessage("Mã khách hàng đã tồn tại!");
            return false;
        }

        return true;
    }

    // Validate chỉ format của customer code (không check uniqueness)
    private boolean validateCustomerCodeFormat(String code) {
        if (code == null || code.length() != 5) {
            view.showMessage("Mã khách hàng phải có đúng 5 ký tự!");
            return false;
        }

        char firstChar = code.charAt(0);
        if (firstChar != 'C' && firstChar != 'G' && firstChar != 'K') {
            view.showMessage("Mã khách hàng phải bắt đầu bằng C, G hoặc K!");
            return false;
        }

        String digits = code.substring(1);
        if (!digits.matches("\\d{4}")) {
            view.showMessage("4 ký tự cuối của mã khách hàng phải là số!");
            return false;
        }

        return true;
    }

    // Validate customer name: Non-empty, 2-25 characters
    private boolean validateCustomerName(String name) {
        if (name == null || name.trim().isEmpty()) {
            view.showMessage("Tên khách hàng không được để trống!");
            return false;
        }

        String trimmedName = name.trim();
        if (trimmedName.length() < 2 || trimmedName.length() > 25) {
            view.showMessage("Tên khách hàng phải có từ 2 đến 25 ký tự!");
            return false;
        }

        return true;
    }

    // Validate phone number: 10 digits, Vietnamese network operator
    private boolean validatePhoneNumber(String phone) {
        if (phone == null || !phone.matches("\\d{10}")) {
            view.showMessage("Số điện thoại phải có đúng 10 chữ số!");
            return false;
        }

        String[] validPrefixes = {
                "032", "033", "034", "035", "036", "037", "038", "039", "086", // Viettel
                "070", "079", "077", "076", "078", // Mobifone
                "083", "084", "085", "081", "082", "088", // Vinaphone
                "092", "056", "058" // Vietnamobile
        };

        String prefix = phone.substring(0, 3);
        boolean isValidPrefix = false;
        for (String validPrefix : validPrefixes) {
            if (prefix.equals(validPrefix)) {
                isValidPrefix = true;
                break;
            }
        }

        if (!isValidPrefix) {
            view.showMessage("Số điện thoại không thuộc nhà mạng Việt Nam!");
            return false;
        }

        return true;
    }

    // Validate email format
    private boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            view.showMessage("Email không được để trống!");
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailRegex)) {
            view.showMessage("Email không đúng định dạng!");
            return false;
        }

        return true;
    }

    // ===== Order =====

    public boolean isValidFutureDate(String eventDate) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);

            Date inputDate = sdf.parse(eventDate);
            Date currentDate = new Date();


            return inputDate.after(currentDate);

        } catch (ParseException e) {
            return false; // Format ngày không đúng
        }
    }

    public boolean validateNumberOfTables(int tables) {
        if (tables <= 0) {
            view.showMessage(" Số bàn phải lớn hơn 0!");
            return false;
        }


        if (tables > 50) {
            view.showMessage(" Số bàn không được vượt quá 50!");
            return false;
        }

        return true;
    }
}
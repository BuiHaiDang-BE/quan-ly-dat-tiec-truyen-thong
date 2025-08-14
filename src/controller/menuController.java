package controller;

import model.fileHandler;
import model.setMenu;
import view.consoleView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class menuController {
    private ArrayList<setMenu> menuList;
    private consoleView view;

    public menuController(consoleView view) {
        this.view = view;
        this.menuList = new ArrayList<>();
    }

    //  Hiển thị menu tiệc

    public void displayFeastMenus() {
        try {
            view.showMessage("--- DANH SÁCH MENU TIỆC ---");
            String filename = "feastmenu/feastmenu.csv";
            File menuFile = new File(filename);

            if (!menuFile.exists()) {
                view.showMessage("Cannot read data from \"feastmenu/feastmenu.csv\". Please check it.");
                return;
            }

            try {

                this.menuList = fileHandler.readSetMenuFromCSV(filename); // ← SỬA NÀY!

                if (this.menuList == null || this.menuList.isEmpty()) {
                    view.showMessage("File \"feastMenu.csv\" trống hoặc không có dữ liệu hợp lệ.");
                    return;
                }

                view.showMessage("Đọc dữ liệu từ \"" + filename + "\" thành công!");
                view.showMessage("Tìm thấy " + this.menuList.size() + " menu tiệc.");

            } catch (IOException e) {
                view.showMessage("Cannot read data from \"feastMenu.csv\". Please check it.");
                return;
            }

            // Sắp xếp this.menuList thay vì local menuList
            java.util.Collections.sort(this.menuList, Comparator.comparingDouble(setMenu::getPrice));

            // Hiển thị this.menuList
            view.displayMenuList(this.menuList);

            // Thống kê từ this.menuList
            view.showMessage("Tổng cộng: " + this.menuList.size() + " menu tiệc có sẵn.");

            double totalPrice = 0;
            for (setMenu menu : this.menuList) { // Dùng this.menuList
                totalPrice += menu.getPrice();
            }
            double averagePrice = this.menuList.size() > 0 ? totalPrice / this.menuList.size() : 0;
            view.showMessage("Giá trung bình: " + String.format("%.2f", averagePrice) + " VND");

        } catch (Exception e) {
            view.showMessage("Có lỗi không xác định xảy ra: " + e.getMessage());
        } finally {
            view.waitForEnter();
        }
    }


    // Tìm set menu theo code (dùng cho OrderController)
    public setMenu findMenuByCode(String code) {

        System.out.println("Đang tìm mã: '" + code + "'");
        for (setMenu sm : menuList) {
            if (sm.getCode().equalsIgnoreCase(code)) {
                return sm;
            }
        }
        return null;
    }

    public ArrayList<setMenu> getMenuList() {
        return menuList;
    }


    // Trong menuController
    public void printAllMenuCodes() {
        System.out.println("=== Danh sách mã menu có sẵn ===");
        for (setMenu menu : menuList) {
            System.out.println("- " + menu.getCode() + " | " + menu.getName());
        }
    }
}

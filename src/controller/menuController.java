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

    public menuController() {
    }

    public menuController(consoleView view) {
        this.view = view;
        this.menuList = new ArrayList<>();
    }

    // Hiển thị menu tiệc
    public void displayFeastMenus() {
        try {
            view.showMessage("--- FEAST MENU LIST ---");
            String filename = "feastmenu/feastmenu.csv";
            File menuFile = new File(filename);

            if (!menuFile.exists()) {
                view.showMessage("Cannot read data from \"feastmenu/feastmenu.csv\". Please check it.");
                return;
            }

            try {
                this.menuList = fileHandler.readSetMenuFromCSV(filename); // ← SỬA NÀY!

                if (this.menuList == null || this.menuList.isEmpty()) {
                    view.showMessage("File \"feastMenu.csv\" is empty or contains no valid data.");
                    return;
                }

                view.showMessage("Data read successfully from \"" + filename + "\"!");
                view.showMessage("Found " + this.menuList.size() + " feast menus.");

            } catch (IOException e) {
                view.showMessage("Cannot read data from \"feastMenu.csv\". Please check it.");
                return;
            }

            // Sort this.menuList instead of local menuList
            java.util.Collections.sort(this.menuList, Comparator.comparingDouble(setMenu::getPrice));

            // Display this.menuList
            view.displayMenuList(this.menuList);

            // Statistics from this.menuList
            view.showMessage("Total: " + this.menuList.size() + " feast menus available.");

            double totalPrice = 0;
            for (setMenu menu : this.menuList) { // Use this.menuList
                totalPrice += menu.getPrice();
            }
            double averagePrice = this.menuList.size() > 0 ? totalPrice / this.menuList.size() : 0;
            view.showMessage("Average price: " + String.format("%.2f", averagePrice) + " VND");

        } catch (Exception e) {
            view.showMessage("An unspecified error occurred: " + e.getMessage());
        } finally {
            view.waitForEnter();
        }
    }


    public setMenu findMenuByCode(String code) {
        System.out.println("Đang tìm mã: '" + code + "'");
        for (setMenu sm : menuList) {
            if (sm.getCode().equalsIgnoreCase(code)) {
                return sm;
            }
        }
        return null;
    }


}
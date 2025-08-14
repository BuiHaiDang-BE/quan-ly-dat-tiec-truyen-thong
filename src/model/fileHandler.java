package model;

import java.io.*;
import java.util.ArrayList;

public class fileHandler {

    // Lưu dữ liệu dạng object vào file binary
    public static <T> void saveToFile(String filename, ArrayList<T> list) throws IOException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(list);
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    // Đọc menu từ file CSV
    public static ArrayList<setMenu> readSetMenuFromCSV(String filename) throws IOException {
        ArrayList<setMenu> menuList = new ArrayList<>();
        BufferedReader br = null;

        try {
            FileInputStream fis = new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);

            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;

            System.out.println("Đang đọc file CSV: " + filename);

            while ((line = br.readLine()) != null) {
                lineNumber++;
                System.out.println("Dòng " + lineNumber + ": " + line);

                // Bỏ qua header row (dòng đầu tiên)
                if (isFirstLine) {
                    isFirstLine = false;
                    // Kiểm tra xem có phải header không
                    if (line.toLowerCase().contains("menu") && line.toLowerCase().contains("price")) {
                        System.out.println("Bỏ qua header row");
                        continue; // Skip header
                    }
                }

                // Bỏ qua dòng trống
                if (line.trim().isEmpty()) {
                    System.out.println("Bỏ qua dòng trống");
                    continue;
                }

                try {
                    // Parse CSV line
                    String[] parts = parseCSVLine(line);
                    System.out.println("Parsed parts: " + java.util.Arrays.toString(parts));

                    if (parts.length >= 4) {
                        String menuId = parts[0].trim();
                        String menuName = parts[1].trim();
                        String description = parts[2].trim();

                        // Kiểm tra giá trước khi parse
                        String priceStr = parts[3].trim();
                        System.out.println("Attempting to parse price: '" + priceStr + "'");

                        if (priceStr.isEmpty() || priceStr.equals("-") || priceStr.equals("null")) {
                            System.err.println("Cảnh báo: Giá không hợp lệ tại dòng " + lineNumber + ", bỏ qua: " + priceStr);
                            continue;
                        }

                        // Loại bỏ các ký tự không phải số (trừ dấu chấm)
                        priceStr = priceStr.replaceAll("[^0-9.]", "");

                        double price;
                        try {
                            price = Double.parseDouble(priceStr);
                        } catch (NumberFormatException e) {
                            System.err.println("Lỗi parse giá tại dòng " + lineNumber + ": " + priceStr + " -> " + e.getMessage());
                            continue; // Bỏ qua dòng này
                        }

                        // Nếu có trường serves (cột thứ 5)
                        int serves = 1;
                        if (parts.length >= 5) {
                            try {
                                String servesStr = parts[4].trim();
                                if (!servesStr.isEmpty() && !servesStr.equals("-")) {
                                    serves = Integer.parseInt(servesStr);
                                }
                            } catch (NumberFormatException e) {
                                serves = 1; // Default value
                                System.err.println("Cảnh báo: Không thể parse serves, dùng giá trị mặc định 1");
                            }
                        }

                        // Tạo object setMenu
                        setMenu menu = new setMenu(menuId, menuName, price, description);
                        menuList.add(menu);
                        System.out.println("Đã thêm menu: " + menuId + " - " + menuName + " - " + price);

                    } else {
                        System.err.println("Cảnh báo: Dòng " + lineNumber + " không đủ dữ liệu (cần ít nhất 4 cột): " + line);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Lỗi parse số tại dòng " + lineNumber + ": " + e.getMessage());
                    System.err.println("Nội dung dòng: " + line);
                } catch (Exception e) {
                    System.err.println("Lỗi xử lý dòng " + lineNumber + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }

        System.out.println("Hoàn thành đọc file. Tổng cộng: " + menuList.size() + " menu");
        return menuList;
    }



    // Helper method: Parse CSV line đơn giản
    private static String[] parseCSVLine(String line) {
        ArrayList<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        result.add(current.toString());
        return result.toArray(new String[result.size()]);
    }

    // Method để save menu ra CSV - tương thích Java 8
    public static void saveMenuToCSV(String filename, ArrayList<setMenu> menuList) throws IOException {
        PrintWriter pw = null;
        try {
            // Sử dụng OutputStreamWriter với UTF-8 cho Java 8
            FileOutputStream fos = new FileOutputStream(filename);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            pw = new PrintWriter(osw);

            // Write header
            pw.println("Menu ID,Menu Name,Description,Price,Serves");

            // Write data
            for (setMenu menu : menuList) {
                pw.printf("%s,\"%s\",\"%s\",%.2f,1%n",
                        menu.getCode(),
                        menu.getName().replace("\"", "\"\""), // Escape quotes
                        menu.getIngredients().replace("\"", "\"\""), // Escape quotes
                        menu.getPrice()
                );
            }
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}

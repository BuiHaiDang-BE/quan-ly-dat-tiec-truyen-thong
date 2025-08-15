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

            System.out.println("Reading CSV file: " + filename);

            while ((line = br.readLine()) != null) {
                lineNumber++;
                System.out.println("Line " + lineNumber + ": " + line);

                // Bỏ qua header row (dòng đầu tiên)
                if (isFirstLine) {
                    isFirstLine = false;
                    // Kiểm tra xem có phải header không
                    if (line.toLowerCase().contains("menu") && line.toLowerCase().contains("price")) {
                        System.out.println("Skipping header row");
                        continue; // Skip header
                    }
                }

                // Bỏ qua dòng trống
                if (line.trim().isEmpty()) {
                    System.out.println("Skipping empty line");
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
                            System.err.println("Warning: Invalid price at line " + lineNumber + ", skipping: " + priceStr);
                            continue;
                        }

                        // Loại bỏ các ký tự không phải số (trừ dấu chấm)
                        priceStr = priceStr.replaceAll("[^0-9.]", "");

                        double price;
                        try {
                            price = Double.parseDouble(priceStr);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing price at line " + lineNumber + ": " + priceStr + " -> " + e.getMessage());
                            continue;
                        }



                        // Tạo object setMenu
                        setMenu menu = new setMenu(menuId, menuName, price, description);
                        menuList.add(menu);
                        System.out.println("Added menu: " + menuId + " - " + menuName + " - " + price);

                    } else {
                        System.err.println("Warning: Line " + lineNumber + " does not have enough data (needs at least 4 columns): " + line);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number at line " + lineNumber + ": " + e.getMessage());
                    System.err.println("Line content: " + line);
                } catch (Exception e) {
                    System.err.println("Error processing line " + lineNumber + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }

        System.out.println("Finished reading file. Total: " + menuList.size() + " menus");
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


}
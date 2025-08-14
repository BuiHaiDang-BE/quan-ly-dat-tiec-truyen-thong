package model;

public class setMenu {
    private String Code;
    private String Name;
    private double Price;
    private String Ingredients;// String chứa các món ăn;
    private int serves; // Số người ăn

    public setMenu() {
    }

    public setMenu(String code, String name, double price, String ingredients) {
        this.Code = code;
        this.Name = name;
        this.Price = price;
        this.Ingredients = ingredients;
        this.serves = 1;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        this.Ingredients = ingredients;
    }

    public int getServes() { return serves; }

    public void setServes(int serves) { this.serves = serves; }

    @Override
    public String toString() {
        return "setMenu{" +
                "Code='" + Code + '\'' +
                ", Name='" + Name + '\'' +
                ", Price=" + Price +
                ", Ingredients='" + Ingredients + '\'' +
                ", serves=" + serves +
                '}';
    }
}

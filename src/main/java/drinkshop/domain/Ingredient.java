package drinkshop.domain;

public class Ingredient {
    private int id;
    private String denumire;

    public Ingredient(int id, String denumire) {
        this.id = id;
        this.denumire = denumire;
    }

    public int getId() { return id; }
    public String getDenumire() { return denumire; }

    @Override
    public String toString() {
        return denumire;
    }
}
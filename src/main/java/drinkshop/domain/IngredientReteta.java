
package drinkshop.domain;

public class IngredientReteta {

    private Ingredient ingredient;
    private double cantitate;

    public IngredientReteta(Ingredient ingredient, double cantitate) {
        this.ingredient = ingredient;
        this.cantitate = cantitate;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getCantitate() {
        return cantitate;
    }

    public void setCantitate(double cantitate) {
        this.cantitate = cantitate;
    }

    @Override
    public String toString() {
        return ingredient + "," + cantitate;
    }
}


package drinkshop.service;

import drinkshop.domain.Ingredient;
import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;

import java.util.List;

public class IngredientService {
    private final Repository<Integer, Ingredient> ingrRepo;

    public IngredientService(Repository<Integer, Ingredient> ingrRepo) {
        this.ingrRepo = ingrRepo;
    }

    public List<Ingredient> getAll() {
        return ingrRepo.findAll();
    }

    public void add(Ingredient s) {
        ingrRepo.save(s);
    }

    public void update(Ingredient s) {
        ingrRepo.update(s);
    }

    public void delete(int id) {
        ingrRepo.delete(id);
    }
}

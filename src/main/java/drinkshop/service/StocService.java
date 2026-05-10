
package drinkshop.service;

import drinkshop.domain.Ingredient;
import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

import java.util.List;
import java.util.Map;

public class StocService {

    private final Repository<Integer, Stoc> stocRepo;
private final Validator<Stoc> stocValidator;
    public StocService(Repository<Integer, Stoc> stocRepo, Validator<Stoc> stocValidator) {
        this.stocValidator = stocValidator;
        this.stocRepo = stocRepo;
    }

    public List<Stoc> getAll() {
        return stocRepo.findAll();
    }

    public void add(Stoc s) {
        stocValidator.validate(s);
        stocRepo.save(s);
    }

    public void update(Stoc s) {
        stocRepo.update(s);
    }

    public void delete(int id) {
        stocRepo.delete(id);
    }

    public boolean areSuficient(Reteta reteta) {
        List<IngredientReteta> ingredienteNecesare = reteta.getIngrediente();

        for (IngredientReteta e : ingredienteNecesare) {
            Ingredient ingredient = e.getIngredient();
            double necesar = e.getCantitate();

            double disponibil = stocRepo.findAll().stream()
                    .filter(s -> s.getIngredient().getId() == ingredient.getId())
                    .mapToDouble(Stoc::getCantitate)
                    .sum();

            if (disponibil < necesar) {
                return false;
            }
        }
        return true;
    }

    public void consuma(Reteta reteta) {
        if (!areSuficient(reteta)) {
            throw new IllegalStateException("Stoc insuficient pentru rețeta.");
        }

        for (IngredientReteta e : reteta.getIngrediente()) {
            Ingredient ingredient = e.getIngredient();
            double necesar = e.getCantitate();

            List<Stoc> ingredienteStoc = stocRepo.findAll().stream()
                    .filter(s -> s.getIngredient().getId() == ingredient.getId())
                    .collect(java.util.stream.Collectors.toList());

            double ramas = necesar;

            for (Stoc s : ingredienteStoc) {
                if (ramas <= 0) break;

                double deScazut = Math.min(s.getCantitate(), ramas);
                s.setCantitate(s.getCantitate() - deScazut);
                ramas -= deScazut;

                stocRepo.update(s);
            }
        }
    }
}

package drinkshop.repository.file;

import drinkshop.domain.Ingredient;
import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.repository.Repository;
import drinkshop.repository.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileRetetaRepository
        extends FileAbstractRepository<Integer, Reteta> {
    private Repository<Integer,Ingredient> ingrRepository;
    public FileRetetaRepository(String fileName, Repository<Integer,Ingredient> ingrRepository) throws RepositoryException {
        super(fileName);
        this.ingrRepository = ingrRepository;
        loadFromFile();
    }

    @Override
    protected Integer getId(Reteta entity) {
        return entity.getId();
    }

    @Override
    protected Reteta extractEntity(String line) {

        String[] elems = line.split(",");

        int productId = Integer.parseInt(elems[0]);
        List<IngredientReteta> ingrediente = new ArrayList<>();
        int index=1;
        while (index<elems.length) {
            String ingredientTotal= elems[index++];
            String[] ingredientSeparat = ingredientTotal.split(":");
            int ingredientId = Integer.parseInt(ingredientSeparat[0]);
            Ingredient ingredient = ingrRepository.findOne(ingredientId);
            Double ingredientQuantity = Double.parseDouble(ingredientSeparat[1]);
            ingrediente.add(new IngredientReteta(ingredient, ingredientQuantity));
        }
        return new Reteta(productId, ingrediente);
    }

    @Override
    protected String createEntityAsString(Reteta entity) {
        String ingrediente = entity.getIngrediente().stream()
                .map(entry -> entry.getIngredient().getId() + ":" + entry.getCantitate())
                .collect(Collectors.joining(","));
        return entity.getId() + "," +
                ingrediente;
    }
}

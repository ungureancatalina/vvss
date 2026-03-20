
package drinkshop.repository.file;

import drinkshop.domain.Ingredient;
import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.repository.RepositoryException;

public class FileStocRepository
        extends FileAbstractRepository<Integer, Stoc> {
    private final Repository<Integer, Ingredient> ingredientRepository;

    public FileStocRepository(String fileName, Repository<Integer, Ingredient> ingrRepo) throws RepositoryException {
        super(fileName);
        this.ingredientRepository = ingrRepo;
        loadFromFile();
    }

    @Override
    protected Integer getId(Stoc entity) {
        return entity.getId();
    }

    @Override
    protected Stoc extractEntity(String line) {
        String[] elems = line.split(";");

        int id = Integer.parseInt(elems[0]);
        Ingredient ingredient = ingredientRepository.findOne(Integer.parseInt(elems[1]));
        double cantitate = Double.parseDouble(elems[2]);
        double stocMinim = Double.parseDouble(elems[3]);

        return new Stoc(id, ingredient, cantitate, stocMinim);
    }

    @Override
    protected String createEntityAsString(Stoc entity) {
        return entity.getId() + ";" +
                entity.getIngredient().getId() + ";" +
                entity.getCantitate() + ";" +
                entity.getStocMinim();
    }
}

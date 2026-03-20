
package drinkshop.repository.file;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Ingredient;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.RepositoryException;

import java.io.File;

public class FileIngredientRepository extends FileAbstractRepository<Integer, Ingredient> {
    public FileIngredientRepository(String fileName) throws RepositoryException {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(Ingredient entity) {
        return entity.getId();
    }

    @Override
    protected Ingredient extractEntity(String line) {

        String[] elems = line.split(",");

        int id = Integer.parseInt(elems[0]);
        String name = elems[1];

        return new Ingredient(id, name);
    }

    @Override
    protected String createEntityAsString(Ingredient entity) {
        return entity.getId() + "," + entity.getDenumire();
    }

}

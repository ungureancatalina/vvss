
package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.repository.RepositoryException;
import drinkshop.repository.file.*;
import drinkshop.service.DrinkShopService;
import drinkshop.service.validator.StocValidator;
import drinkshop.service.validator.Validator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DrinkShopApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ---------- Initializare Repository-uri care citesc din fisiere ----------

        Repository<Integer, Product> productRepo;
        Repository<Integer, Order> orderRepo;
        Repository<Integer, Ingredient> ingrRepo;
        Repository<Integer, Reteta> retetaRepo;
        Repository<Integer, Stoc> stocRepo;
        try {
            productRepo = new FileProductRepository("data/products.txt");
            orderRepo = new FileOrderRepository("data/orders.txt", productRepo);
            ingrRepo = new FileIngredientRepository("data/ingrediente.txt");
            retetaRepo = new FileRetetaRepository("data/retete.txt", ingrRepo);
            stocRepo = new FileStocRepository("data/stocuri.txt", ingrRepo);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        Validator<Stoc> stocValidator = new StocValidator();

        // ---------- Initializare Service ----------
        DrinkShopService service = new DrinkShopService(productRepo, orderRepo, retetaRepo, stocRepo, ingrRepo,stocValidator);

        // ---------- Incarcare FXML ----------

        FXMLLoader loader = new FXMLLoader(getClass().getResource("drinkshop.fxml"));
        Scene scene = new Scene(loader.load());

        // ---------- Setare Service in Controller ----------
        DrinkShopController controller = loader.getController();
        controller.setService(service);

        // ---------- Afisare Fereastra ----------
        stage.setTitle("Coffee Shop Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

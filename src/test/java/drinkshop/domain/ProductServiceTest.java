package drinkshop.domain;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.RepositoryException;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.ProductService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("BlackBox")
class ProductServiceTest {

    private ProductService service;

    @BeforeEach
    void setUp() {
        try {
            java.io.File file = new java.io.File("test_file.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            drinkshop.repository.file.FileProductRepository repo =
                    new FileProductRepository("test_file.txt");

            service = new drinkshop.service.ProductService(repo);

        } catch (Exception e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @DisplayName("EC_TC1: Valid (Apa minerala, 5.50)")
    void test_EC_TC1_Valid() {
        CategorieBautura categorieDummy = new CategorieBautura(1, "WATER_BASED");
        TipBautura tipDummy = new TipBautura(1, "BASIC");
        Product p = new Product(1, "Apa minerala", 5.50, categorieDummy, tipDummy);

        assertDoesNotThrow(() -> {
            service.addProduct(p);
        }, "Produsul ar trebui sa fie adaugat cu succes.");
    }

    @Test
    @DisplayName("EC_TC2: Invalid (Nume este spatiu gol)")
    void test_EC_TC2_Invalid_Nume() {
        CategorieBautura categorieDummy = new CategorieBautura(1, "WATER_BASED");
        TipBautura tipDummy = new TipBautura(1, "BASIC");
        Product p = new Product(2, " ", 5.50, categorieDummy, tipDummy);

        Exception exception = assertThrows(Exception.class, () -> {
            service.addProduct(p);
        });

        assertTrue(exception.getMessage().contains("Numele nu poate fi gol!"),
                "Ar trebui sa arunce mesajul de eroare corect de la validator.");
    }

    @Test
    @DisplayName("EC_TC3: Invalid (Pret negativ -1.50)")
    void test_EC_TC3_Invalid_Pret() {
        CategorieBautura categorieDummy = new CategorieBautura(1, "WATER_BASED");
        TipBautura tipDummy = new TipBautura(1, "BASIC");
        Product p = new Product(3, "Apa minerala", -1.50, categorieDummy, tipDummy);

        Exception exception = assertThrows(Exception.class, () -> {
            service.addProduct(p);
        });

        assertTrue(exception.getMessage().contains("Pret invalid!"),
                "Ar trebui sa arunce mesajul de eroare corect de la validator.");
    }

    @Test
    @DisplayName("BVA_TC1: Valid (Cola, Limita inferioara 0.01)")
    void test_BVA_TC1_Valid_Limita() {
        CategorieBautura categorieDummy = new CategorieBautura(1, "WATER_BASED");
        TipBautura tipDummy = new TipBautura(1, "BASIC");
        Product p = new Product(4, "Cola", 0.01, categorieDummy, tipDummy);

        assertDoesNotThrow(() -> {
            service.addProduct(p);
        }, "Produsul Cola ar trebui sa fie adaugat (prețul 0.01 e valid).");
    }

    @Test
    @DisplayName("BVA_TC2: Invalid (Cola, Sub limita 0.00)")
    void test_BVA_TC2_Invalid_Zero() {
        CategorieBautura categorieDummy = new CategorieBautura(1, "WATER_BASED");
        TipBautura tipDummy = new TipBautura(1, "BASIC");
        Product p = new Product(5, "Cola", 0.00, categorieDummy, tipDummy);

        Exception exception = assertThrows(Exception.class, () -> {
            service.addProduct(p);
        });

        assertTrue(exception.getMessage().contains("Pret invalid!"),
                "Ar trebui sa dea eroare pentru pretul 0.00.");
    }

    @Test
    @DisplayName("BVA_TC3: Valid (Apa minerala, Pret normal 5.50)")
    void test_BVA_TC3_Valid_Normal() {
        CategorieBautura categorieDummy = new CategorieBautura(1, "WATER_BASED");
        TipBautura tipDummy = new TipBautura(1, "BASIC");
        Product p = new Product(6, "Apa minerala", 5.50, categorieDummy, tipDummy);

        assertDoesNotThrow(() -> {
            service.addProduct(p);
        });
    }

    @Test
    @DisplayName("BVA_TC4: Invalid (Apa minerala, Pret negativ -1.50)")
    void test_BVA_TC4_Invalid_Negativ() {
        CategorieBautura categorieDummy = new CategorieBautura(1, "WATER_BASED");
        TipBautura tipDummy = new TipBautura(1, "BASIC");
        Product p = new Product(7, "Apa minerala", -1.50, categorieDummy, tipDummy);

        Exception exception = assertThrows(Exception.class, () -> {
            service.addProduct(p);
        });

        assertTrue(exception.getMessage().contains("Pret invalid!"));
    }
}
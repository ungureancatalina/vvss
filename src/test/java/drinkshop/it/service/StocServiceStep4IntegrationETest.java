package drinkshop.it.service;

import drinkshop.domain.Ingredient;
import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.repository.RepositoryException;
import drinkshop.repository.file.FileStocRepository;
import drinkshop.service.StocService;
import drinkshop.service.validator.StocValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Step 4: Integrare E (se testează S + V + R cu E)
 * Strategie: Top-down, Breadth First
 * Real: StocService (S), StocValidator (V), FileStocRepository (R), Stoc (E)
 * Mock: Niciunul
 */

public class StocServiceStep4IntegrationETest {

    private StocService stocService;
    private StocValidator stocValidator;
    private FileStocRepository stocRepo;
    private Repository<Integer, Ingredient> ingredientRepoMock;
    private final String testFileName = "data/stocuri_it_step4_test.txt";

    @BeforeEach
    void setUp() throws IOException, RepositoryException {
        // Pregătim un fișier gol pentru repository-ul real pentru a asigura un mediu curat pentru fiecare test
        Path path = Paths.get(testFileName);
        Files.createDirectories(path.getParent());
        Files.write(path, new byte[0]);

        // Inițializăm toate obiectele reale pentru a testa integrarea completă
        stocValidator = new StocValidator();
        ingredientRepoMock = mock(Repository.class);
        stocRepo = new FileStocRepository(testFileName, ingredientRepoMock);
        stocService = new StocService(stocRepo);
    }

    // Curățăm fișierul după fiecare test pentru a preveni interferența între teste
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(testFileName));
    }

    @Test
    @DisplayName("Integration Step 4 - Add Success (All Real Objects)")
    void testAddSuccessFullIntegration() {
        // Arrange
        // Creăm un obiect Stoc real cu date valide pentru a trece validarea și a fi adăugat în repository
        Ingredient menta = new Ingredient(1, "Mentă");
        when(ingredientRepoMock.findOne(1)).thenReturn(menta);

        // Creăm un obiect Stoc real, folosind obiectul menta, nu un String
        Stoc realStoc = new Stoc(200, menta, 50.0, 10.0);

        // Act
        // Adăugăm stocul folosind serviciul real, care va valida și salva în repository-ul real
        stocValidator.validate(realStoc);
        stocService.add(realStoc);

        // Assert
        // Verificăm că stocul a fost adăugat corect în repository-ul real și că datele sunt consistente
        assertEquals(1, stocRepo.findAll().size());
        Stoc savedStoc = stocRepo.findOne(200);
        assertNotNull(savedStoc);
        assertEquals(menta, savedStoc.getIngredient());
        assertEquals(50.0, savedStoc.getCantitate());
    }

    @Test
    @DisplayName("Integration Step 4 - Add Failure (All Real Objects)")
    void testAddFailureFullIntegration() {
        // Arrange
        // Creăm un obiect Stoc real cu date invalide pentru a provoca o eroare de validare și a verifica că nu se adaugă în repository
        Ingredient miere = new Ingredient(2, "Miere");
        Stoc invalidStoc = new Stoc(201, miere, 5.0, 10.0);

        // Act & Assert
        // Încercăm să adăugăm stocul invalid și verificăm că se aruncă o excepție de validare
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            stocValidator.validate(invalidStoc);
            stocService.add(invalidStoc);
        });

        // Verify
        // Verificăm că mesajul excepției conține informații despre motivul eșecului
        assertTrue(ex.getMessage().contains("Cantitatea este sub stocul minim!"));

        // Verificăm că stocul nu a fost adăugat în repository-ul real din cauza validării eșuate
        assertEquals(0, stocRepo.findAll().size());
    }
}
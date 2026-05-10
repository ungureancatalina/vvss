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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Step 3: Integrare R (se testează S + V cu R; pentru E se folosesc obiecte mock)
 * Strategie: Top-down, Breadth First
 * Real: StocService (S), StocValidator (V), FileStocRepository (R)
 * Mock: Stoc (E)
 */

public class StocServiceStep3IntegrationRTest {

    private StocService stocService;
    private StocValidator stocValidator;
    private FileStocRepository stocRepo;
    private Repository<Integer, Ingredient> ingredientRepoMock;
    private Stoc mockStoc;
    private final String testFileName = "data/stocuri_it_test.txt";

    @BeforeEach
    void setUp() throws IOException, RepositoryException {
        // Pregătim un fișier gol pentru repository-ul real pentru a asigura un mediu curat pentru fiecare test
        Path path = Paths.get(testFileName);
        Files.createDirectories(path.getParent());
        Files.write(path, new byte[0]);

        // Inițializăm obiectele reale și mock
        stocValidator = new StocValidator();
        ingredientRepoMock = mock(Repository.class);
        stocRepo = new FileStocRepository(testFileName, ingredientRepoMock);
        mockStoc = mock(Stoc.class);
        stocService = new StocService(stocRepo,stocValidator);
    }

    // Curățăm fișierul după fiecare test pentru a preveni interferența între teste
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(testFileName));
    }

    @Test
    @DisplayName("Integration Step 3 - Add Success (Real Validator & Repo)")
    void testAddSuccessWithRealRepo() {
        Ingredient mockIngredient = mock(Ingredient.class);
        // Arrange
        // Configurăm mockStoc să aibă date valide pentru a trece validarea
        when(mockStoc.getId()).thenReturn(100);
        when(mockStoc.getIngredient()).thenReturn(mockIngredient);
        when(mockStoc.getCantitate()).thenReturn(20.0);
        when(mockStoc.getStocMinim()).thenReturn(10.0);

        // Act
        // Adăugăm stocul folosind serviciul, care va valida și apoi va salva în repository-ul real
        stocValidator.validate(mockStoc);
        stocService.add(mockStoc);

        // Assert
        // Verificăm că stocul a fost adăugat în repository-ul real și că datele sunt corecte
        assertEquals(1, stocRepo.findAll().size());
        assertEquals(mockIngredient, stocRepo.findOne(100).getIngredient());
    }

    @Test
    @DisplayName("Integration Step 2 - Add Failure (Real Validator & Repo)")
    void testAddFailureWithRealRepo() {
        // Arrange
        // Configurăm mockStoc să aibă date invalide pentru a provoca o eroare de validare
        when(mockStoc.getId()).thenReturn(-5);

        // Act & Assert
        // Așteptăm ca metoda add să arunce o ValidationException din cauza datelor invalide
        assertThrows(ValidationException.class, () -> {
            stocValidator.validate(mockStoc);
            stocService.add(mockStoc);
        });

        // Verify
        // Verificăm că stocul nu a fost adăugat în repository-ul real din cauza validării eșuate
        assertEquals(0, stocRepo.findAll().size());
    }
}
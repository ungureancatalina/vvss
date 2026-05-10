package drinkshop.it.service;

import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.service.StocService;
import drinkshop.service.validator.StocValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Step 2: Integrare V (se testează S cu V; pentru R se folosesc obiecte mock)
 * Strategie: Top-down, Breadth First
 * Real: StocService (S), StocValidator (V)
 * Mock: Repository (R), Stoc (E)
 */

public class StocServiceStep2IntegrationVTest {

    private StocService stocService;
    private StocValidator stocValidator;
    private Repository<Integer, Stoc> stocRepo;
    private Stoc mockStoc;

    @BeforeEach
    void setUp() {
        // V rămâne real, pentru că vrem să testăm integrarea cu validatorul real
        stocValidator = new StocValidator();
        // R rămâne mock, pentru că nu ne interesează încă integrarea cu repository-ul real (vom face asta în Step 3)
        stocRepo = mock(Repository.class);
        // E rămâne mock, pentru că nu vrem să depindem de implementarea concretă a clasei Stoc (vom face asta în Step 4)
        mockStoc = mock(Stoc.class);

        stocService = new StocService(stocRepo, stocValidator);
    }

    @Test
    @DisplayName("Integration Step 2 - Add Success (Real Validator)")
    void testAddSuccessWithRealValidator() {
        // Arrange
        // Configurăm mockStoc să aibă date valide pentru a trece validarea
        when(mockStoc.getId()).thenReturn(1);
        when(mockStoc.getIngredient()).thenReturn("Portocale");
        when(mockStoc.getCantitate()).thenReturn(10.0);
        when(mockStoc.getStocMinim()).thenReturn(5.0);

        // Nu trebuie să configurăm stocValidator pentru că este real și va valida conform implementării sale
        when(stocRepo.save(mockStoc)).thenReturn(mockStoc);

        // Act
        stocService.add(mockStoc);

        // Verify
        // Verificăm că validatorul a fost apelat o singură dată cu mockStoc
        verify(stocRepo, times(1)).save(mockStoc);
    }

    @Test
    @DisplayName("Integration Step 2 - Add Failure (Real Validator)")
    void testAddFailureWithRealValidator() {
        // Arrange
        // Configurăm mockStoc să aibă date invalide pentru a provoca o eroare de validare
        when(mockStoc.getId()).thenReturn(1);
        when(mockStoc.getIngredient()).thenReturn("Portocale");
        when(mockStoc.getCantitate()).thenReturn(-1.0); // INVALID

        // Act & Assert
        // Așteptăm ca metoda add să arunce o ValidationException din cauza datelor invalide
        assertThrows(ValidationException.class, () -> {
            stocService.add(mockStoc);
        });

        // Verify
        // Verificăm că metoda save nu a fost apelată deoarece validarea a eșuat
        verify(stocRepo, never()).save(any());
    }
}

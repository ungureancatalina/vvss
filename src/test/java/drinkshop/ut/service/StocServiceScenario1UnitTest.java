package drinkshop.ut.service;

import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.service.StocService;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Task: Unit Testing (Testarea în izolare) pentru clasa S (StocService)
 * Scenariu: (1) V <--- S ---> R
 * Obiecte Mock: Validator (V), Repository (R), Stoc (E)
 */

public class StocServiceScenario1UnitTest {

    // Obiecte colaboratoare mock și obiectul testat
    private Repository<Integer, Stoc> stocRepo;
    private Validator<Stoc> stocValidator;
    private StocService stocService;
    private Stoc mockStoc;

    // Inițializare înainte de fiecare test pentru a asigura un mediu curat și independent
    @BeforeEach
    void setUp() {
        // Cream obiecte mock
        stocRepo = mock(Repository.class);
        stocValidator = mock(Validator.class);
        mockStoc = mock(Stoc.class);
        stocService = new StocService(stocRepo, stocValidator);
    }

    @Test
    @DisplayName("Test Add Success - Verifică fluxul normal de adăugare")
    void testAddSuccess() {
        // ARANJARE (Arrange / Given)

        // Configurăm comportamentul mock-urilor pentru a simula un scenariu de succes
        doNothing().when(stocValidator).validate(mockStoc);
        // Simulăm că metoda save returnează stocul adăugat
        when(stocRepo.save(mockStoc)).thenReturn(mockStoc);

        // ACȚIUNE (Act / When)
        stocService.add(mockStoc);

        // ASSERT (Assert / Then)
        // Verificăm că metodele validate și save au fost apelate o singură dată cu argumentul corect
        verify(stocValidator, times(1)).validate(mockStoc);
        verify(stocRepo, times(1)).save(mockStoc);
        // Verificăm că nu s-au aruncat excepții în timpul adăugării
        assertDoesNotThrow(() -> {
        });
    }

    @Test
    @DisplayName("Test Add Failure - Verifică faptul că nu se salvează dacă validarea eșuează")
    void testAddValidationError() {
        // ARANJARE (Arrange / Given)

        // Configurăm comportamentul mock-ului pentru a simula o excepție de validare
        String errorMsg = "Cantitate negativa!";
        // Simulăm că metoda validate aruncă o excepție de validare când se încearcă adăugarea unui stoc invalid
        doThrow(new ValidationException(errorMsg)).when(stocValidator).validate(mockStoc);

        // ACȚIUNE (Act / When)
        // Testăm metoda add și capturăm excepția aruncată
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            stocService.add(mockStoc);
        });

        // ASSERT (Assert / Then)
        // Verificăm că mesajul excepției este cel așteptat
        assertEquals(errorMsg, exception.getMessage());
        // Verificăm că metoda validate a fost apelată o singură dată, iar metoda save nu a fost apelată deloc
        verify(stocValidator, times(1)).validate(mockStoc);
        verify(stocRepo, never()).save(any());
    }
}

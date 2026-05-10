package drinkshop.domain;

import drinkshop.repository.Repository;
import drinkshop.service.StocService;
import drinkshop.service.validator.StocValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("WhiteBox")
class StocServiceTest {

    private InMemoryStocRepository repo;
    private StocService service;
    private StocValidator validator;

    @BeforeEach
    void setUp() {
        repo = new InMemoryStocRepository();
        validator = new StocValidator();
        service = new StocService(repo,validator);
    }

    @Test
    void F02_TC01_stocInsuficient_aruncaExceptie() {
        Ingredient cafea = new Ingredient(1, "Cafea");
        repo.save(new Stoc(1, cafea, 4, 0));

        Reteta reteta = new Reteta(1, List.of(
                new IngredientReteta(cafea, 10)
        ));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.consuma(reteta));

        assertEquals("Stoc insuficient pentru rețeta.", ex.getMessage());
        assertEquals(4, repo.findOne(1).getCantitate(), 0.001);
    }

    @Test
    void F02_TC02_retetaFaraIngrediente_nuAruncaExceptie() {
        Reteta reteta = new Reteta(2, new ArrayList<>());

        assertDoesNotThrow(() -> service.consuma(reteta));
        assertTrue(repo.findAll().isEmpty());
    }

    @Test
    void F02_TC03_unIngredient_oIntrare_consumValid() {
        Ingredient cafea = new Ingredient(1, "Cafea");
        repo.save(new Stoc(1, cafea, 5, 0));

        Reteta reteta = new Reteta(3, List.of(
                new IngredientReteta(cafea, 3)
        ));

        assertDoesNotThrow(() -> service.consuma(reteta));

        assertEquals(2, repo.findOne(1).getCantitate(), 0.001);
    }

    @Test
    void F02_TC04_unIngredient_douaIntrari_consumValid() {
        Ingredient lapte = new Ingredient(2, "Lapte");
        repo.save(new Stoc(1, lapte, 2, 0));
        repo.save(new Stoc(2, lapte, 5, 0));

        Reteta reteta = new Reteta(4, List.of(
                new IngredientReteta(lapte, 3)
        ));

        assertDoesNotThrow(() -> service.consuma(reteta));

        assertEquals(0, repo.findOne(1).getCantitate(), 0.001);
        assertEquals(4, repo.findOne(2).getCantitate(), 0.001);
    }

    @Test
    void F02_TC05_douaIngrediente_consumValid() {
        Ingredient cafea = new Ingredient(1, "Cafea");
        Ingredient zahar = new Ingredient(2, "Zahar");

        repo.save(new Stoc(1, cafea, 5, 0));
        repo.save(new Stoc(2, zahar, 3, 0));

        Reteta reteta = new Reteta(5, List.of(
                new IngredientReteta(cafea, 2),
                new IngredientReteta(zahar, 1)
        ));

        assertDoesNotThrow(() -> service.consuma(reteta));

        assertEquals(3, repo.findOne(1).getCantitate(), 0.001);
        assertEquals(2, repo.findOne(2).getCantitate(), 0.001);
    }

    private static class InMemoryStocRepository implements Repository<Integer, Stoc> {
        private final Map<Integer, Stoc> data = new LinkedHashMap<>();

        @Override
        public Stoc findOne(Integer id) {
            return data.get(id);
        }

        @Override
        public List<Stoc> findAll() {
            return new ArrayList<>(data.values());
        }

        @Override
        public Stoc save(Stoc entity) {
            return data.put(entity.getId(), entity);
        }

        @Override
        public Stoc delete(Integer id) {
            return data.remove(id);
        }

        @Override
        public Stoc update(Stoc entity) {
            data.put(entity.getId(), entity);
            return entity;
        }
    }
}
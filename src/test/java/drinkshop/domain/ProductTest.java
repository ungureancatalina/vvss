package drinkshop.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product product;
    CategorieBautura juiceCategory;
    TipBautura waterBasedType;

    @BeforeEach
    void setUp() {
        juiceCategory = new CategorieBautura(1, "JUICE");
        waterBasedType = new TipBautura(1, "WATER_BASED");

        product = new Product(100, "Limonada", 10.0, juiceCategory, waterBasedType);
    }

    @AfterEach
    void tearDown() {
        product = null;
    }

    @Test
    void getId() {
        assertEquals(100, product.getId());
    }

    @Test
    void getNume() {
        assertEquals("Limonada", product.getNume());
    }

    @Test
    void getPret() {
        assertEquals(10.0, product.getPret());
    }

    @Test
    void getCategorie() {
        assertEquals(juiceCategory, product.getCategorie());
    }

    @Test
    void setCategorie() {
        CategorieBautura smoothieCategory = new CategorieBautura(2, "SMOOTHIE");
        product.setCategorie(smoothieCategory);
        assertEquals(smoothieCategory, product.getCategorie());
    }

    @Test
    void getTip() {
        assertEquals(waterBasedType, product.getTip());
    }

    @Test
    void setTip() {
        TipBautura basicType = new TipBautura(2, "BASIC");
        product.setTip(basicType);
        assertEquals(basicType, product.getTip());
    }

    @Test
    void setNume() {
        product.setNume("newLimonada");
        assertEquals("newLimonada", product.getNume());
    }

    @Test
    void setPret() {
        product.setPret(10.05);
        assertEquals(10.05, product.getPret());
    }

    @Test
    void testToString() {
        String expectedString = "Limonada (JUICE, WATER_BASED) - 10.0 lei";
        assertEquals(expectedString, product.toString());
    }
}
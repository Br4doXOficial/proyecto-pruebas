package cl.iplacex.automation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryServiceTest {
    private InventoryService service;

    @BeforeEach
    void setUp() {
        service = new InventoryService(new InMemoryInventoryRepository());
    }

    @Test
    void descuentaStockCuandoLaVentaEsValida() {
        service.register("SKU-001", "Teclado", 10);

        Product updated = service.sell("SKU-001", 3);

        assertEquals(7, updated.stock());
    }

    @Test
    void rechazaVentaCuandoNoExisteStockSuficiente() {
        service.register("SKU-002", "Mouse", 2);

        IllegalStateException error = assertThrows(
                IllegalStateException.class,
                () -> service.sell("SKU-002", 3)
        );

        assertEquals("Stock insuficiente", error.getMessage());
    }

    @Test
    void rechazaUnSkuDuplicado() {
        service.register("SKU-003", "Monitor", 4);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.register("SKU-003", "Monitor alternativo", 1)
        );
    }
}


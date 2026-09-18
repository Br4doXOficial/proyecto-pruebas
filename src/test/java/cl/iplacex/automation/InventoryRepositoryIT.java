package cl.iplacex.automation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryRepositoryIT {
    @Test
    void integraServicioYRepositorioDuranteUnaReposicion() {
        InventoryRepository repository = new InMemoryInventoryRepository();
        InventoryService service = new InventoryService(repository);
        service.register("SKU-010", "Notebook", 5);

        service.restock("SKU-010", 4);

        assertEquals(9, repository.findBySku("SKU-010").orElseThrow().stock());
    }

    @Test
    void integraRegistroVentaYConsultaFinal() {
        InventoryRepository repository = new InMemoryInventoryRepository();
        InventoryService service = new InventoryService(repository);
        service.register("SKU-011", "Webcam", 8);

        service.sell("SKU-011", 2);

        assertEquals(6, service.findRequired("SKU-011").stock());
    }
}


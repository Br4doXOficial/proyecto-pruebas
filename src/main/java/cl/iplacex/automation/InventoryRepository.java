package cl.iplacex.automation;

import java.util.Optional;

public interface InventoryRepository {
    void save(Product product);
    Optional<Product> findBySku(String sku);
}


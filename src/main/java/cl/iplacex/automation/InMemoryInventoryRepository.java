package cl.iplacex.automation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryInventoryRepository implements InventoryRepository {
    private final Map<String, Product> products = new HashMap<>();

    @Override
    public void save(Product product) {
        products.put(product.sku(), product);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(products.get(sku));
    }
}


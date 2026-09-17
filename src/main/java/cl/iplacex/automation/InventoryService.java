package cl.iplacex.automation;

public class InventoryService {
    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public Product register(String sku, String name, int initialStock) {
        if (repository.findBySku(sku).isPresent()) {
            throw new IllegalArgumentException("El SKU ya existe");
        }
        Product product = new Product(sku, name, initialStock);
        repository.save(product);
        return product;
    }

    public Product sell(String sku, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        Product product = findRequired(sku);
        if (product.stock() < quantity) {
            throw new IllegalStateException("Stock insuficiente");
        }
        Product updated = product.withStock(product.stock() - quantity);
        repository.save(updated);
        return updated;
    }

    public Product restock(String sku, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        Product product = findRequired(sku);
        Product updated = product.withStock(product.stock() + quantity);
        repository.save(updated);
        return updated;
    }

    public Product findRequired(String sku) {
        return repository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + sku));
    }
}


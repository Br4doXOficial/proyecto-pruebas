package cl.iplacex.automation;

public record Product(String sku, String name, int stock) {
    public Product {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("El SKU es obligatorio");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }

    public Product withStock(int newStock) {
        return new Product(sku, name, newStock);
    }
}


package cl.iplacex.automation;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        String command = args.length == 0 ? "demo" : args[0];
        if ("health".equalsIgnoreCase(command)) {
            System.out.println("HEALTHY");
            return;
        }
        if (!"demo".equalsIgnoreCase(command)) {
            System.err.println("Uso: java -jar inventario-automatizado.jar [demo|health]");
            System.exit(2);
        }

        InventoryService service = new InventoryService(new InMemoryInventoryRepository());
        service.register("SKU-001", "Teclado", 10);
        Product result = service.sell("SKU-001", 3);
        System.out.println("DEMO_OK sku=" + result.sku() + " stock=" + result.stock());
    }
}


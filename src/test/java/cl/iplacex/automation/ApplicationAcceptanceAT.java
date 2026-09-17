package cl.iplacex.automation;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationAcceptanceAT {
    @Test
    void ejecutaFlujoVisibleDeRegistroYVenta() throws IOException, InterruptedException {
        Path jar = Path.of("target", "inventario-automatizado-1.0.0.jar");
        Process process = new ProcessBuilder("java", "-jar", jar.toString(), "demo")
                .redirectErrorStream(true)
                .start();

        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();

        assertEquals(0, exitCode);
        assertTrue(output.contains("DEMO_OK sku=SKU-001 stock=7"));
    }
}


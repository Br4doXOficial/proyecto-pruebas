package com.ejemplo.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {

    private String password;

    @Given("el usuario está en la página de inicio de sesión")
    public void elUsuarioEstaEnLaPaginaDeInicioDeSesion() {
        System.out.println("✅ En página de login");
    }

    @When("ingresa el email {string}")
    public void ingresaElEmail(String email) {
        System.out.println("📧 Email: " + email);
    }

    @When("ingresa la contraseña {string}")
    public void ingresaLaContrasena(String password) {
        this.password = password;
        System.out.println("🔑 Contraseña: " + password);
    }

    @Then("el sistema redirige al dashboard")
    public void elSistemaRedirigeAlDashboard() {
        System.out.println("✅ Redirigiendo...");
    }

    @Then("muestra el mensaje {string}")
    public void muestraElMensaje(String esperado) {
        String actual = "123456".equals(password) ? "Bienvenido" : "Credenciales incorrectas";
        assertEquals(esperado, actual);
        System.out.println("📝 Mensaje: " + actual);
    }
}
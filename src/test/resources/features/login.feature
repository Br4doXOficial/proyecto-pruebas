Feature: Inicio de sesión

  Scenario: Login exitoso
    Given el usuario está en la página de inicio de sesión
    When ingresa el email "test@mail.com"
    And ingresa la contraseña "123456"
    Then el sistema redirige al dashboard

  Scenario: Login fallido
    Given el usuario está en la página de inicio de sesión
    When ingresa el email "test@mail.com"
    And ingresa la contraseña "wrong"
    Then muestra el mensaje "Credenciales incorrectas"
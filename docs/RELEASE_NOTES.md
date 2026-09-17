# Versión 1.0.0

Primera versión evaluable del proyecto.

## Incluye

- Aplicación Java 17 para control básico de inventario.
- Proyecto Maven con JUnit 5, Surefire y Failsafe.
- Tres pruebas unitarias, dos pruebas de integración y una prueba de aceptación.
- Pipeline de CI con stages separados para build y pruebas.
- Deployment pipeline con acceptance tests, estrategia Blue-Green, smoke test y rollback.
- Flujos equivalentes para GitHub Actions y Jenkins.

## Resultado de validación

La versión fue construida y validada localmente con Maven 3.9.16. Todas las pruebas finalizaron sin fallos y el ejercicio de despliegue comprobó el cambio entre slots `blue` y `green`, seguido de un rollback exitoso.

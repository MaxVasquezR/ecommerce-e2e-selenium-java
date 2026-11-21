# E-commerce E2E – Selenium WebDriver + Java

Automatización del flujo de ventas de [automationexercise.com](https://automationexercise.com) para el curso **Calidad de Software (IDAT)**.
El proyecto cubre las etapas críticas del negocio (registro, inicio de sesión, carrito y checkout), valida casos negativos y se ejecuta en **Google Chrome** y **Microsoft Edge**.

## Tecnologías

| Herramienta | Versión | Uso |
|---|---|---|
| Java (JDK) | 17 o superior | Lenguaje |
| Maven | 3.9 o superior | Build y dependencias |
| Selenium WebDriver | 4.53.0 | Automatización del navegador |
| Selenium Manager | incluido en Selenium | Descarga automática de chromedriver / msedgedriver |
| TestNG | 7.12.0 | Framework de pruebas, data providers y ejecución en paralelo |

No hace falta descargar drivers a mano: **Selenium Manager** detecta la versión instalada de Chrome o Edge y descarga el driver compatible en `~/.cache/selenium`.

## Cobertura

| Clase | Tipo | Qué valida |
|---|---|---|
| `PurchaseFlowE2ETest` | E2E | Flujo completo: registro → logout → login → 2 productos al carrito → checkout (dirección, ítems, total) → pago → orden confirmada → eliminación de la cuenta |
| `RegistrationTest` | Positivo | Creación de usuario con todos los datos de la cuenta |
| `LoginTest` | Positivo | Login y logout con credenciales válidas |
| `CartTest` | Positivo | Varios productos desde el catálogo; cantidad personalizada desde el detalle (precio × cantidad) |
| `NegativeLoginTest` | Negativo | Contraseña incorrecta, correo no registrado, contraseña con otras mayúsculas, inyección SQL, correo vacío, contraseña vacía y correo sin formato válido |
| `NegativeSignupTest` | Negativo | Registro con un correo ya existente |
| `NegativeCheckoutTest` | Negativo | Checkout bloqueado para un usuario sin sesión |

En total son **14 ejecuciones por navegador**. Cada caso negativo comprueba (assert) que el sistema bloqueó el acceso y además **muestra en consola el mensaje de error del sistema**.

## Estructura

```
src/test/java/com/idat/qa
├── api/            AccountApiClient: crea y elimina cuentas vía API (preparación y limpieza de datos)
├── components/     Partes reutilizables de la UI: Header, modal "Added!", tabla del carrito
├── config/         ConfigReader: lee config.properties y permite sobrescribir con -D
├── driver/         BrowserType, DriverFactory (Chrome/Edge) y DriverManager (ThreadLocal)
├── listeners/      TestListener: muestra el avance en consola con el navegador usado
├── models/         User, Product, CartItem, PaymentCard (records)
├── pages/          Page Objects: Home, Login, AccountInformation, Products, Cart, Checkout, Payment...
├── tests/          Pruebas positivas y el flujo E2E
│   └── negative/   Pruebas negativas
└── utils/          TestDataFactory, ExpectedMessages, ScreenshotUtil
src/test/resources
├── config.properties
└── suites/
    ├── single-browser.xml   todas las pruebas en un navegador
    └── cross-browser.xml    todas las pruebas en Chrome y Edge, en paralelo
```

Decisiones de diseño:

- **Page Object Model.** Los selectores viven solo en las clases `pages`/`components`, y las pruebas solo usan métodos de negocio (`loginAs`, `addToCart`, `proceedToCheckout`, etc.).
- **Esperas dinámicas.** Todas las interacciones pasan por `WebDriverWait` en `BasePage`. No se usan `Thread.sleep` ni esperas implícitas.
- **Bloqueo de publicidad.** El sitio carga anuncios de Google (`#google_vignette`) que interrumpen la navegación. `DriverFactory` los bloquea con Chrome DevTools Protocol.
- **Datos aislados.** Cada prueba genera un usuario con correo único y lo elimina al terminar, aunque la prueba falle.
- **Paralelismo seguro.** `DriverManager` usa `ThreadLocal`, así que Chrome y Edge corren a la vez sin compartir sesión.

## Requisitos previos

1. **JDK 17 o superior.** Verificar con `java -version`.
2. **Maven 3.9 o superior.** Verificar con `mvn -v`.
3. **Google Chrome** y/o **Microsoft Edge** instalados, en su última versión.
4. Conexión a internet: para el sitio y para la primera descarga de drivers.

## Ejecución

```bash
# Todas las pruebas en Chrome (navegador por defecto)
mvn clean test

# Todas las pruebas en Edge
mvn clean test -Dbrowser=edge

# Cross-browser: Chrome y Edge en paralelo
mvn clean test -Dsuite=cross-browser

# Sin interfaz gráfica (headless), combinable con cualquiera de los anteriores
mvn clean test -Dheadless=true

# Una sola clase o un solo método
mvn clean test -Dtest=PurchaseFlowE2ETest
mvn clean test -Dtest=NegativeLoginTest -Dbrowser=edge
mvn clean test -Dtest=CartTest#shouldAddProductWithCustomQuantity
```

Cualquier clave de `config.properties` se puede sobrescribir con `-D<clave>=<valor>`, por ejemplo `-Dexplicit.wait.seconds=25`.

## Reportes

- **Consola:** cada prueba muestra `[RUN ]`, `[PASS]` o `[FAIL]` con el navegador, los pasos y los mensajes del sistema.
- **Reporte HTML de TestNG:** `target/surefire-reports/index.html`.
- **Capturas de fallas:** `target/screenshots/<NAVEGADOR>_<prueba>_<fecha>.png`, generadas automáticamente cuando una prueba falla.

## Solución de problemas

| Problema | Solución |
|---|---|
| `SessionNotCreatedException` o el driver no coincide con el navegador | Actualiza Chrome o Edge y borra la caché de drivers (`%USERPROFILE%\.cache\selenium` en Windows, `~/.cache/selenium` en Linux/macOS). |
| Sin acceso a internet o proxy corporativo al descargar drivers | Configura el proxy con `-Dhttps.proxyHost=... -Dhttps.proxyPort=...`, o descarga el driver y usa `-Dwebdriver.chrome.driver=C:\ruta\chromedriver.exe` (en Edge: `-Dwebdriver.edge.driver`). |
| `TimeoutException` por lentitud del sitio | Aumenta la espera: `-Dexplicit.wait.seconds=30`. |
| Aviso `Unable to find an exact match for CDP version` | Es informativo y no afecta las pruebas: el navegador es más nuevo que la versión de CDP que trae Selenium. |
| Aparece publicidad a pantalla completa | Verifica que `block.ads=true` en `config.properties`. |
| La consola de Windows muestra `ContraseÃ±a` en lugar de `Contraseña` | Ejecuta `chcp 65001` en CMD/PowerShell antes de `mvn` para usar UTF-8. El reporte HTML no se ve afectado. |
| `java` o `mvn` no se reconocen como comando | Agrega `JAVA_HOME\bin` y `MAVEN_HOME\bin` a la variable de entorno `PATH`. |

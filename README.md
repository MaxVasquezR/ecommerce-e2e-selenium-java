# E-commerce E2E – Selenium WebDriver + Java

## 1. Resumen

Proyecto de automatización de pruebas del flujo de ventas de [automationexercise.com](https://automationexercise.com), desarrollado para el curso **Calidad de Software (IDAT)**.

Valida de punta a punta las etapas críticas del negocio (**registro de usuario, inicio de sesión, carrito y checkout**), comprueba que el sistema bloquee los **errores de usuario** (casos negativos) y se ejecuta en **Google Chrome** y **Microsoft Edge**.

## 2. Tecnologías

| Herramienta | Versión | Uso |
|---|---|---|
| Java (JDK) | 17 o superior | Lenguaje |
| Apache Maven | 3.8 o superior | Build y gestión de dependencias |
| Selenium WebDriver | 4.53.0 | Automatización del navegador |
| Selenium Manager | incluido en Selenium | Gestión automática de drivers (chromedriver / msedgedriver) |
| TestNG | 7.12.0 | Ejecución de pruebas, data providers y paralelismo |

## 3. Entornos soportados

| Sistema operativo | Navegadores | Modo |
|---|---|---|
| Windows 10 / 11 | Chrome y Edge (últimas versiones) | Con ventana (por defecto) o headless |
| Linux (Ubuntu / Debian) | Chrome y Edge (últimas versiones) | Con ventana si hay entorno gráfico; headless en servidores o CI |

No hace falta descargar drivers: Selenium Manager detecta la versión instalada de cada navegador y descarga el driver compatible en la primera ejecución.

## 4. Requisitos previos

| Requisito | Verificación | Descarga |
|---|---|---|
| JDK 17 o superior | `java -version` | https://adoptium.net |
| Maven 3.8 o superior | `mvn -v` | https://maven.apache.org/download.cgi |
| Google Chrome | `chrome://settings/help` | https://www.google.com/chrome |
| Microsoft Edge | `edge://settings/help` | https://www.microsoft.com/edge (ya viene incluido en Windows) |
| Conexión a internet | — | Para acceder al sitio y descargar dependencias y drivers |

`java` y `mvn` deben estar en la variable de entorno `PATH`.

## 5. Ejecución

```bash
git clone https://github.com/MaxVasquezR/ecommerce-e2e-selenium-java.git
cd ecommerce-e2e-selenium-java
```

Desde la carpeta raíz del proyecto (la que contiene `pom.xml`):

| Objetivo | Comando |
|---|---|
| Todas las pruebas en Chrome (por defecto) | `mvn clean test` |
| Todas las pruebas en Edge | `mvn clean test -Dbrowser=edge` |
| Chrome y Edge en paralelo (cross-browser) | `mvn clean test -Dsuite=cross-browser` |
| Solo el flujo de compra E2E | `mvn clean test -Dtest=PurchaseFlowE2ETest` |
| Una clase en un navegador concreto | `mvn clean test -Dtest=NegativeLoginTest -Dbrowser=edge` |
| Sin ventana (headless) | agregar `-Dheadless=true` a cualquier comando |

- Los comandos son iguales en Windows (CMD o PowerShell) y en Linux.
- La primera ejecución tarda más, porque se descargan las dependencias y los drivers.
- Resultado esperado: `Tests run: 14, Failures: 0` por navegador (28 en cross-browser) y `BUILD SUCCESS`.

## 6. Pruebas incluidas

| Clase | Tipo | Qué valida |
|---|---|---|
| `PurchaseFlowE2ETest` | E2E | Registro → logout → login → 2 productos al carrito → checkout (dirección, ítems y total) → pago → orden confirmada |
| `RegistrationTest` | Positivo | Creación de un usuario con todos los datos de la cuenta |
| `LoginTest` | Positivo | Inicio y cierre de sesión con credenciales válidas |
| `CartTest` | Positivo | Varios productos desde el catálogo; cantidad personalizada (precio × cantidad) |
| `NegativeLoginTest` | Negativo | Contraseña incorrecta, correo no registrado, contraseña con otras mayúsculas, inyección SQL, correo vacío, contraseña vacía y correo sin formato |
| `NegativeSignupTest` | Negativo | Registro con un correo ya existente |
| `NegativeCheckoutTest` | Negativo | Checkout bloqueado para un usuario sin sesión |

Son **14 ejecuciones por navegador**. Los casos negativos comprueban que el sistema bloqueó el acceso y muestran en consola el mensaje de error del sistema. Los datos de prueba son ficticios, y las cuentas creadas se eliminan al terminar cada prueba.

## 7. Reportes

| Ubicación | Contenido |
|---|---|
| Consola | Avance en vivo: `[RUN ]`, `[PASS]` o `[FAIL]`, con el navegador, los pasos y los mensajes del sistema |
| `target/surefire-reports/index.html` | Reporte HTML de TestNG |
| `target/screenshots/` | Capturas automáticas de las pruebas que fallan |

## 8. Arquitectura técnica

```
src/test/java/com/idat/qa
├── api/          Cliente de la API del sitio (preparación y limpieza de datos)
├── components/   Componentes reutilizables de la UI (header, modal del carrito, tabla del carrito)
├── config/       Lectura de config.properties
├── driver/       Creación y gestión del WebDriver (Chrome / Edge)
├── listeners/    Log de ejecución en consola
├── models/       Modelos de datos (User, Product, CartItem, PaymentCard)
├── pages/        Page Objects
├── tests/        Pruebas positivas y E2E
│   └── negative/ Pruebas negativas
└── utils/        Datos de prueba, mensajes esperados y capturas
src/test/resources
├── config.properties    URL, navegador, headless, tiempos de espera
└── suites/              single-browser.xml / cross-browser.xml
```

- **Page Object Model:** los selectores están solo en `pages` y `components`; las pruebas usan métodos de negocio.
- **Esperas dinámicas:** se usa `WebDriverWait` en todas las interacciones, sin `Thread.sleep` ni esperas implícitas.
- **Aislamiento:** cada prueba usa un navegador nuevo y un usuario con correo único.
- **Ejecución en paralelo:** el WebDriver se gestiona por hilo (`ThreadLocal`), así que Chrome y Edge pueden correr a la vez.
- **Configuración externa:** cualquier valor de `config.properties` se puede sobrescribir con `-D<clave>=<valor>`.

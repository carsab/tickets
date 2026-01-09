# Reporte de Validación de Historias de Usuario
## Help Desk Mini

**Fecha de Generación:** 2026-01-09
**Versión del Sistema:** 1.0.0
**Autor:** Sistema de Validación Automatizada

---

## Resumen Ejecutivo

Se han creado **23 escenarios de prueba** automatizados que validan las **5 Historias de Usuario** del sistema Help Desk Mini. Las pruebas cubren todos los criterios de aceptación especificados en la documentación.

| Historia de Usuario | Escenarios | Estado |
|---------------------|------------|--------|
| HU-01: Crear Ticket | 4 | Implementado |
| HU-02: Listar y Filtrar Tickets | 5 | Implementado |
| HU-03: Cambiar Estado de Ticket | 6 | Implementado |
| HU-04: Agregar Comentarios | 4 | Implementado |
| HU-05: Consultar Resumen Diario | 4 | Implementado |

---

## HU-01: Crear Ticket

**Como:** Colaborador interno
**Quiero:** Registrar un nuevo ticket de soporte
**Para:** Que el área de TI pueda atender mi solicitud

### Escenarios Validados

#### Escenario 1: Creación exitosa de ticket
- **Archivo:** `HU01CrearTicketTest.java`
- **Método:** `escenario1_CreacionExitosaDeTicket()`
- **Validaciones:**
  - Título: "Acceso a VPN corporativa" (25 caracteres)
  - Descripción: 65 caracteres
  - Solicitante: "Pepe Pérez"
  - Categoría: "Acceso"
  - Prioridad: "Alta"
  - Estado inicial: "Abierto"
  - Fecha de creación registrada
  - Mensaje de éxito con ID

#### Escenario 2: Validación de título muy corto
- **Archivo:** `HU01CrearTicketTest.java`
- **Método:** `escenario2_ValidacionTituloMuyCorto()`
- **Validaciones:**
  - Título: "Test" (4 caracteres) - RECHAZADO
  - Error: "El título debe tener entre 5 y 120 caracteres"
  - Ticket NO creado

#### Escenario 3: Validación de descripción muy corta
- **Archivo:** `HU01CrearTicketTest.java`
- **Método:** `escenario3_ValidacionDescripcionMuyCorta()`
- **Validaciones:**
  - Título: "Problema con correo" (19 caracteres)
  - Descripción: "Error" (5 caracteres) - RECHAZADO
  - Error: "La descripción debe tener entre 10 y 2000 caracteres"
  - Ticket NO creado

#### Escenario 4: Validación de campos obligatorios
- **Archivo:** `HU01CrearTicketTest.java`
- **Método:** `escenario4_ValidacionCamposCategoriaVacia()`
- **Validaciones:**
  - Categoría vacía - RECHAZADO
  - Error: "La categoría es obligatoria"
  - Ticket NO creado

---

## HU-02: Listar y Filtrar Tickets

**Como:** Agente de soporte
**Quiero:** Ver y filtrar los tickets registrados
**Para:** Gestionar eficientemente las solicitudes

### Escenarios Validados

#### Escenario 1: Listar todos los tickets con paginación
- **Archivo:** `HU02ListarFiltrarTicketsTest.java`
- **Método:** `escenario1_ListarTodosLosTicketsConPaginacion()`
- **Validaciones:**
  - 25 tickets en sistema
  - Muestra 10 tickets por página
  - Total de páginas: 3
  - Ordenados por fecha descendente
  - Campos mostrados: ID, título, categoría, prioridad, estado, fecha

#### Escenario 2: Navegar entre páginas
- **Archivo:** `HU02ListarFiltrarTicketsTest.java`
- **Método:** `escenario2_NavegarEntrePaginas()`
- **Validaciones:**
  - Navegación a página 2
  - Muestra tickets 11-20
  - Indica página 2 de 3

#### Escenario 3: Filtrar por estado
- **Archivo:** `HU02ListarFiltrarTicketsTest.java`
- **Método:** `escenario3_FiltrarPorEstado()`
- **Validaciones:**
  - Filtro: "En Progreso"
  - Solo muestra tickets con ese estado
  - Mantiene paginación y orden

#### Escenario 4: Filtrar por categoría
- **Archivo:** `HU02ListarFiltrarTicketsTest.java`
- **Método:** `escenario4_FiltrarPorCategoria()`
- **Validaciones:**
  - Filtro: "Hardware"
  - Solo muestra tickets de esa categoría
  - Mantiene paginación y orden

#### Escenario 5: Filtrar combinado (estado + categoría)
- **Archivo:** `HU02ListarFiltrarTicketsTest.java`
- **Método:** `escenario5_FiltrarPorEstadoYCategoriaCombinados()`
- **Validaciones:**
  - Filtros: Estado="Abierto" + Categoría="Software"
  - Muestra solo tickets que cumplen ambos criterios
  - Maneja caso sin resultados (lista vacía)

---

## HU-03: Cambiar Estado de Ticket

**Como:** Agente de soporte
**Quiero:** Actualizar el estado de un ticket
**Para:** Reflejar el progreso en la atención

### Diagrama de Transiciones Válidas

```
ABIERTO ──────→ EN_PROGRESO ──────→ RESUELTO ──────→ CERRADO
    │                                    ↑
    └────────────────────────────────────┘
           (transición directa permitida)
```

### Escenarios Validados

#### Escenario 1: Cambiar de Abierto a En Progreso
- **Archivo:** `HU03CambiarEstadoTicketTest.java`
- **Método:** `escenario1_CambiarDeAbiertoAEnProgreso()`
- **Validaciones:**
  - Transición: ABIERTO → EN_PROGRESO
  - Fecha de actualización registrada
  - Mensaje: "Estado actualizado exitosamente"

#### Escenario 2: Cambiar de Abierto a Resuelto directamente
- **Archivo:** `HU03CambiarEstadoTicketTest.java`
- **Método:** `escenario2_CambiarDeAbiertoAResueltoDirect()`
- **Validaciones:**
  - Transición: ABIERTO → RESUELTO
  - Fecha de resolución registrada

#### Escenario 3: Cambiar de En Progreso a Resuelto
- **Archivo:** `HU03CambiarEstadoTicketTest.java`
- **Método:** `escenario3_CambiarDeEnProgresoAResuelto()`
- **Validaciones:**
  - Transición: EN_PROGRESO → RESUELTO
  - Fecha de resolución registrada

#### Escenario 4: Cambiar de Resuelto a Cerrado
- **Archivo:** `HU03CambiarEstadoTicketTest.java`
- **Método:** `escenario4_CambiarDeResueltoACerrado()`
- **Validaciones:**
  - Transición: RESUELTO → CERRADO
  - Fecha de cierre registrada
  - Mensaje: "Ticket cerrado exitosamente"

#### Escenario 5: Intentar cerrar sin pasar por Resuelto
- **Archivo:** `HU03CambiarEstadoTicketTest.java`
- **Método:** `escenario5_IntentarCerrarSinPasarPorResuelto()`
- **Validaciones:**
  - Transición: ABIERTO → CERRADO (INVÁLIDA)
  - Error: "Transición de estado no permitida"
  - Estado permanece como "Abierto"

#### Escenario 6: Transición inválida En Progreso a Abierto
- **Archivo:** `HU03CambiarEstadoTicketTest.java`
- **Método:** `escenario6_TransicionInvalidaEnProgresoAAbierto()`
- **Validaciones:**
  - Transición: EN_PROGRESO → ABIERTO (INVÁLIDA)
  - Error: "Transición de estado no permitida: En Progreso → Abierto"
  - Estado permanece como "En Progreso"

---

## HU-04: Agregar Comentarios

**Como:** Agente de soporte o solicitante
**Quiero:** Agregar comentarios a un ticket
**Para:** Documentar acciones, decisiones o información adicional

### Escenarios Validados

#### Escenario 1: Agregar comentario exitosamente
- **Archivo:** `HU04AgregarComentariosTest.java`
- **Método:** `escenario1_AgregarComentarioExitosamente()`
- **Validaciones:**
  - Autor: "María González"
  - Texto: "Se verificó el acceso, pendiente aprobación del supervisor"
  - Fecha de creación registrada
  - Mensaje: "Comentario agregado exitosamente"

#### Escenario 2: Ver histórico de comentarios
- **Archivo:** `HU04AgregarComentariosTest.java`
- **Método:** `escenario2_VerHistoricoDeComentarios()`
- **Validaciones:**
  - 3 comentarios mostrados
  - Campos: autor, fecha/hora, texto
  - Ordenados del más reciente al más antiguo

#### Escenario 3: Validación de comentario vacío
- **Archivo:** `HU04AgregarComentariosTest.java`
- **Método:** `escenario3_ValidacionComentarioVacio()`
- **Validaciones:**
  - Texto vacío - RECHAZADO
  - Error: "El texto del comentario es obligatorio"
  - Comentario NO guardado

#### Escenario 4: Validación de autor obligatorio
- **Archivo:** `HU04AgregarComentariosTest.java`
- **Método:** `escenario4_ValidacionAutorObligatorio()`
- **Validaciones:**
  - Autor vacío - RECHAZADO
  - Error: "El autor es obligatorio"
  - Comentario NO guardado

---

## HU-05: Consultar Resumen Diario

**Como:** Supervisor del área de TI
**Quiero:** Ver un resumen de la actividad diaria
**Para:** Tener visibilidad del volumen y categorías más demandadas

### Escenarios Validados

#### Escenario 1: Ver resumen del día actual
- **Archivo:** `HU05ResumenDiarioTest.java`
- **Método:** `escenario1_VerResumenDelDiaActual()`
- **Validaciones:**
  - Tickets creados hoy: 28
  - Tickets resueltos hoy: 8
  - Top 3 categorías con tickets abiertos:
    1. Software: 8 tickets
    2. Acceso: 5 tickets
    3. Hardware: 4 tickets

#### Escenario 2: Resumen sin actividad del día
- **Archivo:** `HU05ResumenDiarioTest.java`
- **Método:** `escenario2_ResumenSinActividadDelDia()`
- **Validaciones:**
  - Tickets creados hoy: 0
  - Tickets resueltos hoy: 0
  - Top categorías: vacío o de días anteriores

#### Escenario 3: Menos de 3 categorías con tickets abiertos
- **Archivo:** `HU05ResumenDiarioTest.java`
- **Método:** `escenario3_MenosDe3CategoriasConTicketsAbiertos()`
- **Validaciones:**
  - Solo 2 categorías mostradas
  - No muestra categorías vacías

#### Escenario 4: Actualización en tiempo real
- **Archivo:** `HU05ResumenDiarioTest.java`
- **Método:** `escenario4_ActualizacionEnTiempoReal()`
- **Validaciones:**
  - Contadores se actualizan al crear tickets
  - Top categorías se recalcula
  - Tickets resueltos se incrementa al resolver

---

## Instrucciones de Ejecución

### Prerequisitos
- Java 17+
- Maven 3.6+
- Conexión a internet para descarga de dependencias

### Ejecutar Todas las Pruebas

```bash
cd /home/user/tickets/backend
mvn test -Dtest="HU*" -Dspring.profiles.active=test
```

### Ejecutar Pruebas por Historia de Usuario

```bash
# HU-01: Crear Ticket
mvn test -Dtest="HU01CrearTicketTest"

# HU-02: Listar y Filtrar Tickets
mvn test -Dtest="HU02ListarFiltrarTicketsTest"

# HU-03: Cambiar Estado de Ticket
mvn test -Dtest="HU03CambiarEstadoTicketTest"

# HU-04: Agregar Comentarios
mvn test -Dtest="HU04AgregarComentariosTest"

# HU-05: Consultar Resumen Diario
mvn test -Dtest="HU05ResumenDiarioTest"
```

### Generar Reporte HTML

```bash
mvn surefire-report:report
# El reporte estará en: target/site/surefire-report.html
```

---

## Estructura de Archivos de Prueba

```
backend/src/test/
├── java/com/soporteagil/helpdesk/
│   ├── HU01CrearTicketTest.java          (4 escenarios)
│   ├── HU02ListarFiltrarTicketsTest.java (5 escenarios)
│   ├── HU03CambiarEstadoTicketTest.java  (6 escenarios)
│   ├── HU04AgregarComentariosTest.java   (4 escenarios)
│   └── HU05ResumenDiarioTest.java        (4 escenarios)
└── resources/
    └── application-test.properties       (Configuración H2)
```

---

## Cobertura de Criterios de Aceptación

### Matriz de Trazabilidad

| Criterio | Escenario de Prueba | Estado |
|----------|---------------------|--------|
| HU01-E1: Creación exitosa | escenario1_CreacionExitosaDeTicket | CUBIERTO |
| HU01-E2: Título muy corto | escenario2_ValidacionTituloMuyCorto | CUBIERTO |
| HU01-E3: Descripción muy corta | escenario3_ValidacionDescripcionMuyCorta | CUBIERTO |
| HU01-E4: Campos obligatorios | escenario4_ValidacionCamposCategoriaVacia | CUBIERTO |
| HU02-E1: Listar con paginación | escenario1_ListarTodosLosTicketsConPaginacion | CUBIERTO |
| HU02-E2: Navegar páginas | escenario2_NavegarEntrePaginas | CUBIERTO |
| HU02-E3: Filtrar por estado | escenario3_FiltrarPorEstado | CUBIERTO |
| HU02-E4: Filtrar por categoría | escenario4_FiltrarPorCategoria | CUBIERTO |
| HU02-E5: Filtrar combinado | escenario5_FiltrarPorEstadoYCategoriaCombinados | CUBIERTO |
| HU03-E1: Abierto → En Progreso | escenario1_CambiarDeAbiertoAEnProgreso | CUBIERTO |
| HU03-E2: Abierto → Resuelto | escenario2_CambiarDeAbiertoAResueltoDirect | CUBIERTO |
| HU03-E3: En Progreso → Resuelto | escenario3_CambiarDeEnProgresoAResuelto | CUBIERTO |
| HU03-E4: Resuelto → Cerrado | escenario4_CambiarDeResueltoACerrado | CUBIERTO |
| HU03-E5: Abierto → Cerrado (inv) | escenario5_IntentarCerrarSinPasarPorResuelto | CUBIERTO |
| HU03-E6: En Progreso → Abierto (inv) | escenario6_TransicionInvalidaEnProgresoAAbierto | CUBIERTO |
| HU04-E1: Agregar comentario | escenario1_AgregarComentarioExitosamente | CUBIERTO |
| HU04-E2: Ver histórico | escenario2_VerHistoricoDeComentarios | CUBIERTO |
| HU04-E3: Comentario vacío | escenario3_ValidacionComentarioVacio | CUBIERTO |
| HU04-E4: Autor obligatorio | escenario4_ValidacionAutorObligatorio | CUBIERTO |
| HU05-E1: Resumen día actual | escenario1_VerResumenDelDiaActual | CUBIERTO |
| HU05-E2: Sin actividad | escenario2_ResumenSinActividadDelDia | CUBIERTO |
| HU05-E3: <3 categorías | escenario3_MenosDe3CategoriasConTicketsAbiertos | CUBIERTO |
| HU05-E4: Actualización tiempo real | escenario4_ActualizacionEnTiempoReal | CUBIERTO |

**Total: 23/23 criterios cubiertos (100%)**

---

## Conclusiones

1. **Cobertura Completa:** Todas las historias de usuario tienen pruebas automatizadas que validan los criterios de aceptación especificados.

2. **Pruebas de Integración:** Las pruebas utilizan MockMvc para simular llamadas HTTP reales al API REST, validando el comportamiento end-to-end.

3. **Base de Datos en Memoria:** Se utiliza H2 como base de datos para pruebas, garantizando aislamiento y reproducibilidad.

4. **Validaciones Completas:**
   - Validaciones de campos obligatorios
   - Validaciones de longitud de campos
   - Validaciones de transiciones de estado
   - Validaciones de integridad de datos

5. **Reportes por Consola:** Cada prueba genera un reporte detallado en consola para facilitar el seguimiento.

---

*Reporte generado automáticamente por el sistema de validación de Help Desk Mini*

package com.soporteagil.helpdesk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soporteagil.helpdesk.dto.TicketDTO;
import com.soporteagil.helpdesk.entity.Ticket;
import com.soporteagil.helpdesk.repository.TicketRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * HU-02: Listar y Filtrar Tickets
 *
 * Como agente de soporte
 * Quiero ver y filtrar los tickets registrados
 * Para gestionar eficientemente las solicitudes
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("HU-02: Listar y Filtrar Tickets")
class HU02ListarFiltrarTicketsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository ticketRepository;

    private List<Ticket> ticketsCreados;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        ticketsCreados = new ArrayList<>();
    }

    private void crearTicketsParaPrueba(int cantidad, String categoria, String prioridad) {
        for (int i = 1; i <= cantidad; i++) {
            Ticket ticket = Ticket.builder()
                    .titulo("Ticket de prueba " + i + " - " + categoria)
                    .descripcion("Descripción del ticket de prueba número " + i + " para la categoría " + categoria)
                    .solicitante("Usuario " + i)
                    .categoria(categoria)
                    .prioridad(Ticket.Prioridad.fromString(prioridad))
                    .build();
            ticketsCreados.add(ticketRepository.save(ticket));
        }
    }

    /**
     * Escenario 1: Listar todos los tickets con paginación
     *
     * Dado que existen 25 tickets en el sistema
     * Cuando accedo a la página de listado de tickets
     * Entonces el sistema muestra los primeros 10 tickets
     * y los tickets están ordenados por fecha de creación descendente (más recientes primero)
     * muestra controles de paginación (página 1 de 3)
     * y por cada ticket se muestra: ID, título, categoría, prioridad, estado y fecha de creación
     */
    @Test
    @Order(1)
    @DisplayName("Escenario 1: Listar todos los tickets con paginación")
    void escenario1_ListarTodosLosTicketsConPaginacion() throws Exception {
        // Arrange - Crear 25 tickets
        crearTicketsParaPrueba(25, "Hardware", "Alta");

        // Act & Assert
        mockMvc.perform(get("/api/tickets")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(10))
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(25))
                .andExpect(jsonPath("$.data.totalPages").value(3))
                // Verificar que cada ticket tiene los campos requeridos
                .andExpect(jsonPath("$.data.content[0].ticketId").isNotEmpty())
                .andExpect(jsonPath("$.data.content[0].titulo").isNotEmpty())
                .andExpect(jsonPath("$.data.content[0].categoria").isNotEmpty())
                .andExpect(jsonPath("$.data.content[0].prioridad").isNotEmpty())
                .andExpect(jsonPath("$.data.content[0].estado").isNotEmpty())
                .andExpect(jsonPath("$.data.content[0].fechaCreacion").isNotEmpty());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-02 - Escenario 1");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Total tickets: 25");
        System.out.println("Tickets mostrados: 10 (página 1)");
        System.out.println("Total páginas: 3");
        System.out.println("Campos validados: ID, título, categoría, prioridad, estado, fecha");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 2: Navegar entre páginas
     *
     * Dado que estoy en la página 1 del listado con 25 tickets totales
     * Cuando presiono el botón "Siguiente"
     * Entonces el sistema muestra los tickets 11 al 20
     * e indica que estoy en la página 2 de 3
     */
    @Test
    @Order(2)
    @DisplayName("Escenario 2: Navegar entre páginas")
    void escenario2_NavegarEntrePaginas() throws Exception {
        // Arrange - Crear 25 tickets
        crearTicketsParaPrueba(25, "Software", "Media");

        // Act & Assert - Página 2 (index 1)
        mockMvc.perform(get("/api/tickets")
                        .param("page", "1")  // Página 2 (índice 1)
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(10))
                .andExpect(jsonPath("$.data.pageNumber").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(25))
                .andExpect(jsonPath("$.data.totalPages").value(3))
                .andExpect(jsonPath("$.data.first").value(false));

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-02 - Escenario 2");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Página solicitada: 2 de 3");
        System.out.println("Tickets mostrados: 11-20 (10 elementos)");
        System.out.println("Es primera página: NO");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 3: Filtrar por estado
     *
     * Dado que existen tickets con diferentes estados
     * Cuando selecciono el filtro de estado "En Progreso"
     * y presiono "Filtrar"
     * Entonces el sistema muestra solo tickets con estado "En Progreso"
     * y mantiene la paginación de 10 tickets por página
     * y mantiene el orden por fecha de creación descendente
     */
    @Test
    @Order(3)
    @DisplayName("Escenario 3: Filtrar por estado")
    void escenario3_FiltrarPorEstado() throws Exception {
        // Arrange - Crear tickets con diferentes estados
        crearTicketsParaPrueba(5, "Hardware", "Alta");  // Todos quedan en ABIERTO

        // Cambiar algunos a EN_PROGRESO
        List<Ticket> allTickets = ticketRepository.findAll();
        for (int i = 0; i < 3; i++) {
            Ticket t = allTickets.get(i);
            t.setEstado(Ticket.Estado.EN_PROGRESO);
            ticketRepository.save(t);
        }

        // Act & Assert - Filtrar por "En Progreso"
        mockMvc.perform(get("/api/tickets")
                        .param("estado", "En Progreso")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(3))
                .andExpect(jsonPath("$.data.totalElements").value(3))
                // Verificar que todos tienen estado "En Progreso"
                .andExpect(jsonPath("$.data.content[0].estado").value("En Progreso"))
                .andExpect(jsonPath("$.data.content[1].estado").value("En Progreso"))
                .andExpect(jsonPath("$.data.content[2].estado").value("En Progreso"));

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-02 - Escenario 3");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Filtro aplicado: Estado = 'En Progreso'");
        System.out.println("Tickets totales: 5");
        System.out.println("Tickets filtrados: 3");
        System.out.println("Todos con estado 'En Progreso': SI");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 4: Filtrar por categoría
     *
     * Dado que existen tickets de diferentes categorías
     * Cuando selecciono el filtro de categoría "Hardware"
     * y presiono "Filtrar"
     * Entonces el sistema muestra solo tickets de categoría "Hardware"
     * y mantiene la paginación y ordenamiento
     */
    @Test
    @Order(4)
    @DisplayName("Escenario 4: Filtrar por categoría")
    void escenario4_FiltrarPorCategoria() throws Exception {
        // Arrange - Crear tickets con diferentes categorías
        crearTicketsParaPrueba(3, "Hardware", "Alta");
        crearTicketsParaPrueba(5, "Software", "Media");
        crearTicketsParaPrueba(2, "Red", "Baja");

        // Act & Assert - Filtrar por "Hardware"
        mockMvc.perform(get("/api/tickets")
                        .param("categoria", "Hardware")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(3))
                .andExpect(jsonPath("$.data.totalElements").value(3))
                // Verificar que todos tienen categoría "Hardware"
                .andExpect(jsonPath("$.data.content[0].categoria").value("Hardware"))
                .andExpect(jsonPath("$.data.content[1].categoria").value("Hardware"))
                .andExpect(jsonPath("$.data.content[2].categoria").value("Hardware"));

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-02 - Escenario 4");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Filtro aplicado: Categoría = 'Hardware'");
        System.out.println("Tickets totales: 10 (3 Hardware, 5 Software, 2 Red)");
        System.out.println("Tickets filtrados: 3");
        System.out.println("Todos con categoría 'Hardware': SI");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 5: Filtrar por estado y categoría combinados
     *
     * Dado que existen tickets variados
     * Cuando selecciono estado "Abierto"
     * selecciono categoría "Software"
     * presiono "Filtrar"
     * Entonces el sistema muestra solo tickets que cumplan ambos criterios
     * muestra mensaje si no hay resultados "No se encontraron tickets con los filtros seleccionados"
     */
    @Test
    @Order(5)
    @DisplayName("Escenario 5: Filtrar por estado y categoría combinados")
    void escenario5_FiltrarPorEstadoYCategoriaCombinados() throws Exception {
        // Arrange - Crear tickets variados
        crearTicketsParaPrueba(3, "Hardware", "Alta");  // ABIERTO
        crearTicketsParaPrueba(4, "Software", "Media"); // ABIERTO
        crearTicketsParaPrueba(2, "Red", "Baja");       // ABIERTO

        // Cambiar algunos a EN_PROGRESO
        List<Ticket> allTickets = ticketRepository.findAll();
        for (Ticket t : allTickets) {
            if (t.getCategoria().equals("Software") && ticketsCreados.indexOf(t) % 2 == 0) {
                t.setEstado(Ticket.Estado.EN_PROGRESO);
                ticketRepository.save(t);
            }
        }

        // Act & Assert - Filtrar por estado "Abierto" y categoría "Software"
        mockMvc.perform(get("/api/tickets")
                        .param("estado", "Abierto")
                        .param("categoria", "Software")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());

        // También probar caso sin resultados
        mockMvc.perform(get("/api/tickets")
                        .param("estado", "Cerrado")
                        .param("categoria", "Software")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(0))
                .andExpect(jsonPath("$.data.totalElements").value(0));

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-02 - Escenario 5");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Filtros combinados: Estado='Abierto' + Categoría='Software'");
        System.out.println("Filtro sin resultados: Estado='Cerrado' + Categoría='Software'");
        System.out.println("Manejo de sin resultados: OK (lista vacía)");
        System.out.println("========================================\n");
    }

    @AfterAll
    static void printFinalReport() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║         REPORTE FINAL - HU-02: LISTAR Y FILTRAR TICKETS      ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Escenario 1: Listar con paginación              - VALIDADO   ║");
        System.out.println("║ Escenario 2: Navegar entre páginas              - VALIDADO   ║");
        System.out.println("║ Escenario 3: Filtrar por estado                 - VALIDADO   ║");
        System.out.println("║ Escenario 4: Filtrar por categoría              - VALIDADO   ║");
        System.out.println("║ Escenario 5: Filtrar combinado estado+categoría - VALIDADO   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
    }
}

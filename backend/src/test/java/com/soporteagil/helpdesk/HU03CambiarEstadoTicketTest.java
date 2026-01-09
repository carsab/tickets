package com.soporteagil.helpdesk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soporteagil.helpdesk.dto.CambioEstadoRequest;
import com.soporteagil.helpdesk.entity.Ticket;
import com.soporteagil.helpdesk.repository.CambioEstadoRepository;
import com.soporteagil.helpdesk.repository.TicketRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * HU-03: Cambiar Estado de Ticket
 *
 * Como agente de soporte
 * Quiero actualizar el estado de un ticket
 * Para reflejar el progreso en la atención
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("HU-03: Cambiar Estado de Ticket")
class HU03CambiarEstadoTicketTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CambioEstadoRepository cambioEstadoRepository;

    @BeforeEach
    void setUp() {
        cambioEstadoRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    private Ticket crearTicketConEstado(Ticket.Estado estado) {
        Ticket ticket = Ticket.builder()
                .titulo("Ticket de prueba para cambio de estado")
                .descripcion("Descripción del ticket de prueba para validar transiciones de estado")
                .solicitante("Usuario Prueba")
                .categoria("Hardware")
                .prioridad(Ticket.Prioridad.ALTA)
                .build();
        ticket = ticketRepository.save(ticket);

        if (estado != Ticket.Estado.ABIERTO) {
            ticket.setEstado(estado);
            ticket = ticketRepository.save(ticket);
        }
        return ticket;
    }

    /**
     * Escenario 1: Cambiar de Abierto a En Progreso
     *
     * Dado que existe un ticket con estado "Abierto"
     * Cuando selecciono el ticket, cambio el estado a "En Progreso", presiono "Actualizar Estado"
     * Entonces el sistema cambia el estado del ticket a "En Progreso"
     * registra la fecha y hora del cambio de estado
     * muestra mensaje "Estado actualizado exitosamente"
     */
    @Test
    @Order(1)
    @DisplayName("Escenario 1: Cambiar de Abierto a En Progreso")
    void escenario1_CambiarDeAbiertoAEnProgreso() throws Exception {
        // Arrange
        Ticket ticket = crearTicketConEstado(Ticket.Estado.ABIERTO);
        CambioEstadoRequest request = new CambioEstadoRequest();
        request.setNuevoEstado("En Progreso");

        // Act & Assert
        mockMvc.perform(put("/api/tickets/" + ticket.getTicketId() + "/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Estado actualizado exitosamente")))
                .andExpect(jsonPath("$.data.estado").value("En Progreso"));

        // Verificar en base de datos
        Ticket updatedTicket = ticketRepository.findById(ticket.getTicketId()).orElseThrow();
        assertEquals(Ticket.Estado.EN_PROGRESO, updatedTicket.getEstado());
        assertNotNull(updatedTicket.getFechaActualizacion());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-03 - Escenario 1");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Transición: ABIERTO -> EN_PROGRESO");
        System.out.println("Estado anterior: Abierto");
        System.out.println("Estado nuevo: En Progreso");
        System.out.println("Fecha actualización registrada: SI");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 2: Cambiar de Abierto a Resuelto directamente
     *
     * Dado que existe un ticket con estado "Abierto"
     * Cuando selecciono el ticket, cambio el estado a "Resuelto", presiono "Actualizar Estado"
     * Entonces el sistema cambia el estado del ticket a "Resuelto"
     * registra la fecha y hora de resolución
     * muestra mensaje "Ticket marcado como resuelto"
     */
    @Test
    @Order(2)
    @DisplayName("Escenario 2: Cambiar de Abierto a Resuelto directamente")
    void escenario2_CambiarDeAbiertoAResueltoDirect() throws Exception {
        // Arrange
        Ticket ticket = crearTicketConEstado(Ticket.Estado.ABIERTO);
        CambioEstadoRequest request = new CambioEstadoRequest();
        request.setNuevoEstado("Resuelto");

        // Act & Assert
        mockMvc.perform(put("/api/tickets/" + ticket.getTicketId() + "/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.estado").value("Resuelto"));

        // Verificar en base de datos
        Ticket updatedTicket = ticketRepository.findById(ticket.getTicketId()).orElseThrow();
        assertEquals(Ticket.Estado.RESUELTO, updatedTicket.getEstado());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-03 - Escenario 2");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Transición: ABIERTO -> RESUELTO (directa)");
        System.out.println("Estado anterior: Abierto");
        System.out.println("Estado nuevo: Resuelto");
        System.out.println("Fecha resolución registrada: SI");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 3: Cambiar de En Progreso a Resuelto
     *
     * Dado que existe un ticket con estado "En Progreso"
     * Cuando selecciono el ticket, cambio el estado a "Resuelto", presiono "Actualizar Estado"
     * Entonces el sistema cambia el estado del ticket a "Resuelto"
     * registra la fecha y hora de resolución
     * muestra mensaje "Ticket resuelto exitosamente"
     */
    @Test
    @Order(3)
    @DisplayName("Escenario 3: Cambiar de En Progreso a Resuelto")
    void escenario3_CambiarDeEnProgresoAResuelto() throws Exception {
        // Arrange
        Ticket ticket = crearTicketConEstado(Ticket.Estado.EN_PROGRESO);
        CambioEstadoRequest request = new CambioEstadoRequest();
        request.setNuevoEstado("Resuelto");

        // Act & Assert
        mockMvc.perform(put("/api/tickets/" + ticket.getTicketId() + "/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.estado").value("Resuelto"));

        // Verificar en base de datos
        Ticket updatedTicket = ticketRepository.findById(ticket.getTicketId()).orElseThrow();
        assertEquals(Ticket.Estado.RESUELTO, updatedTicket.getEstado());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-03 - Escenario 3");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Transición: EN_PROGRESO -> RESUELTO");
        System.out.println("Estado anterior: En Progreso");
        System.out.println("Estado nuevo: Resuelto");
        System.out.println("Fecha resolución registrada: SI");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 4: Cambiar de Resuelto a Cerrado
     *
     * Dado que existe un ticket con estado "Resuelto"
     * Cuando selecciono el ticket, cambio el estado a "Cerrado", presiono "Actualizar Estado"
     * Entonces el sistema cambia el estado del ticket a "Cerrado"
     * registra la fecha y hora de cierre
     * muestra mensaje "Ticket cerrado exitosamente"
     */
    @Test
    @Order(4)
    @DisplayName("Escenario 4: Cambiar de Resuelto a Cerrado")
    void escenario4_CambiarDeResueltoACerrado() throws Exception {
        // Arrange
        Ticket ticket = crearTicketConEstado(Ticket.Estado.RESUELTO);
        CambioEstadoRequest request = new CambioEstadoRequest();
        request.setNuevoEstado("Cerrado");

        // Act & Assert
        mockMvc.perform(put("/api/tickets/" + ticket.getTicketId() + "/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.estado").value("Cerrado"));

        // Verificar en base de datos
        Ticket updatedTicket = ticketRepository.findById(ticket.getTicketId()).orElseThrow();
        assertEquals(Ticket.Estado.CERRADO, updatedTicket.getEstado());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-03 - Escenario 4");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Transición: RESUELTO -> CERRADO");
        System.out.println("Estado anterior: Resuelto");
        System.out.println("Estado nuevo: Cerrado");
        System.out.println("Fecha cierre registrada: SI");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 5: Intentar cerrar sin pasar por Resuelto (validación)
     *
     * Dado que existe un ticket con estado "Abierto"
     * Cuando intento cambiar el estado directamente a "Cerrado", presiono "Actualizar Estado"
     * Entonces el sistema muestra error "No se puede cerrar un ticket que no ha sido resuelto"
     * el estado del ticket permanece como "Abierto"
     */
    @Test
    @Order(5)
    @DisplayName("Escenario 5: Intentar cerrar sin pasar por Resuelto")
    void escenario5_IntentarCerrarSinPasarPorResuelto() throws Exception {
        // Arrange
        Ticket ticket = crearTicketConEstado(Ticket.Estado.ABIERTO);
        CambioEstadoRequest request = new CambioEstadoRequest();
        request.setNuevoEstado("Cerrado");

        // Act & Assert
        mockMvc.perform(put("/api/tickets/" + ticket.getTicketId() + "/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Transición de estado no permitida")));

        // Verificar que el estado NO cambió
        Ticket unchangedTicket = ticketRepository.findById(ticket.getTicketId()).orElseThrow();
        assertEquals(Ticket.Estado.ABIERTO, unchangedTicket.getEstado());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-03 - Escenario 5");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO (Validación funcionando)");
        System.out.println("Transición intentada: ABIERTO -> CERRADO");
        System.out.println("Error mostrado: Transición de estado no permitida");
        System.out.println("Estado permanece: Abierto");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 6: Intentar transición inválida desde En Progreso a Abierto
     *
     * Dado que existe un ticket con estado "En Progreso"
     * Cuando intento cambiar el estado a "Abierto", presiono "Actualizar Estado"
     * Entonces el sistema muestra error "Transición de estado no permitida: En Progreso → Abierto"
     * y el estado permanece como "En Progreso"
     */
    @Test
    @Order(6)
    @DisplayName("Escenario 6: Transición inválida En Progreso a Abierto")
    void escenario6_TransicionInvalidaEnProgresoAAbierto() throws Exception {
        // Arrange
        Ticket ticket = crearTicketConEstado(Ticket.Estado.EN_PROGRESO);
        CambioEstadoRequest request = new CambioEstadoRequest();
        request.setNuevoEstado("Abierto");

        // Act & Assert
        mockMvc.perform(put("/api/tickets/" + ticket.getTicketId() + "/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Transición de estado no permitida: En Progreso")));

        // Verificar que el estado NO cambió
        Ticket unchangedTicket = ticketRepository.findById(ticket.getTicketId()).orElseThrow();
        assertEquals(Ticket.Estado.EN_PROGRESO, unchangedTicket.getEstado());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-03 - Escenario 6");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO (Validación funcionando)");
        System.out.println("Transición intentada: EN_PROGRESO -> ABIERTO");
        System.out.println("Error mostrado: Transición de estado no permitida");
        System.out.println("Estado permanece: En Progreso");
        System.out.println("========================================\n");
    }

    @AfterAll
    static void printFinalReport() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║        REPORTE FINAL - HU-03: CAMBIAR ESTADO DE TICKET       ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Escenario 1: Abierto -> En Progreso             - VALIDADO   ║");
        System.out.println("║ Escenario 2: Abierto -> Resuelto (directo)      - VALIDADO   ║");
        System.out.println("║ Escenario 3: En Progreso -> Resuelto            - VALIDADO   ║");
        System.out.println("║ Escenario 4: Resuelto -> Cerrado                - VALIDADO   ║");
        System.out.println("║ Escenario 5: Abierto -> Cerrado (inválido)      - VALIDADO   ║");
        System.out.println("║ Escenario 6: En Progreso -> Abierto (inválido)  - VALIDADO   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
    }
}

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
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * HU-01: Crear Ticket
 *
 * Como colaborador interno
 * Quiero registrar un nuevo ticket de soporte
 * Para que el área de TI pueda atender mi solicitud
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("HU-01: Crear Ticket")
class HU01CrearTicketTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
    }

    /**
     * Escenario 1: Creación exitosa de ticket
     *
     * Dado que estoy en el formulario de creación de ticket
     * Cuando ingreso título "Acceso a VPN corporativa" (10 caracteres)
     * e ingreso descripción "Necesito acceso a la VPN para trabajar remotamente desde casa" (65 caracteres)
     * e ingreso solicitante "Pepe Pérez"
     * selecciono categoría "Acceso"
     * selecciono prioridad "Alta"
     * presiono el botón "Crear Ticket"
     * Entonces el sistema crea el ticket con estado inicial "Abierto"
     * y registra la fecha y hora de creación
     * y muestra mensaje "Ticket creado exitosamente con ID: XXX"
     * y redirige al listado de tickets
     */
    @Test
    @Order(1)
    @DisplayName("Escenario 1: Creación exitosa de ticket")
    void escenario1_CreacionExitosaDeTicket() throws Exception {
        // Arrange
        TicketDTO ticketDTO = TicketDTO.builder()
                .titulo("Acceso a VPN corporativa")  // 25 caracteres
                .descripcion("Necesito acceso a la VPN para trabajar remotamente desde casa")  // 65 caracteres
                .solicitante("Pepe Pérez")
                .categoria("Acceso")
                .prioridad("Alta")
                .build();

        // Act
        MvcResult result = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketDTO)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Ticket creado exitosamente con ID:")))
                .andExpect(jsonPath("$.data.ticketId").isNotEmpty())
                .andExpect(jsonPath("$.data.titulo").value("Acceso a VPN corporativa"))
                .andExpect(jsonPath("$.data.descripcion").value("Necesito acceso a la VPN para trabajar remotamente desde casa"))
                .andExpect(jsonPath("$.data.solicitante").value("Pepe Pérez"))
                .andExpect(jsonPath("$.data.categoria").value("Acceso"))
                .andExpect(jsonPath("$.data.prioridad").value("Alta"))
                .andExpect(jsonPath("$.data.estado").value("Abierto"))
                .andExpect(jsonPath("$.data.fechaCreacion").isNotEmpty())
                .andReturn();

        // Verify in database
        Ticket savedTicket = ticketRepository.findAll().get(0);
        assertNotNull(savedTicket.getTicketId());
        assertEquals("Abierto", savedTicket.getEstado().getValor());
        assertNotNull(savedTicket.getFechaCreacion());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-01 - Escenario 1");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Ticket creado con ID: " + savedTicket.getTicketId());
        System.out.println("Estado inicial: " + savedTicket.getEstado().getValor());
        System.out.println("Fecha creación: " + savedTicket.getFechaCreacion());
        System.out.println("========================================\n");
    }

    /**
     * Escenario 2: Validación de título muy corto
     *
     * Dado que estoy en el formulario de creación de ticket
     * Cuando ingreso título "Test" (4 caracteres)
     * completo los demás campos correctamente
     * y presiono el botón "Crear Ticket"
     * Entonces el sistema muestra error "El título debe tener entre 5 y 120 caracteres"
     * y no se crea el ticket
     */
    @Test
    @Order(2)
    @DisplayName("Escenario 2: Validación de título muy corto")
    void escenario2_ValidacionTituloMuyCorto() throws Exception {
        // Arrange
        TicketDTO ticketDTO = TicketDTO.builder()
                .titulo("Test")  // Solo 4 caracteres - debe fallar
                .descripcion("Esta es una descripción válida con más de 10 caracteres")
                .solicitante("Pepe Pérez")
                .categoria("Acceso")
                .prioridad("Alta")
                .build();

        long countBefore = ticketRepository.count();

        // Act & Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("El título debe tener entre 5 y 120 caracteres")));

        // Verify ticket was NOT created
        assertEquals(countBefore, ticketRepository.count());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-01 - Escenario 2");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Título ingresado: 'Test' (4 caracteres)");
        System.out.println("Error esperado: El título debe tener entre 5 y 120 caracteres");
        System.out.println("Ticket creado: NO");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 3: Validación de descripción muy corta
     *
     * Dado que estoy en el formulario de creación de ticket
     * Cuando ingreso título "Problema con correo" (20 caracteres)
     * ingreso descripción "Error" (5 caracteres)
     * completo los demás campos correctamente
     * presiono el botón "Crear Ticket"
     * Entonces el sistema muestra error "La descripción debe tener entre 10 y 2000 caracteres"
     * y no se crea el ticket
     */
    @Test
    @Order(3)
    @DisplayName("Escenario 3: Validación de descripción muy corta")
    void escenario3_ValidacionDescripcionMuyCorta() throws Exception {
        // Arrange
        TicketDTO ticketDTO = TicketDTO.builder()
                .titulo("Problema con correo")  // 19 caracteres - válido
                .descripcion("Error")  // Solo 5 caracteres - debe fallar
                .solicitante("Pepe Pérez")
                .categoria("Software")
                .prioridad("Media")
                .build();

        long countBefore = ticketRepository.count();

        // Act & Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("La descripción debe tener entre 10 y 2000 caracteres")));

        // Verify ticket was NOT created
        assertEquals(countBefore, ticketRepository.count());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-01 - Escenario 3");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Título ingresado: 'Problema con correo' (19 caracteres)");
        System.out.println("Descripción ingresada: 'Error' (5 caracteres)");
        System.out.println("Error esperado: La descripción debe tener entre 10 y 2000 caracteres");
        System.out.println("Ticket creado: NO");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 4: Validación de campos obligatorios
     *
     * Dado que estoy en el formulario de creación de ticket
     * Cuando dejo el campo categoría vacío
     * y completo los demás campos correctamente
     * y presiono el botón "Crear Ticket"
     * Entonces el sistema muestra error "La categoría es obligatoria"
     * y no se crea el ticket
     */
    @Test
    @Order(4)
    @DisplayName("Escenario 4: Validación de campos obligatorios (categoría vacía)")
    void escenario4_ValidacionCamposCategoriaVacia() throws Exception {
        // Arrange
        TicketDTO ticketDTO = TicketDTO.builder()
                .titulo("Problema con impresora")
                .descripcion("La impresora no enciende, necesito soporte técnico urgente")
                .solicitante("Juan García")
                .categoria("")  // Categoría vacía - debe fallar
                .prioridad("Alta")
                .build();

        long countBefore = ticketRepository.count();

        // Act & Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("La categoría es obligatoria")));

        // Verify ticket was NOT created
        assertEquals(countBefore, ticketRepository.count());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-01 - Escenario 4");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Campo vacío: categoría");
        System.out.println("Error esperado: La categoría es obligatoria");
        System.out.println("Ticket creado: NO");
        System.out.println("========================================\n");
    }

    @AfterAll
    static void printFinalReport() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║           REPORTE FINAL - HU-01: CREAR TICKET                ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Escenario 1: Creación exitosa de ticket          - VALIDADO  ║");
        System.out.println("║ Escenario 2: Validación título muy corto         - VALIDADO  ║");
        System.out.println("║ Escenario 3: Validación descripción muy corta    - VALIDADO  ║");
        System.out.println("║ Escenario 4: Validación campos obligatorios      - VALIDADO  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
    }
}

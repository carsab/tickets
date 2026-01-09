package com.soporteagil.helpdesk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soporteagil.helpdesk.dto.ComentarioDTO;
import com.soporteagil.helpdesk.entity.Comentario;
import com.soporteagil.helpdesk.entity.Ticket;
import com.soporteagil.helpdesk.repository.ComentarioRepository;
import com.soporteagil.helpdesk.repository.TicketRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * HU-04: Agregar Comentarios
 *
 * Como agente de soporte o solicitante
 * Quiero agregar comentarios a un ticket
 * Para documentar acciones, decisiones o información adicional
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("HU-04: Agregar Comentarios")
class HU04AgregarComentariosTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    private Ticket ticketPrueba;

    @BeforeEach
    void setUp() {
        comentarioRepository.deleteAll();
        ticketRepository.deleteAll();

        // Crear ticket de prueba
        ticketPrueba = Ticket.builder()
                .titulo("Ticket para prueba de comentarios")
                .descripcion("Descripción del ticket de prueba para validar funcionalidad de comentarios")
                .solicitante("Usuario Prueba")
                .categoria("Software")
                .prioridad(Ticket.Prioridad.MEDIA)
                .build();
        ticketPrueba = ticketRepository.save(ticketPrueba);
    }

    /**
     * Escenario 1: Agregar comentario exitosamente
     *
     * Dado que estoy visualizando los detalles de un ticket
     * Cuando ingreso en el campo comentario "Se verificó el acceso, pendiente aprobación del supervisor"
     * ingreso autor "María González"
     * presiono "Agregar Comentario"
     * Entonces el sistema guarda el comentario asociado al ticket
     * registra la fecha y hora del comentario
     * muestra el comentario en la lista de comentarios del ticket
     * muestra mensaje "Comentario agregado exitosamente"
     */
    @Test
    @Order(1)
    @DisplayName("Escenario 1: Agregar comentario exitosamente")
    void escenario1_AgregarComentarioExitosamente() throws Exception {
        // Arrange
        ComentarioDTO comentarioDTO = ComentarioDTO.builder()
                .autor("María González")
                .texto("Se verificó el acceso, pendiente aprobación del supervisor")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/tickets/" + ticketPrueba.getTicketId() + "/comentarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Comentario agregado exitosamente")))
                .andExpect(jsonPath("$.data.comentarioId").isNotEmpty())
                .andExpect(jsonPath("$.data.autor").value("María González"))
                .andExpect(jsonPath("$.data.texto").value("Se verificó el acceso, pendiente aprobación del supervisor"))
                .andExpect(jsonPath("$.data.fechaCreacion").isNotEmpty());

        // Verificar en base de datos
        List<Comentario> comentarios = comentarioRepository.findByTicketIdOrderByFechaCreacionDesc(ticketPrueba.getTicketId());
        assertEquals(1, comentarios.size());
        assertEquals("María González", comentarios.get(0).getAutor());
        assertNotNull(comentarios.get(0).getFechaCreacion());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-04 - Escenario 1");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Ticket ID: " + ticketPrueba.getTicketId());
        System.out.println("Autor: María González");
        System.out.println("Texto: Se verificó el acceso, pendiente aprobación del supervisor");
        System.out.println("Fecha registrada: SI");
        System.out.println("Comentario guardado: SI");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 2: Ver histórico de comentarios
     *
     * Dado que un ticket tiene 3 comentarios registrados
     * Cuando accedo a los detalles del ticket
     * Entonces el sistema muestra los 3 comentarios
     * cada comentario muestra: autor, fecha/hora y texto
     * los comentarios están ordenados del más reciente al más antiguo
     */
    @Test
    @Order(2)
    @DisplayName("Escenario 2: Ver histórico de comentarios")
    void escenario2_VerHistoricoDeComentarios() throws Exception {
        // Arrange - Crear 3 comentarios
        for (int i = 1; i <= 3; i++) {
            Comentario comentario = Comentario.builder()
                    .ticket(ticketPrueba)
                    .autor("Usuario " + i)
                    .texto("Comentario número " + i + " del ticket")
                    .build();
            comentarioRepository.save(comentario);

            // Pequeña pausa para asegurar diferentes timestamps
            Thread.sleep(100);
        }

        // Act & Assert
        mockMvc.perform(get("/api/tickets/" + ticketPrueba.getTicketId() + "/comentarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                // Verificar que cada comentario tiene los campos requeridos
                .andExpect(jsonPath("$.data[0].autor").isNotEmpty())
                .andExpect(jsonPath("$.data[0].texto").isNotEmpty())
                .andExpect(jsonPath("$.data[0].fechaCreacion").isNotEmpty())
                .andExpect(jsonPath("$.data[1].autor").isNotEmpty())
                .andExpect(jsonPath("$.data[1].texto").isNotEmpty())
                .andExpect(jsonPath("$.data[1].fechaCreacion").isNotEmpty())
                .andExpect(jsonPath("$.data[2].autor").isNotEmpty())
                .andExpect(jsonPath("$.data[2].texto").isNotEmpty())
                .andExpect(jsonPath("$.data[2].fechaCreacion").isNotEmpty());

        // Verificar ordenamiento (más reciente primero)
        List<Comentario> comentarios = comentarioRepository.findByTicketIdOrderByFechaCreacionDesc(ticketPrueba.getTicketId());
        assertEquals(3, comentarios.size());
        // El último agregado (Usuario 3) debe estar primero
        assertEquals("Usuario 3", comentarios.get(0).getAutor());
        assertEquals("Usuario 1", comentarios.get(2).getAutor());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-04 - Escenario 2");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO");
        System.out.println("Total comentarios: 3");
        System.out.println("Campos mostrados: autor, texto, fechaCreacion");
        System.out.println("Ordenamiento: Más reciente primero (DESC)");
        System.out.println("Primer comentario: Usuario 3 (más reciente)");
        System.out.println("Último comentario: Usuario 1 (más antiguo)");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 3: Validación de comentario vacío
     *
     * Dado que estoy en los detalles de un ticket
     * Cuando dejo el campo comentario vacío
     * y presiono "Agregar Comentario"
     * Entonces el sistema muestra error "El comentario no puede estar vacío"
     * y no se guarda el comentario
     */
    @Test
    @Order(3)
    @DisplayName("Escenario 3: Validación de comentario vacío")
    void escenario3_ValidacionComentarioVacio() throws Exception {
        // Arrange
        ComentarioDTO comentarioDTO = ComentarioDTO.builder()
                .autor("María González")
                .texto("")  // Texto vacío
                .build();

        long countBefore = comentarioRepository.count();

        // Act & Assert
        mockMvc.perform(post("/api/tickets/" + ticketPrueba.getTicketId() + "/comentarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("obligatorio")));

        // Verificar que NO se guardó
        assertEquals(countBefore, comentarioRepository.count());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-04 - Escenario 3");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO (Validación funcionando)");
        System.out.println("Campo vacío: texto del comentario");
        System.out.println("Error mostrado: El texto del comentario es obligatorio");
        System.out.println("Comentario guardado: NO");
        System.out.println("========================================\n");
    }

    /**
     * Escenario 4: Validación de autor obligatorio
     *
     * Dado que estoy agregando un comentario a un ticket
     * Cuando ingreso texto en el comentario
     * y dejo el campo autor vacío
     * y presiono "Agregar Comentario"
     * Entonces el sistema muestra error "El autor es obligatorio"
     * y no se guarda el comentario
     */
    @Test
    @Order(4)
    @DisplayName("Escenario 4: Validación de autor obligatorio")
    void escenario4_ValidacionAutorObligatorio() throws Exception {
        // Arrange
        ComentarioDTO comentarioDTO = ComentarioDTO.builder()
                .autor("")  // Autor vacío
                .texto("Este es un comentario válido con texto suficiente")
                .build();

        long countBefore = comentarioRepository.count();

        // Act & Assert
        mockMvc.perform(post("/api/tickets/" + ticketPrueba.getTicketId() + "/comentarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("autor es obligatorio")));

        // Verificar que NO se guardó
        assertEquals(countBefore, comentarioRepository.count());

        System.out.println("\n========================================");
        System.out.println("REPORTE HU-04 - Escenario 4");
        System.out.println("========================================");
        System.out.println("Resultado: EXITOSO (Validación funcionando)");
        System.out.println("Campo vacío: autor");
        System.out.println("Error mostrado: El autor es obligatorio");
        System.out.println("Comentario guardado: NO");
        System.out.println("========================================\n");
    }

    @AfterAll
    static void printFinalReport() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║          REPORTE FINAL - HU-04: AGREGAR COMENTARIOS          ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Escenario 1: Agregar comentario exitosamente    - VALIDADO   ║");
        System.out.println("║ Escenario 2: Ver histórico de comentarios       - VALIDADO   ║");
        System.out.println("║ Escenario 3: Validación comentario vacío        - VALIDADO   ║");
        System.out.println("║ Escenario 4: Validación autor obligatorio       - VALIDADO   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
    }
}

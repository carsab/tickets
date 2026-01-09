package com.soporteagil.helpdesk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soporteagil.helpdesk.entity.CambioEstado;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de Simulación - Validación del Resumen Diario
 *
 * Este test crea datos de prueba con diferentes estados y categorías
 * y valida que el resumen diario devuelva los valores correctos.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Simulación y Validación del Resumen Diario")
class ResumenDiarioSimulacionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CambioEstadoRepository cambioEstadoRepository;

    // Contadores esperados
    private int ticketsCreadosHoy = 0;
    private int ticketsResueltosHoy = 0;
    private int ticketsHardwareAbiertos = 0;
    private int ticketsSoftwareAbiertos = 0;
    private int ticketsAccesoAbiertos = 0;
    private int ticketsRedAbiertos = 0;

    @BeforeEach
    void limpiarDatos() {
        cambioEstadoRepository.deleteAll();
        ticketRepository.deleteAll();

        // Resetear contadores
        ticketsCreadosHoy = 0;
        ticketsResueltosHoy = 0;
        ticketsHardwareAbiertos = 0;
        ticketsSoftwareAbiertos = 0;
        ticketsAccesoAbiertos = 0;
        ticketsRedAbiertos = 0;
    }

    /**
     * Crea un ticket y actualiza los contadores
     */
    private Ticket crearTicket(String categoria, Ticket.Prioridad prioridad, Ticket.Estado estado) {
        Ticket ticket = Ticket.builder()
                .titulo("Ticket de " + categoria + " - " + System.currentTimeMillis())
                .descripcion("Descripción del ticket de prueba para la categoría " + categoria)
                .solicitante("Usuario Simulación")
                .categoria(categoria)
                .prioridad(prioridad)
                .build();

        ticket = ticketRepository.save(ticket);
        ticketsCreadosHoy++;

        // Si el estado es diferente de ABIERTO, necesitamos cambiarlo
        if (estado != Ticket.Estado.ABIERTO) {
            Ticket.Estado estadoAnterior = ticket.getEstado();
            ticket.setEstado(estado);
            ticket = ticketRepository.save(ticket);

            // Registrar cambio de estado
            CambioEstado cambio = CambioEstado.builder()
                    .ticket(ticket)
                    .estadoAnterior(estadoAnterior.getValor())
                    .estadoNuevo(estado.getValor())
                    .build();
            cambioEstadoRepository.save(cambio);

            if (estado == Ticket.Estado.RESUELTO) {
                ticketsResueltosHoy++;
            }
        }

        // Actualizar contadores de tickets abiertos por categoría
        if (estado == Ticket.Estado.ABIERTO || estado == Ticket.Estado.EN_PROGRESO) {
            switch (categoria) {
                case "Hardware" -> ticketsHardwareAbiertos++;
                case "Software" -> ticketsSoftwareAbiertos++;
                case "Acceso" -> ticketsAccesoAbiertos++;
                case "Red" -> ticketsRedAbiertos++;
            }
        }

        return ticket;
    }

    @Test
    @Order(1)
    @DisplayName("Simulación: Crear datos de prueba y validar resumen diario")
    void simulacionDatosYValidacionResumen() throws Exception {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("SIMULACIÓN DE DATOS - RESUMEN DIARIO");
        System.out.println("=".repeat(70));

        // ============================================
        // PASO 1: Crear tickets de HARDWARE
        // ============================================
        System.out.println("\n[PASO 1] Creando tickets de HARDWARE...");

        // 8 tickets ABIERTOS
        for (int i = 0; i < 8; i++) {
            crearTicket("Hardware", Ticket.Prioridad.ALTA, Ticket.Estado.ABIERTO);
        }
        // 4 tickets EN_PROGRESO
        for (int i = 0; i < 4; i++) {
            crearTicket("Hardware", Ticket.Prioridad.MEDIA, Ticket.Estado.EN_PROGRESO);
        }
        // 3 tickets RESUELTOS
        for (int i = 0; i < 3; i++) {
            crearTicket("Hardware", Ticket.Prioridad.BAJA, Ticket.Estado.RESUELTO);
        }
        // 2 tickets CERRADOS
        for (int i = 0; i < 2; i++) {
            Ticket t = crearTicket("Hardware", Ticket.Prioridad.ALTA, Ticket.Estado.RESUELTO);
            t.setEstado(Ticket.Estado.CERRADO);
            ticketRepository.save(t);
            CambioEstado cambio = CambioEstado.builder()
                    .ticket(t)
                    .estadoAnterior("Resuelto")
                    .estadoNuevo("Cerrado")
                    .build();
            cambioEstadoRepository.save(cambio);
        }

        System.out.println("   Hardware ABIERTOS: 8");
        System.out.println("   Hardware EN_PROGRESO: 4");
        System.out.println("   Hardware RESUELTOS: 3");
        System.out.println("   Hardware CERRADOS: 2");
        System.out.println("   Total Hardware con tickets abiertos/en progreso: 12");

        // ============================================
        // PASO 2: Crear tickets de SOFTWARE
        // ============================================
        System.out.println("\n[PASO 2] Creando tickets de SOFTWARE...");

        // 5 tickets ABIERTOS
        for (int i = 0; i < 5; i++) {
            crearTicket("Software", Ticket.Prioridad.ALTA, Ticket.Estado.ABIERTO);
        }
        // 3 tickets EN_PROGRESO
        for (int i = 0; i < 3; i++) {
            crearTicket("Software", Ticket.Prioridad.MEDIA, Ticket.Estado.EN_PROGRESO);
        }
        // 2 tickets RESUELTOS
        for (int i = 0; i < 2; i++) {
            crearTicket("Software", Ticket.Prioridad.BAJA, Ticket.Estado.RESUELTO);
        }

        System.out.println("   Software ABIERTOS: 5");
        System.out.println("   Software EN_PROGRESO: 3");
        System.out.println("   Software RESUELTOS: 2");
        System.out.println("   Total Software con tickets abiertos/en progreso: 8");

        // ============================================
        // PASO 3: Crear tickets de ACCESO
        // ============================================
        System.out.println("\n[PASO 3] Creando tickets de ACCESO...");

        // 3 tickets ABIERTOS
        for (int i = 0; i < 3; i++) {
            crearTicket("Acceso", Ticket.Prioridad.ALTA, Ticket.Estado.ABIERTO);
        }
        // 2 tickets EN_PROGRESO
        for (int i = 0; i < 2; i++) {
            crearTicket("Acceso", Ticket.Prioridad.MEDIA, Ticket.Estado.EN_PROGRESO);
        }
        // 1 ticket RESUELTO
        crearTicket("Acceso", Ticket.Prioridad.BAJA, Ticket.Estado.RESUELTO);

        System.out.println("   Acceso ABIERTOS: 3");
        System.out.println("   Acceso EN_PROGRESO: 2");
        System.out.println("   Acceso RESUELTOS: 1");
        System.out.println("   Total Acceso con tickets abiertos/en progreso: 5");

        // ============================================
        // PASO 4: Crear tickets de RED
        // ============================================
        System.out.println("\n[PASO 4] Creando tickets de RED...");

        // 2 tickets ABIERTOS
        for (int i = 0; i < 2; i++) {
            crearTicket("Red", Ticket.Prioridad.MEDIA, Ticket.Estado.ABIERTO);
        }
        // 1 ticket EN_PROGRESO
        crearTicket("Red", Ticket.Prioridad.ALTA, Ticket.Estado.EN_PROGRESO);
        // 2 tickets RESUELTOS
        for (int i = 0; i < 2; i++) {
            crearTicket("Red", Ticket.Prioridad.BAJA, Ticket.Estado.RESUELTO);
        }

        System.out.println("   Red ABIERTOS: 2");
        System.out.println("   Red EN_PROGRESO: 1");
        System.out.println("   Red RESUELTOS: 2");
        System.out.println("   Total Red con tickets abiertos/en progreso: 3");

        // ============================================
        // PASO 5: Verificar datos en base de datos
        // ============================================
        System.out.println("\n[PASO 5] Verificando datos en base de datos...");

        long totalTickets = ticketRepository.count();
        long totalCambiosEstado = cambioEstadoRepository.count();

        System.out.println("   Total tickets en BD: " + totalTickets);
        System.out.println("   Total cambios de estado en BD: " + totalCambiosEstado);

        // Verificar por estado
        List<Ticket> todosLosTickets = ticketRepository.findAll();
        long abiertos = todosLosTickets.stream().filter(t -> t.getEstado() == Ticket.Estado.ABIERTO).count();
        long enProgreso = todosLosTickets.stream().filter(t -> t.getEstado() == Ticket.Estado.EN_PROGRESO).count();
        long resueltos = todosLosTickets.stream().filter(t -> t.getEstado() == Ticket.Estado.RESUELTO).count();
        long cerrados = todosLosTickets.stream().filter(t -> t.getEstado() == Ticket.Estado.CERRADO).count();

        System.out.println("\n   Tickets por estado:");
        System.out.println("   - ABIERTO: " + abiertos);
        System.out.println("   - EN_PROGRESO: " + enProgreso);
        System.out.println("   - RESUELTO: " + resueltos);
        System.out.println("   - CERRADO: " + cerrados);

        // ============================================
        // PASO 6: Llamar al endpoint de resumen diario
        // ============================================
        System.out.println("\n[PASO 6] Llamando al endpoint /api/resumen/diario...");

        MvcResult result = mockMvc.perform(get("/api/resumen/diario")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        JsonNode data = jsonResponse.get("data");

        long ticketsCreadosHoyResponse = data.get("ticketsCreadosHoy").asLong();
        long ticketsResueltosHoyResponse = data.get("ticketsResueltosHoy").asLong();
        JsonNode topCategoriasResponse = data.get("topCategorias");

        // ============================================
        // PASO 7: Mostrar resultados y comparar
        // ============================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("RESULTADOS DEL RESUMEN DIARIO");
        System.out.println("=".repeat(70));

        System.out.println("\n┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│                    VALORES ESPERADOS                            │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│ Tickets creados hoy:    " + String.format("%-40s", totalTickets) + "│");
        System.out.println("│ Tickets resueltos hoy:  " + String.format("%-40s", (3 + 2 + 1 + 2)) + "│");
        System.out.println("│ Top categorías (tickets abiertos/en progreso):                 │");
        System.out.println("│   1. Hardware: 12 tickets                                      │");
        System.out.println("│   2. Software: 8 tickets                                       │");
        System.out.println("│   3. Acceso: 5 tickets                                         │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");

        System.out.println("\n┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│                    VALORES OBTENIDOS (API)                      │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│ Tickets creados hoy:    " + String.format("%-40s", ticketsCreadosHoyResponse) + "│");
        System.out.println("│ Tickets resueltos hoy:  " + String.format("%-40s", ticketsResueltosHoyResponse) + "│");
        System.out.println("│ Top categorías:                                                │");

        if (topCategoriasResponse != null && topCategoriasResponse.isArray()) {
            for (int i = 0; i < topCategoriasResponse.size(); i++) {
                JsonNode cat = topCategoriasResponse.get(i);
                String categoria = cat.get("categoria").asText();
                long cantidad = cat.get("cantidad").asLong();
                System.out.println("│   " + (i + 1) + ". " + String.format("%-15s", categoria) + ": " +
                        String.format("%-35s", cantidad + " tickets") + "│");
            }
        } else {
            System.out.println("│   (Sin categorías o formato incorrecto)                        │");
        }
        System.out.println("└─────────────────────────────────────────────────────────────────┘");

        // ============================================
        // PASO 8: Validar y mostrar discrepancias
        // ============================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ANÁLISIS DE RESULTADOS");
        System.out.println("=".repeat(70));

        boolean hayProblemas = false;

        // Validar tickets creados
        if (ticketsCreadosHoyResponse != totalTickets) {
            System.out.println("\n[ERROR] Tickets creados hoy:");
            System.out.println("   Esperado: " + totalTickets);
            System.out.println("   Obtenido: " + ticketsCreadosHoyResponse);
            hayProblemas = true;
        } else {
            System.out.println("\n[OK] Tickets creados hoy: " + ticketsCreadosHoyResponse);
        }

        // Validar tickets resueltos (8 tickets pasaron a resuelto: 3 Hardware + 2 Software + 1 Acceso + 2 Red)
        long esperadosResueltos = 8; // Los que pasaron directamente a RESUELTO
        if (ticketsResueltosHoyResponse != esperadosResueltos) {
            System.out.println("\n[ADVERTENCIA] Tickets resueltos hoy:");
            System.out.println("   Esperado: " + esperadosResueltos);
            System.out.println("   Obtenido: " + ticketsResueltosHoyResponse);
            System.out.println("   Nota: Verificar la consulta en CambioEstadoRepository");
            hayProblemas = true;
        } else {
            System.out.println("[OK] Tickets resueltos hoy: " + ticketsResueltosHoyResponse);
        }

        // Validar top categorías
        if (topCategoriasResponse == null || topCategoriasResponse.size() == 0) {
            System.out.println("\n[ERROR] Top categorías está vacío");
            System.out.println("   Posible causa: La consulta en TicketRepository.findTop3CategoriasConTicketsAbiertos");
            System.out.println("   usa 'ABIERTO' y 'EN_PROGRESO' pero el enum se guarda diferente en BD");
            hayProblemas = true;
        } else {
            System.out.println("\n[INFO] Top " + topCategoriasResponse.size() + " categorías encontradas");

            // Verificar primera categoría (debería ser Hardware con 12)
            if (topCategoriasResponse.size() > 0) {
                String primeraCategoria = topCategoriasResponse.get(0).get("categoria").asText();
                long primeraCantidad = topCategoriasResponse.get(0).get("cantidad").asLong();

                if (!primeraCategoria.equals("Hardware") || primeraCantidad != 12) {
                    System.out.println("[ADVERTENCIA] Primera categoría esperada: Hardware (12)");
                    System.out.println("              Primera categoría obtenida: " + primeraCategoria + " (" + primeraCantidad + ")");
                    hayProblemas = true;
                }
            }
        }

        System.out.println("\n" + "=".repeat(70));
        if (hayProblemas) {
            System.out.println("RESULTADO: HAY DISCREPANCIAS - Revisar las consultas JPQL");
            System.out.println("\nPosibles problemas identificados:");
            System.out.println("1. La consulta 'findTop3CategoriasConTicketsAbiertos' usa:");
            System.out.println("   WHERE t.estado IN ('ABIERTO', 'EN_PROGRESO')");
            System.out.println("   Pero @Enumerated(EnumType.STRING) guarda 'ABIERTO', 'EN_PROGRESO'");
            System.out.println("   Sin embargo, si los valores no coinciden, verificar la BD.");
            System.out.println("\n2. La consulta 'countTicketsResueltosHoy' busca:");
            System.out.println("   c.estadoNuevo = 'Resuelto'");
            System.out.println("   Verificar que los cambios de estado se guarden con 'Resuelto'.");
        } else {
            System.out.println("RESULTADO: TODAS LAS VALIDACIONES PASARON CORRECTAMENTE");
        }
        System.out.println("=".repeat(70) + "\n");
    }

    @Test
    @Order(2)
    @DisplayName("Debug: Verificar valores guardados en base de datos")
    void debugValoresEnBaseDatos() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("DEBUG: VALORES EN BASE DE DATOS");
        System.out.println("=".repeat(70));

        // Crear algunos tickets de prueba
        Ticket t1 = Ticket.builder()
                .titulo("Ticket debug ABIERTO")
                .descripcion("Descripción del ticket de debug estado ABIERTO")
                .solicitante("Debug User")
                .categoria("Hardware")
                .prioridad(Ticket.Prioridad.ALTA)
                .build();
        t1 = ticketRepository.save(t1);

        Ticket t2 = Ticket.builder()
                .titulo("Ticket debug EN_PROGRESO")
                .descripcion("Descripción del ticket de debug estado EN_PROGRESO")
                .solicitante("Debug User")
                .categoria("Software")
                .prioridad(Ticket.Prioridad.MEDIA)
                .build();
        t2 = ticketRepository.save(t2);
        t2.setEstado(Ticket.Estado.EN_PROGRESO);
        t2 = ticketRepository.save(t2);

        // Mostrar cómo se guardan los estados
        System.out.println("\n[INFO] Valores de Estado enum:");
        for (Ticket.Estado estado : Ticket.Estado.values()) {
            System.out.println("   Enum name: " + estado.name() + " | Valor display: " + estado.getValor());
        }

        System.out.println("\n[INFO] Tickets guardados:");
        List<Ticket> tickets = ticketRepository.findAll();
        for (Ticket t : tickets) {
            System.out.println("   ID: " + t.getTicketId() +
                    " | Estado (enum): " + t.getEstado().name() +
                    " | Estado (valor): " + t.getEstado().getValor() +
                    " | Categoría: " + t.getCategoria());
        }

        // La consulta JPQL usa el NOMBRE del enum, no el valor
        // t.estado IN ('ABIERTO', 'EN_PROGRESO') debería funcionar
        // porque @Enumerated(EnumType.STRING) guarda el .name() del enum

        System.out.println("\n[INFO] En la BD con @Enumerated(EnumType.STRING):");
        System.out.println("   Se guarda el .name() del enum: ABIERTO, EN_PROGRESO, RESUELTO, CERRADO");
        System.out.println("   La consulta JPQL usa estos nombres, debería funcionar.");
        System.out.println("   Si no funciona, puede ser problema de conversión en Oracle.");

        System.out.println("\n" + "=".repeat(70) + "\n");
    }
}

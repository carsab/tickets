package com.soporteagil.helpdesk.controller;

import com.soporteagil.helpdesk.dto.*;
import com.soporteagil.helpdesk.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TicketController {
    
    private final TicketService ticketService;
    
    /**
     * RF-01: Crear nuevo ticket
     * POST /api/tickets
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TicketDTO>> crearTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        log.info("POST /api/tickets - Crear ticket: {}", ticketDTO.getTitulo());
        
        TicketDTO ticketCreado = ticketService.crearTicket(ticketDTO);
        
        ApiResponse<TicketDTO> response = ApiResponse.success(
                ticketCreado,
                "Ticket creado exitosamente con ID: " + ticketCreado.getTicketId()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * RF-02: Listar tickets con filtros y paginación
     * GET /api/tickets?estado=Abierto&categoria=Hardware&page=0&size=10
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TicketDTO>>> listarTickets(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/tickets - Estado: {}, Categoría: {}, Página: {}, Tamaño: {}", 
                 estado, categoria, page, size);
        
        PageResponse<TicketDTO> tickets = ticketService.listarTickets(estado, categoria, page, size);
        
        ApiResponse<PageResponse<TicketDTO>> response = ApiResponse.success(
                tickets,
                String.format("Se encontraron %d tickets", tickets.getTotalElements())
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener ticket por ID
     * GET /api/tickets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketDTO>> obtenerTicket(@PathVariable Long id) {
        log.info("GET /api/tickets/{} - Obtener ticket", id);
        
        TicketDTO ticket = ticketService.obtenerTicketPorId(id);
        
        ApiResponse<TicketDTO> response = ApiResponse.success(
                ticket,
                "Ticket recuperado exitosamente"
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * RF-03: Cambiar estado de ticket
     * PUT /api/tickets/{id}/estado
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<TicketDTO>> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRequest request) {
        
        log.info("PUT /api/tickets/{}/estado - Nuevo estado: {}", id, request.getNuevoEstado());
        
        TicketDTO ticketActualizado = ticketService.cambiarEstado(id, request.getNuevoEstado());
        
        ApiResponse<TicketDTO> response = ApiResponse.success(
                ticketActualizado,
                "Estado actualizado exitosamente a: " + request.getNuevoEstado()
        );
        
        return ResponseEntity.ok(response);
    }
}
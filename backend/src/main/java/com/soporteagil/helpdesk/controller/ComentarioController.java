package com.soporteagil.helpdesk.controller;

import com.soporteagil.helpdesk.dto.ApiResponse;
import com.soporteagil.helpdesk.dto.ComentarioDTO;
import com.soporteagil.helpdesk.service.ComentarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets/{ticketId}/comentarios")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ComentarioController {
    
    private final ComentarioService comentarioService;
    
    /**
     * RF-04: Agregar comentario a un ticket
     * POST /api/tickets/{ticketId}/comentarios
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ComentarioDTO>> agregarComentario(
            @PathVariable Long ticketId,
            @Valid @RequestBody ComentarioDTO comentarioDTO) {
        
        log.info("POST /api/tickets/{}/comentarios - Agregar comentario", ticketId);
        
        ComentarioDTO comentarioCreado = comentarioService.agregarComentario(ticketId, comentarioDTO);
        
        ApiResponse<ComentarioDTO> response = ApiResponse.success(
                comentarioCreado,
                "Comentario agregado exitosamente"
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Listar comentarios de un ticket
     * GET /api/tickets/{ticketId}/comentarios
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ComentarioDTO>>> listarComentarios(
            @PathVariable Long ticketId) {
        
        log.info("GET /api/tickets/{}/comentarios - Listar comentarios", ticketId);
        
        List<ComentarioDTO> comentarios = comentarioService.listarComentariosPorTicket(ticketId);
        
        ApiResponse<List<ComentarioDTO>> response = ApiResponse.success(
                comentarios,
                String.format("Se encontraron %d comentarios", comentarios.size())
        );
        
        return ResponseEntity.ok(response);
    }
}

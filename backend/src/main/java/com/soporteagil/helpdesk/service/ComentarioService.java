package com.soporteagil.helpdesk.service;

import com.soporteagil.helpdesk.dto.ComentarioDTO;
import com.soporteagil.helpdesk.entity.Comentario;
import com.soporteagil.helpdesk.entity.Ticket;
import com.soporteagil.helpdesk.exception.BusinessException;
import com.soporteagil.helpdesk.repository.ComentarioRepository;
import com.soporteagil.helpdesk.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComentarioService {
    
    private final ComentarioRepository comentarioRepository;
    private final TicketRepository ticketRepository;
    
    /**
     * RF-04: Agregar comentario a un ticket
     */
    @Transactional
    public ComentarioDTO agregarComentario(Long ticketId, ComentarioDTO comentarioDTO) {
        log.info("Agregando comentario al ticket {}", ticketId);
        
        // Validar que el ticket existe
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new BusinessException("Ticket no encontrado con ID: " + ticketId));
        
        // Crear comentario
        Comentario comentario = Comentario.builder()
                .ticket(ticket)
                .autor(comentarioDTO.getAutor())
                .texto(comentarioDTO.getTexto())
                .build();
        
        comentario = comentarioRepository.save(comentario);
        log.info("Comentario agregado exitosamente con ID: {}", comentario.getComentarioId());
        
        return convertirADTO(comentario);
    }
    
    /**
     * Listar comentarios de un ticket
     */
    @Transactional(readOnly = true)
    public List<ComentarioDTO> listarComentariosPorTicket(Long ticketId) {
        log.info("Listando comentarios del ticket {}", ticketId);
        
        // Validar que el ticket existe
        if (!ticketRepository.existsById(ticketId)) {
            throw new BusinessException("Ticket no encontrado con ID: " + ticketId);
        }
        
        List<Comentario> comentarios = comentarioRepository.findByTicketIdOrderByFechaCreacionDesc(ticketId);
        
        return comentarios.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convertir entidad a DTO
     */
    private ComentarioDTO convertirADTO(Comentario comentario) {
        return ComentarioDTO.builder()
                .comentarioId(comentario.getComentarioId())
                .autor(comentario.getAutor())
                .texto(comentario.getTexto())
                .fechaCreacion(comentario.getFechaCreacion())
                .build();
    }
}
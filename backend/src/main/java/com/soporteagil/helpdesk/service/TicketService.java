package com.soporteagil.helpdesk.service;

import com.soporteagil.helpdesk.dto.*;
import com.soporteagil.helpdesk.entity.*;
import com.soporteagil.helpdesk.entity.Ticket.*;
import com.soporteagil.helpdesk.exception.BusinessException;
import com.soporteagil.helpdesk.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    
    private final TicketRepository ticketRepository;
    private final CambioEstadoRepository cambioEstadoRepository;
    private final ComentarioRepository comentarioRepository;
    private final JdbcTemplate jdbcTemplate;
    
    /**
     * RF-01: Crear ticket
     */
    @Transactional
    public TicketDTO crearTicket(TicketDTO ticketDTO) {
        log.info("Creando nuevo ticket: {}", ticketDTO.getTitulo());
        
        // Validar prioridad
        Prioridad prioridad;
        try {
            prioridad = Prioridad.fromString(ticketDTO.getPrioridad());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Prioridad inválida: " + ticketDTO.getPrioridad());
        }
        
        // Crear entidad
        Ticket ticket = Ticket.builder()
                .titulo(ticketDTO.getTitulo())
                .descripcion(ticketDTO.getDescripcion())
                .solicitante(ticketDTO.getSolicitante())
                .categoria(ticketDTO.getCategoria())
                .prioridad(prioridad)
                .estado(Estado.ABIERTO)
                .build();
        
        // Guardar
        ticket = ticketRepository.save(ticket);
        log.info("Ticket creado exitosamente con ID: {}", ticket.getTicketId());
        
        return convertirADTO(ticket);
    }
    
    /**
     * RF-02: Listar y filtrar tickets con paginación
     */
    @Transactional(readOnly = true)
    public PageResponse<TicketDTO> listarTickets(String estado, String categoria, int page, int size) {
        log.info("Listando tickets - Estado: {}, Categoría: {}, Página: {}", estado, categoria, page);
        
        // Validar tamaño de página
        if (size > 100) size = 100;
        if (size < 1) size = 10;
        
        // Convertir estado de String a Enum si se proporciona
        Estado estadoEnum = null;
        if (estado != null && !estado.isBlank()) {
            try {
                estadoEnum = Estado.fromString(estado);
            } catch (IllegalArgumentException e) {
                throw new BusinessException("Estado inválido: " + estado);
            }
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> ticketsPage = ticketRepository.findByFilters(
                estadoEnum,
                (categoria != null && !categoria.isBlank()) ? categoria : null,
                pageable
        );
        
        List<TicketDTO> ticketDTOs = ticketsPage.getContent().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        
        return PageResponse.<TicketDTO>builder()
                .content(ticketDTOs)
                .pageNumber(ticketsPage.getNumber())
                .pageSize(ticketsPage.getSize())
                .totalElements(ticketsPage.getTotalElements())
                .totalPages(ticketsPage.getTotalPages())
                .last(ticketsPage.isLast())
                .first(ticketsPage.isFirst())
                .build();
    }
    
    /**
     * RF-03: Cambiar estado de ticket (usando PL/SQL para bonificación)
     */
    @Transactional
    public TicketDTO cambiarEstado(Long ticketId, String nuevoEstadoStr) {
        log.info("Cambiando estado del ticket {} a {}", ticketId, nuevoEstadoStr);
        
        // Validar que el ticket existe
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new BusinessException("Ticket no encontrado con ID: " + ticketId));
        
        // Validar nuevo estado
        Estado nuevoEstado;
        try {
            nuevoEstado = Estado.fromString(nuevoEstadoStr);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Estado inválido: " + nuevoEstadoStr);
        }
        
        // Validar transición usando lógica de negocio
        validarTransicionEstado(ticket.getEstado(), nuevoEstado);
        
        // Si se tiene Oracle configurado, usar el procedimiento PL/SQL
        // Para demostración, también incluyo la lógica directa en Java
        try {
            // Intentar usar procedimiento PL/SQL (bonificación)
            Map<String, Object> resultado = ejecutarProcedimientoCambioEstado(
                    ticketId, 
                    nuevoEstado.getValor()
            );
            
            Integer resultadoNum = (Integer) resultado.get("p_resultado");
            String mensaje = (String) resultado.get("p_mensaje");
            
            if (resultadoNum == 0) {
                throw new BusinessException(mensaje);
            }
            
            log.info("Estado cambiado usando PL/SQL: {}", mensaje);
            
        } catch (Exception e) {
            // Si falla el procedimiento, usar lógica directa
            log.warn("No se pudo usar PL/SQL, usando lógica directa: {}", e.getMessage());
            
            Estado estadoAnterior = ticket.getEstado();
            ticket.setEstado(nuevoEstado);
            ticketRepository.save(ticket);
            
            // Registrar cambio de estado
            CambioEstado cambio = CambioEstado.builder()
                    .ticket(ticket)
                    .estadoAnterior(estadoAnterior.getValor())
                    .estadoNuevo(nuevoEstado.getValor())
                    .build();
            cambioEstadoRepository.save(cambio);
        }
        
        // Recargar ticket actualizado
        ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new BusinessException("Error al recargar ticket"));
        
        log.info("Estado del ticket {} cambiado exitosamente a {}", ticketId, nuevoEstado.getValor());
        return convertirADTO(ticket);
    }
    
    /**
     * Validar transiciones de estado según reglas de negocio
     */
    private void validarTransicionEstado(Estado estadoActual, Estado nuevoEstado) {
        if (estadoActual == nuevoEstado) {
            throw new BusinessException("El estado nuevo es igual al estado actual");
        }
        
        boolean transicionValida = false;
        String mensajeError = "";
        
        switch (estadoActual) {
            case ABIERTO:
                transicionValida = (nuevoEstado == Estado.EN_PROGRESO || nuevoEstado == Estado.RESUELTO);
                mensajeError = "Desde 'Abierto' solo se puede cambiar a 'En Progreso' o 'Resuelto'";
                break;
                
            case EN_PROGRESO:
                transicionValida = (nuevoEstado == Estado.RESUELTO);
                mensajeError = "Desde 'En Progreso' solo se puede cambiar a 'Resuelto'";
                break;
                
            case RESUELTO:
                transicionValida = (nuevoEstado == Estado.CERRADO);
                mensajeError = "Desde 'Resuelto' solo se puede cambiar a 'Cerrado'";
                break;
                
            case CERRADO:
                transicionValida = false;
                mensajeError = "No se puede cambiar el estado de un ticket cerrado";
                break;
        }
        
        if (!transicionValida) {
            throw new BusinessException(
                    String.format("Transición de estado no permitida: %s → %s. %s",
                            estadoActual.getValor(), nuevoEstado.getValor(), mensajeError)
            );
        }
    }
    
    /**
     * Ejecutar procedimiento PL/SQL para cambio de estado (bonificación)
     */
    private Map<String, Object> ejecutarProcedimientoCambioEstado(Long ticketId, String nuevoEstado) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName("sp_cambiar_estado_ticket")
                    .declareParameters(
                            new SqlParameter("p_ticket_id", Types.NUMERIC),
                            new SqlParameter("p_estado_nuevo", Types.VARCHAR),
                            new SqlOutParameter("p_resultado", Types.NUMERIC),
                            new SqlOutParameter("p_mensaje", Types.VARCHAR)
                    );
            
            Map<String, Object> inParams = new HashMap<>();
            inParams.put("p_ticket_id", ticketId);
            inParams.put("p_estado_nuevo", nuevoEstado);
            
            return jdbcCall.execute(inParams);
            
        } catch (Exception e) {
            log.error("Error al ejecutar procedimiento PL/SQL: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Obtener ticket por ID con comentarios
     */
    @Transactional(readOnly = true)
    public TicketDTO obtenerTicketPorId(Long ticketId) {
        log.info("Obteniendo ticket con ID: {}", ticketId);
        
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new BusinessException("Ticket no encontrado con ID: " + ticketId));
        
        TicketDTO dto = convertirADTO(ticket);
        
        // Cargar comentarios
        List<Comentario> comentarios = comentarioRepository.findByTicketIdOrderByFechaCreacionDesc(ticketId);
        dto.setComentarios(comentarios.stream()
                .map(this::convertirComentarioADTO)
                .collect(Collectors.toList()));
        
        return dto;
    }
    
    /**
     * Convertir entidad a DTO
     */
    private TicketDTO convertirADTO(Ticket ticket) {
        return TicketDTO.builder()
                .ticketId(ticket.getTicketId())
                .titulo(ticket.getTitulo())
                .descripcion(ticket.getDescripcion())
                .solicitante(ticket.getSolicitante())
                .categoria(ticket.getCategoria())
                .prioridad(ticket.getPrioridad().getValor())
                .estado(ticket.getEstado().getValor())
                .fechaCreacion(ticket.getFechaCreacion())
                .fechaActualizacion(ticket.getFechaActualizacion())
                .build();
    }
    
    /**
     * Convertir comentario a DTO
     */
    private ComentarioDTO convertirComentarioADTO(Comentario comentario) {
        return ComentarioDTO.builder()
                .comentarioId(comentario.getComentarioId())
                .autor(comentario.getAutor())
                .texto(comentario.getTexto())
                .fechaCreacion(comentario.getFechaCreacion())
                .build();
    }
}
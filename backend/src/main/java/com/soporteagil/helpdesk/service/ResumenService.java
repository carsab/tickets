package com.soporteagil.helpdesk.service;

import com.soporteagil.helpdesk.dto.ResumenDiarioDTO;
import com.soporteagil.helpdesk.repository.CambioEstadoRepository;
import com.soporteagil.helpdesk.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumenService {
    
    private final TicketRepository ticketRepository;
    private final CambioEstadoRepository cambioEstadoRepository;
    
    /**
     * RF-05: Obtener resumen diario
     * - Total de tickets creados hoy
     * - Total de tickets resueltos hoy
     * - Top 3 categorías con tickets abiertos
     */
    @Transactional(readOnly = true)
    public ResumenDiarioDTO obtenerResumenDiario() {
        log.info("Generando resumen diario");
        
        // Contar tickets creados hoy
        Long ticketsCreadosHoy = ticketRepository.countTicketsCreadosHoy();
        log.debug("Tickets creados hoy: {}", ticketsCreadosHoy);
        
        // Contar tickets resueltos hoy
        Long ticketsResueltosHoy = cambioEstadoRepository.countTicketsResueltosHoy();
        log.debug("Tickets resueltos hoy: {}", ticketsResueltosHoy);
        
        // Obtener top 3 categorías con tickets abiertos
        List<Object[]> resultados = ticketRepository.findTop3CategoriasConTicketsAbiertos(
                PageRequest.of(0, 3)
        );
        
        List<ResumenDiarioDTO.CategoriaResumen> topCategorias = resultados.stream()
                .map(row -> new ResumenDiarioDTO.CategoriaResumen(
                        (String) row[0],  // categoria
                        ((Number) row[1]).longValue()  // cantidad
                ))
                .collect(Collectors.toList());
        
        log.debug("Top categorías: {}", topCategorias);
        
        return ResumenDiarioDTO.builder()
                .ticketsCreadosHoy(ticketsCreadosHoy)
                .ticketsResueltosHoy(ticketsResueltosHoy)
                .topCategorias(topCategorias)
                .build();
    }
}
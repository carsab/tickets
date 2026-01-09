package com.soporteagil.helpdesk.repository;

import com.soporteagil.helpdesk.entity.Ticket;
import com.soporteagil.helpdesk.entity.Ticket.Estado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    // Buscar con filtros opcionales de estado y categoría
    @Query("SELECT t FROM Ticket t WHERE " +
           "(:estado IS NULL OR t.estado = :estado) AND " +
           "(:categoria IS NULL OR t.categoria = :categoria) " +
           "ORDER BY t.fechaCreacion DESC")
    Page<Ticket> findByFilters(@Param("estado") Estado estado, 
                                @Param("categoria") String categoria,
                                Pageable pageable);
    
    // Contar tickets creados hoy (usando consulta nativa para Oracle)
    @Query(value = "SELECT COUNT(*) FROM tickets WHERE TRUNC(fecha_creacion) = TRUNC(SYSDATE)",
           nativeQuery = true)
    Long countTicketsCreadosHoy();
    
    // Contar tickets con estados abiertos o en progreso por categoría (Top 3)
    // Nota: Los estados se guardan con formato "Abierto", "En Progreso" (primera letra mayúscula)
    @Query("SELECT t.categoria AS categoria, COUNT(t) AS cantidad " +
           "FROM Ticket t " +
           "WHERE t.estado IN (com.soporteagil.helpdesk.entity.Ticket$Estado.ABIERTO, " +
           "com.soporteagil.helpdesk.entity.Ticket$Estado.EN_PROGRESO) " +
           "GROUP BY t.categoria " +
           "ORDER BY COUNT(t) DESC")
    List<Object[]> findTop3CategoriasConTicketsAbiertos(Pageable pageable);
}
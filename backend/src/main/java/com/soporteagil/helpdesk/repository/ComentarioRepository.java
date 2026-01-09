package com.soporteagil.helpdesk.repository;

import com.soporteagil.helpdesk.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    
    @Query("SELECT c FROM Comentario c WHERE c.ticket.ticketId = :ticketId " +
           "ORDER BY c.fechaCreacion DESC")
    List<Comentario> findByTicketIdOrderByFechaCreacionDesc(@Param("ticketId") Long ticketId);
}
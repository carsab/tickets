package com.soporteagil.helpdesk.repository;

import com.soporteagil.helpdesk.entity.CambioEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CambioEstadoRepository extends JpaRepository<CambioEstado, Long> {
    
    List<CambioEstado> findByTicketTicketIdOrderByFechaCambioDesc(Long ticketId);
    
    // Contar tickets resueltos hoy
    @Query("SELECT COUNT(c) FROM CambioEstado c WHERE " +
           "c.estadoNuevo = 'Resuelto' AND " +
           "CAST(c.fechaCambio AS date) = CAST(CURRENT_TIMESTAMP AS date)")
    Long countTicketsResueltosHoy();
}
package com.soporteagil.helpdesk.repository;

import com.soporteagil.helpdesk.entity.CambioEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CambioEstadoRepository extends JpaRepository<CambioEstado, Long> {
    
    List<CambioEstado> findByTicketTicketIdOrderByFechaCambioDesc(Long ticketId);
    
    // Contar tickets resueltos hoy (usando consulta nativa para Oracle)
    @Query(value = "SELECT COUNT(*) FROM cambios_estado WHERE estado_nuevo = 'Resuelto' AND TRUNC(fecha_cambio) = TRUNC(SYSDATE)",
           nativeQuery = true)
    Long countTicketsResueltosHoy();
}
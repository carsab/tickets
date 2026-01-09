// CambioEstado.java
package com.soporteagil.helpdesk.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cambios_estado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambioEstado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cambios_estado")
    @SequenceGenerator(name = "seq_cambios_estado", sequenceName = "seq_cambios_estado", allocationSize = 1)
    @Column(name = "cambio_id")
    private Long cambioId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
    
    @Column(name = "estado_anterior", nullable = false, length = 20)
    private String estadoAnterior;
    
    @Column(name = "estado_nuevo", nullable = false, length = 20)
    private String estadoNuevo;
    
    @Column(name = "fecha_cambio", nullable = false, updatable = false)
    private LocalDateTime fechaCambio;
    
    @PrePersist
    protected void onCreate() {
        fechaCambio = LocalDateTime.now();
    }
}

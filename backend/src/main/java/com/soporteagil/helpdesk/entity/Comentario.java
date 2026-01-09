
package com.soporteagil.helpdesk.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comentario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_comentarios")
    @SequenceGenerator(name = "seq_comentarios", sequenceName = "seq_comentarios", allocationSize = 1)
    @Column(name = "comentario_id")
    private Long comentarioId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
    
    @NotBlank(message = "El autor es obligatorio")
    @Column(nullable = false, length = 100)
    private String autor;
    
    @NotBlank(message = "El texto del comentario es obligatorio")
    @Column(nullable = false, length = 2000)
    private String texto;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
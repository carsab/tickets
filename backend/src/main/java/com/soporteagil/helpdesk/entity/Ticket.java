package com.soporteagil.helpdesk.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tickets")
    @SequenceGenerator(name = "seq_tickets", sequenceName = "seq_tickets", allocationSize = 1)
    @Column(name = "ticket_id")
    private Long ticketId;
    
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 120, message = "El título debe tener entre 5 y 120 caracteres")
    @Column(nullable = false, length = 120)
    private String titulo;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 2000, message = "La descripción debe tener entre 10 y 2000 caracteres")
    @Column(nullable = false, length = 2000)
    private String descripcion;
    
    @NotBlank(message = "El solicitante es obligatorio")
    @Column(nullable = false, length = 100)
    private String solicitante;
    
    @NotBlank(message = "La categoría es obligatoria")
    @Column(nullable = false, length = 50)
    private String categoria;
    
    @NotNull(message = "La prioridad es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Prioridad prioridad;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Estado estado = Estado.ABIERTO;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CambioEstado> cambiosEstado = new ArrayList<>();
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comentario> comentarios = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        estado = Estado.ABIERTO;
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    public enum Prioridad {
        BAJA("Baja"),
        MEDIA("Media"),
        ALTA("Alta");
        
        private final String valor;
        
        Prioridad(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
        
        public static Prioridad fromString(String valor) {
            for (Prioridad p : Prioridad.values()) {
                if (p.valor.equalsIgnoreCase(valor)) {
                    return p;
                }
            }
            throw new IllegalArgumentException("Prioridad inválida: " + valor);
        }
    }
    
    public enum Estado {
        ABIERTO("Abierto"),
        EN_PROGRESO("En Progreso"),
        RESUELTO("Resuelto"),
        CERRADO("Cerrado");
        
        private final String valor;
        
        Estado(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
        
        public static Estado fromString(String valor) {
            for (Estado e : Estado.values()) {
                if (e.valor.equalsIgnoreCase(valor)) {
                    return e;
                }
            }
            throw new IllegalArgumentException("Estado inválido: " + valor);
        }
    }
}
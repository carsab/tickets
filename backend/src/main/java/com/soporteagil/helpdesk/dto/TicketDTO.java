// TicketDTO.java
package com.soporteagil.helpdesk.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {
    private Long ticketId;
    
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 120, message = "El título debe tener entre 5 y 120 caracteres")
    private String titulo;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 2000, message = "La descripción debe tener entre 10 y 2000 caracteres")
    private String descripcion;
    
    @NotBlank(message = "El solicitante es obligatorio")
    private String solicitante;
    
    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;
    
    @NotBlank(message = "La prioridad es obligatoria")
    @Pattern(regexp = "Baja|Media|Alta", message = "La prioridad debe ser: Baja, Media o Alta")
    private String prioridad;
    
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<ComentarioDTO> comentarios;
}

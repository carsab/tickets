package com.soporteagil.helpdesk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioDTO {
    private Long comentarioId;
    
    @NotBlank(message = "El autor es obligatorio")
    private String autor;
    
    @NotBlank(message = "El texto del comentario es obligatorio")
    private String texto;
    
    private LocalDateTime fechaCreacion;
}
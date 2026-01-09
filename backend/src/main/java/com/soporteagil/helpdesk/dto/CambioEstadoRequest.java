package com.soporteagil.helpdesk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambioEstadoRequest {
    @NotBlank(message = "El nuevo estado es obligatorio")
    @Pattern(regexp = "Abierto|En Progreso|Resuelto|Cerrado", 
             message = "Estado inválido. Valores permitidos: Abierto, En Progreso, Resuelto, Cerrado")
    private String nuevoEstado;
}
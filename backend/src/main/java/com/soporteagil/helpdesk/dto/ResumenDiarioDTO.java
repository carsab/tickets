package com.soporteagil.helpdesk.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenDiarioDTO {
    private Long ticketsCreadosHoy;
    private Long ticketsResueltosHoy;
    private List<CategoriaResumen> topCategorias;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoriaResumen {
        private String categoria;
        private Long cantidad;
    }
}

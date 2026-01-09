package com.soporteagil.helpdesk.controller;

import com.soporteagil.helpdesk.dto.ApiResponse;
import com.soporteagil.helpdesk.dto.ResumenDiarioDTO;
import com.soporteagil.helpdesk.service.ResumenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resumen")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ResumenController {
    
    private final ResumenService resumenService;
    
    /**
     * RF-05: Obtener resumen diario
     * GET /api/resumen/diario
     */
    @GetMapping("/diario")
    public ResponseEntity<ApiResponse<ResumenDiarioDTO>> obtenerResumenDiario() {
        log.info("GET /api/resumen/diario - Obtener resumen diario");
        
        ResumenDiarioDTO resumen = resumenService.obtenerResumenDiario();
        
        ApiResponse<ResumenDiarioDTO> response = ApiResponse.success(
                resumen,
                "Resumen diario generado exitosamente"
        );
        
        return ResponseEntity.ok(response);
    }
}
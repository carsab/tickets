package com.soporteagil.helpdesk.converter;

import com.soporteagil.helpdesk.entity.Ticket.Estado;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertidor JPA para el enum Estado.
 * Convierte entre el enum y su valor legible en la base de datos.
 *
 * Esto es necesario porque la restricción CHECK en Oracle espera valores como
 * "Abierto", "En Progreso", etc., pero @Enumerated(EnumType.STRING) guardaría
 * los nombres del enum: "ABIERTO", "EN_PROGRESO", etc.
 */
@Converter(autoApply = true)
public class EstadoConverter implements AttributeConverter<Estado, String> {

    @Override
    public String convertToDatabaseColumn(Estado estado) {
        if (estado == null) {
            return null;
        }
        return estado.getValor();
    }

    @Override
    public Estado convertToEntityAttribute(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return Estado.fromString(valor);
    }
}

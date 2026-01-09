package com.soporteagil.helpdesk.converter;

import com.soporteagil.helpdesk.entity.Ticket.Prioridad;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertidor JPA para el enum Prioridad.
 * Convierte entre el enum y su valor legible en la base de datos.
 *
 * Mantiene consistencia con EstadoConverter para que todos los enums
 * se guarden con sus valores legibles en la base de datos.
 */
@Converter(autoApply = true)
public class PrioridadConverter implements AttributeConverter<Prioridad, String> {

    @Override
    public String convertToDatabaseColumn(Prioridad prioridad) {
        if (prioridad == null) {
            return null;
        }
        return prioridad.getValor();
    }

    @Override
    public Prioridad convertToEntityAttribute(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return Prioridad.fromString(valor);
    }
}

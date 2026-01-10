-- Scripts SQL DDL (Oracle)
alter session set "_ORACLE_SCRIPT" = true;

-- 1. Crear secuencias- Secuencias para IDs autoincrementales
CREATE SEQUENCE seq_tickets START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_cambios_estado START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_comentarios START WITH 1 INCREMENT BY 1;


-- 2. Crear tabla TICKETS

CREATE TABLE tickets (
    ticket_id NUMBER PRIMARY KEY,
    titulo VARCHAR2(120) NOT NULL,
    descripcion VARCHAR2(2000) NOT NULL,
    solicitante VARCHAR2(100) NOT NULL,
    categoria VARCHAR2(50) NOT NULL,
    prioridad VARCHAR2(10) NOT NULL,
    estado VARCHAR2(20) DEFAULT 'Abierto' NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- Constraints
    CONSTRAINT chk_titulo_longitud CHECK (LENGTH(titulo) BETWEEN 5 AND 120),
    CONSTRAINT chk_descripcion_longitud CHECK (LENGTH(descripcion) BETWEEN 10 AND 2000),
    CONSTRAINT chk_prioridad CHECK (prioridad IN ('Baja', 'Media', 'Alta')),
    CONSTRAINT chk_estado CHECK (estado IN ('Abierto', 'En Progreso', 'Resuelto', 'Cerrado'))
);

-- Índices para optimizar consultas
CREATE INDEX idx_tickets_estado ON tickets(estado);
CREATE INDEX idx_tickets_categoria ON tickets(categoria);
CREATE INDEX idx_tickets_fecha_creacion ON tickets(fecha_creacion DESC);
CREATE INDEX idx_tickets_estado_categoria ON tickets(estado, categoria);

-- 3. Crear tabla CAMBIOS_ESTADO

CREATE TABLE cambios_estado (
    cambio_id NUMBER PRIMARY KEY,
    ticket_id NUMBER NOT NULL,
    estado_anterior VARCHAR2(20) NOT NULL,
    estado_nuevo VARCHAR2(20) NOT NULL,
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- Foreign Key
    CONSTRAINT fk_cambios_ticket FOREIGN KEY (ticket_id) 
        REFERENCES tickets(ticket_id) ON DELETE CASCADE,
    
    -- Constraints
    CONSTRAINT chk_cambio_estado_anterior CHECK (estado_anterior IN ('Abierto', 'En Progreso', 'Resuelto', 'Cerrado')),
    CONSTRAINT chk_cambio_estado_nuevo CHECK (estado_nuevo IN ('Abierto', 'En Progreso', 'Resuelto', 'Cerrado'))
);

-- Índice para consultas por ticket
CREATE INDEX idx_cambios_ticket ON cambios_estado(ticket_id);
CREATE INDEX idx_cambios_fecha ON cambios_estado(fecha_cambio DESC);

-- 4. Crear tabla COMENTARIOS

CREATE TABLE comentarios (
    comentario_id NUMBER PRIMARY KEY,
    ticket_id NUMBER NOT NULL,
    autor VARCHAR2(100) NOT NULL,
    texto VARCHAR2(2000) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- Foreign Key
    CONSTRAINT fk_comentarios_ticket FOREIGN KEY (ticket_id) 
        REFERENCES tickets(ticket_id) ON DELETE CASCADE
);

-- Índice para consultas por ticket
CREATE INDEX idx_comentarios_ticket ON comentarios(ticket_id);
CREATE INDEX idx_comentarios_fecha ON comentarios(fecha_creacion DESC);

-- Procedimientos PL/SQL (Bonificación)

-- 1. Procedimiento para validar transiciones de estado

CREATE OR REPLACE PROCEDURE sp_validar_transicion_estado (
    p_estado_actual IN VARCHAR2,
    p_estado_nuevo IN VARCHAR2,
    p_es_valida OUT NUMBER,
    p_mensaje OUT VARCHAR2
) AS
BEGIN
    p_es_valida := 0;
    p_mensaje := 'Transición no permitida';
    
    -- Validar transiciones permitidas
    IF p_estado_actual = 'Abierto' AND p_estado_nuevo IN ('En Progreso', 'Resuelto') THEN
        p_es_valida := 1;
        p_mensaje := 'Transición válida';
    ELSIF p_estado_actual = 'En Progreso' AND p_estado_nuevo = 'Resuelto' THEN
        p_es_valida := 1;
        p_mensaje := 'Transición válida';
    ELSIF p_estado_actual = 'Resuelto' AND p_estado_nuevo = 'Cerrado' THEN
        p_es_valida := 1;
        p_mensaje := 'Transición válida';
    ELSIF p_estado_actual = p_estado_nuevo THEN
        p_es_valida := 0;
        p_mensaje := 'El estado nuevo es igual al actual';
    ELSE
        p_es_valida := 0;
        p_mensaje := 'Transición de estado no permitida: ' || p_estado_actual || ' → ' || p_estado_nuevo;
    END IF;
    
EXCEPTION
    WHEN OTHERS THEN
        p_es_valida := 0;
        p_mensaje := 'Error al validar transición: ' || SQLERRM;
END;
/


-- 2. Procedimiento para cambiar estado de ticket
 
CREATE OR REPLACE PROCEDURE sp_cambiar_estado_ticket (
    p_ticket_id IN NUMBER,
    p_estado_nuevo IN VARCHAR2,
    p_resultado OUT NUMBER,
    p_mensaje OUT VARCHAR2
) AS
    v_estado_actual VARCHAR2(20);
    v_es_valida NUMBER;
    v_mensaje_validacion VARCHAR2(500);
BEGIN
    -- Obtener estado actual
    SELECT estado INTO v_estado_actual 
    FROM tickets 
    WHERE ticket_id = p_ticket_id;
    
    -- Validar transición
    sp_validar_transicion_estado(v_estado_actual, p_estado_nuevo, v_es_valida, v_mensaje_validacion);
    
    IF v_es_valida = 1 THEN
        -- Actualizar estado del ticket
        UPDATE tickets 
        SET estado = p_estado_nuevo,
            fecha_actualizacion = CURRENT_TIMESTAMP
        WHERE ticket_id = p_ticket_id;
        
        -- Registrar cambio de estado
        INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo)
        VALUES (seq_cambios_estado.NEXTVAL, p_ticket_id, v_estado_actual, p_estado_nuevo);
        
        COMMIT;
        p_resultado := 1;
        p_mensaje := 'Estado actualizado exitosamente';
    ELSE
        p_resultado := 0;
        p_mensaje := v_mensaje_validacion;
    END IF;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_resultado := 0;
        p_mensaje := 'Ticket no encontrado';
    WHEN OTHERS THEN
        ROLLBACK;
        p_resultado := 0;
        p_mensaje := 'Error al cambiar estado: ' || SQLERRM;
END;
/


-- 3. Función para obtener resumen diario
 
CREATE OR REPLACE FUNCTION fn_obtener_resumen_diario 
RETURN SYS_REFCURSOR AS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT 
            (SELECT COUNT(*) FROM tickets WHERE TRUNC(fecha_creacion) = TRUNC(SYSDATE)) AS tickets_creados_hoy,
            (SELECT COUNT(*) FROM cambios_estado WHERE estado_nuevo = 'Resuelto' AND TRUNC(fecha_cambio) = TRUNC(SYSDATE)) AS tickets_resueltos_hoy
        FROM dual;
    
    RETURN v_cursor;
END;
/

-- 4. Función para top 3 categorías con tickets abiertos
 
CREATE OR REPLACE FUNCTION fn_top_categorias_abiertas 
RETURN SYS_REFCURSOR AS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT categoria, COUNT(*) AS cantidad
        FROM tickets
        WHERE estado IN ('Abierto', 'En Progreso')
        GROUP BY categoria
        ORDER BY cantidad DESC
        FETCH FIRST 3 ROWS ONLY;
    
    RETURN v_cursor;
END;
/
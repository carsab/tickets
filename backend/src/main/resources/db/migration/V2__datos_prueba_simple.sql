-- ============================================================================
-- Script Simplificado: Datos de Prueba para Resumen Diario
-- Base de datos: Oracle 11g
-- Sistema: Help Desk Mini
-- ============================================================================
-- IMPORTANTE: Los valores de estado y prioridad usan formato con primera letra
-- en mayúscula: "Abierto", "En Progreso", "Resuelto", "Cerrado"
--               "Alta", "Media", "Baja"

-- Limpiar datos existentes
DELETE FROM comentarios;
DELETE FROM cambios_estado;
DELETE FROM tickets;
COMMIT;

-- ============================================================================
-- HARDWARE: 12 tickets abiertos/en progreso, 3 resueltos
-- ============================================================================

-- Hardware ABIERTOS (8)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (1, 'HW001 - Monitor no enciende', 'El monitor del puesto 101 no enciende', 'Carlos García', 'Hardware', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (2, 'HW002 - Teclado con fallas', 'Varias teclas del teclado no responden', 'María López', 'Hardware', 'Media', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (3, 'HW003 - Mouse sin conexión', 'El mouse Bluetooth no conecta', 'Juan Pérez', 'Hardware', 'Baja', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (4, 'HW004 - Impresora atascada', 'La impresora tiene papel atascado', 'Ana Torres', 'Hardware', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (5, 'HW005 - PC no arranca', 'El equipo no enciende', 'Pedro Sánchez', 'Hardware', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (6, 'HW006 - Laptop sobrecalentamiento', 'La laptop se apaga por calor', 'Laura Martínez', 'Hardware', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (7, 'HW007 - Parlantes sin audio', 'Los parlantes no funcionan', 'Roberto Díaz', 'Hardware', 'Baja', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (8, 'HW008 - Cámara web borrosa', 'La imagen de la cámara es mala', 'Sandra Ruiz', 'Hardware', 'Media', 'Abierto', SYSDATE, SYSDATE);

-- Hardware EN PROGRESO (4)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (9, 'HW009 - Disco duro lleno', 'El disco está al 95% capacidad', 'Miguel Ángel', 'Hardware', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (10, 'HW010 - Memoria RAM insuficiente', 'El equipo se congela constantemente', 'Carmen Flores', 'Hardware', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (11, 'HW011 - Puerto USB dañado', 'El puerto USB no reconoce dispositivos', 'Diego Hernández', 'Hardware', 'Media', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (12, 'HW012 - Ventilador ruidoso', 'El ventilador del CPU hace ruido', 'Patricia Vega', 'Hardware', 'Baja', 'En Progreso', SYSDATE, SYSDATE);

-- Hardware RESUELTOS (3) - con registro de cambio de estado
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (13, 'HW013 - Cable desconectado', 'Sin conexión por cable suelto', 'Fernando Castro', 'Hardware', 'Alta', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (1, 13, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (14, 'HW014 - Monitor con líneas', 'El monitor mostraba líneas', 'Sofía Morales', 'Hardware', 'Media', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (2, 14, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (15, 'HW015 - Batería agotada', 'La batería no cargaba', 'Andrés Jiménez', 'Hardware', 'Alta', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (3, 15, 'Abierto', 'Resuelto', SYSDATE);

-- ============================================================================
-- SOFTWARE: 8 tickets abiertos/en progreso, 2 resueltos
-- ============================================================================

-- Software ABIERTOS (5)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (16, 'SW001 - Error en Excel', 'Excel muestra error al iniciar', 'Lucía Mendoza', 'Software', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (17, 'SW002 - Outlook no sincroniza', 'El correo no descarga mensajes', 'Ricardo Ortiz', 'Software', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (18, 'SW003 - Antivirus desactualizado', 'El antivirus no se actualiza', 'Elena Vargas', 'Software', 'Media', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (19, 'SW004 - Teams no conecta', 'Microsoft Teams con errores', 'Gabriel Luna', 'Software', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (20, 'SW005 - Sistema lento', 'PC lento después de update', 'Valeria Ríos', 'Software', 'Media', 'Abierto', SYSDATE, SYSDATE);

-- Software EN PROGRESO (3)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (21, 'SW006 - Instalación SAP', 'Requiere instalación SAP FI', 'Arturo Domínguez', 'Software', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (22, 'SW007 - Licencia Adobe', 'La licencia Adobe venció', 'Natalia Ponce', 'Software', 'Media', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (23, 'SW008 - VPN no conecta', 'Error de autenticación VPN', 'Héctor Medina', 'Software', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

-- Software RESUELTOS (2)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (24, 'SW009 - Drivers actualizados', 'Se actualizaron drivers video', 'Daniela Solís', 'Software', 'Baja', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (4, 24, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (25, 'SW010 - Config impresora', 'Se configuró impresora red', 'Mauricio Reyes', 'Software', 'Media', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (5, 25, 'Abierto', 'Resuelto', SYSDATE);

-- ============================================================================
-- ACCESO: 5 tickets abiertos/en progreso, 1 resuelto
-- ============================================================================

-- Acceso ABIERTOS (3)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (26, 'AC001 - Acceso carpeta', 'Necesito acceso a Contabilidad', 'Claudia Herrera', 'Acceso', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (27, 'AC002 - Reset contraseña', 'Olvidé contraseña del ERP', 'Raúl Guerrero', 'Acceso', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (28, 'AC003 - Permisos usuario', 'Necesito permisos admin temporal', 'Isabel Campos', 'Acceso', 'Media', 'Abierto', SYSDATE, SYSDATE);

-- Acceso EN PROGRESO (2)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (29, 'AC004 - Alta usuario nuevo', 'Crear cuenta nuevo empleado', 'Teresa Molina', 'Acceso', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (30, 'AC005 - Desbloqueo cuenta', 'Cuenta bloqueada por intentos', 'Oscar Fuentes', 'Acceso', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

-- Acceso RESUELTOS (1)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (31, 'AC006 - Baja usuario', 'Desactivar cuenta ex-empleado', 'Rosa Navarro', 'Acceso', 'Media', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (6, 31, 'Abierto', 'Resuelto', SYSDATE);

-- ============================================================================
-- RED: 3 tickets abiertos/en progreso, 2 resueltos
-- ============================================================================

-- Red ABIERTOS (2)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (32, 'RD001 - Internet lento', 'La conexión está muy lenta', 'Javier Romero', 'Red', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (33, 'RD002 - WiFi no conecta', 'No puedo conectar WiFi corp', 'Mónica Delgado', 'Red', 'Media', 'Abierto', SYSDATE, SYSDATE);

-- Red EN PROGRESO (1)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (34, 'RD003 - Punto red sin servicio', 'Punto de red escritorio 205', 'Ernesto Blanco', 'Red', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

-- Red RESUELTOS (2)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (35, 'RD004 - Config IP fija', 'Se asignó IP fija servidor', 'Adriana Salas', 'Red', 'Media', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (7, 35, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (36, 'RD005 - Acceso servidor remoto', 'Se habilitó puerto remoto', 'Hugo Espinoza', 'Red', 'Alta', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (8, 36, 'Abierto', 'Resuelto', SYSDATE);

COMMIT;

-- ============================================================================
-- CONSULTAS DE VERIFICACIÓN
-- ============================================================================

-- 1. Verificar total de tickets
SELECT 'Total Tickets' AS descripcion, COUNT(*) AS cantidad FROM tickets;

-- 2. Verificar tickets por estado
SELECT estado, COUNT(*) AS cantidad
FROM tickets
GROUP BY estado
ORDER BY estado;

-- 3. Verificar tickets abiertos/en progreso por categoría (para el Top 3)
SELECT categoria, COUNT(*) AS cantidad
FROM tickets
WHERE estado IN ('Abierto', 'En Progreso')
GROUP BY categoria
ORDER BY cantidad DESC;

-- 4. Verificar tickets creados hoy
SELECT COUNT(*) AS tickets_creados_hoy
FROM tickets
WHERE TRUNC(fecha_creacion) = TRUNC(SYSDATE);

-- 5. Verificar tickets resueltos hoy
SELECT COUNT(*) AS tickets_resueltos_hoy
FROM cambios_estado
WHERE estado_nuevo = 'Resuelto'
AND TRUNC(fecha_cambio) = TRUNC(SYSDATE);

-- ============================================================================
-- VALORES ESPERADOS:
-- ============================================================================
-- Total Tickets: 36
--
-- Por Estado:
--   Abierto: 18 (8 HW + 5 SW + 3 AC + 2 RD)
--   En Progreso: 10 (4 HW + 3 SW + 2 AC + 1 RD)
--   Resuelto: 8 (3 HW + 2 SW + 1 AC + 2 RD)
--
-- Tickets Creados Hoy: 36
-- Tickets Resueltos Hoy: 8
--
-- Top 3 Categorías (abiertos + en progreso):
--   1. Hardware: 12 (8 + 4)
--   2. Software: 8 (5 + 3)
--   3. Acceso: 5 (3 + 2)
--   (Red: 3 = 2 + 1)
-- ============================================================================

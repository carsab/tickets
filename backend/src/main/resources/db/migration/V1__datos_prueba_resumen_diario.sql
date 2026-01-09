-- ============================================================================
-- Script de Migración: Datos de Prueba para Resumen Diario
-- Base de datos: Oracle 11g
-- Sistema: Help Desk Mini
-- Fecha: 2026-01-09
-- ============================================================================

-- IMPORTANTE: Este script crea datos de prueba para validar el resumen diario
-- Los tickets se crean con fecha de hoy (SYSDATE) para que aparezcan en el resumen

-- ============================================================================
-- PASO 0: Limpiar datos existentes (OPCIONAL - Descomentar si es necesario)
-- ============================================================================
-- DELETE FROM comentarios;
-- DELETE FROM cambios_estado;
-- DELETE FROM tickets;
-- COMMIT;

-- ============================================================================
-- PASO 1: Crear secuencias si no existen
-- ============================================================================
-- Las secuencias ya deberían existir, pero por seguridad:
-- CREATE SEQUENCE seq_tickets START WITH 1 INCREMENT BY 1;
-- CREATE SEQUENCE seq_cambios_estado START WITH 1 INCREMENT BY 1;
-- CREATE SEQUENCE seq_comentarios START WITH 1 INCREMENT BY 1;

-- ============================================================================
-- PASO 2: Insertar Tickets de HARDWARE
-- ============================================================================
-- 8 tickets ABIERTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Monitor no enciende', 'El monitor del puesto 101 no enciende desde esta mañana', 'Carlos García', 'Hardware', 'ALTA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Teclado con teclas pegadas', 'Varias teclas del teclado no responden correctamente', 'María López', 'Hardware', 'MEDIA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Mouse inalámbrico sin conexión', 'El mouse Bluetooth no se conecta al equipo', 'Juan Pérez', 'Hardware', 'BAJA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Impresora atascada', 'La impresora del piso 3 tiene papel atascado', 'Ana Torres', 'Hardware', 'ALTA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'PC no arranca', 'El equipo no enciende, posible falla en fuente', 'Pedro Sánchez', 'Hardware', 'ALTA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Laptop sobrecalentándose', 'La laptop se apaga por sobrecalentamiento', 'Laura Martínez', 'Hardware', 'ALTA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Parlantes sin audio', 'Los parlantes del escritorio no emiten sonido', 'Roberto Díaz', 'Hardware', 'BAJA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Cámara web borrosa', 'La imagen de la cámara web se ve muy borrosa', 'Sandra Ruiz', 'Hardware', 'MEDIA', 'ABIERTO', SYSDATE, SYSDATE);

-- 4 tickets EN_PROGRESO
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Disco duro lleno', 'El disco duro está al 95% de capacidad', 'Miguel Ángel', 'Hardware', 'ALTA', 'EN_PROGRESO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Memoria RAM insuficiente', 'El equipo se congela por falta de memoria', 'Carmen Flores', 'Hardware', 'ALTA', 'EN_PROGRESO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Puerto USB dañado', 'Uno de los puertos USB no reconoce dispositivos', 'Diego Hernández', 'Hardware', 'MEDIA', 'EN_PROGRESO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Ventilador ruidoso', 'El ventilador del CPU hace mucho ruido', 'Patricia Vega', 'Hardware', 'BAJA', 'EN_PROGRESO', SYSDATE, SYSDATE);

-- 3 tickets RESUELTOS (con cambio de estado)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Cable de red desconectado', 'Sin conexión por cable suelto', 'Fernando Castro', 'Hardware', 'ALTA', 'RESUELTO', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Monitor con líneas', 'El monitor mostraba líneas horizontales', 'Sofía Morales', 'Hardware', 'MEDIA', 'RESUELTO', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Batería de laptop agotada', 'La batería no cargaba, se reemplazó', 'Andrés Jiménez', 'Hardware', 'ALTA', 'RESUELTO', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

-- ============================================================================
-- PASO 3: Insertar Tickets de SOFTWARE
-- ============================================================================
-- 5 tickets ABIERTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Error al abrir Excel', 'Excel muestra error al iniciar y no abre archivos', 'Lucía Mendoza', 'Software', 'ALTA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Outlook no sincroniza', 'El correo de Outlook no descarga nuevos mensajes', 'Ricardo Ortiz', 'Software', 'ALTA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Antivirus desactualizado', 'El antivirus no se actualiza hace 2 semanas', 'Elena Vargas', 'Software', 'MEDIA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Teams no conecta', 'Microsoft Teams muestra error de conexión', 'Gabriel Luna', 'Software', 'ALTA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Sistema lento después de actualización', 'El PC quedó muy lento después del último Windows Update', 'Valeria Ríos', 'Software', 'MEDIA', 'ABIERTO', SYSDATE, SYSDATE);

-- 3 tickets EN_PROGRESO
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Instalación de SAP', 'Requiere instalación del módulo SAP FI', 'Arturo Domínguez', 'Software', 'ALTA', 'EN_PROGRESO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Licencia de Adobe expirada', 'La licencia de Adobe Creative Cloud venció', 'Natalia Ponce', 'Software', 'MEDIA', 'EN_PROGRESO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'VPN no conecta', 'Error de autenticación al conectar VPN corporativa', 'Héctor Medina', 'Software', 'ALTA', 'EN_PROGRESO', SYSDATE, SYSDATE);

-- 2 tickets RESUELTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Actualización de drivers', 'Se actualizaron los drivers de video', 'Daniela Solís', 'Software', 'BAJA', 'RESUELTO', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Configuración de impresora', 'Se configuró la impresora de red', 'Mauricio Reyes', 'Software', 'MEDIA', 'RESUELTO', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

-- ============================================================================
-- PASO 4: Insertar Tickets de ACCESO
-- ============================================================================
-- 3 tickets ABIERTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Acceso a carpeta compartida', 'Necesito acceso a la carpeta de Contabilidad', 'Claudia Herrera', 'Acceso', 'ALTA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Reset de contraseña', 'Olvidé mi contraseña del sistema ERP', 'Raúl Guerrero', 'Acceso', 'ALTA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Permisos de usuario', 'Necesito permisos de administrador temporal', 'Isabel Campos', 'Acceso', 'MEDIA', 'ABIERTO', SYSDATE, SYSDATE);

-- 2 tickets EN_PROGRESO
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Alta de usuario nuevo', 'Crear cuenta para nuevo empleado de RRHH', 'Teresa Molina', 'Acceso', 'ALTA', 'EN_PROGRESO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Desbloqueo de cuenta', 'Mi cuenta quedó bloqueada por intentos fallidos', 'Oscar Fuentes', 'Acceso', 'ALTA', 'EN_PROGRESO', SYSDATE, SYSDATE);

-- 1 ticket RESUELTO
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Baja de usuario', 'Desactivar cuenta de empleado que salió', 'Rosa Navarro', 'Acceso', 'MEDIA', 'RESUELTO', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

-- ============================================================================
-- PASO 5: Insertar Tickets de RED
-- ============================================================================
-- 2 tickets ABIERTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Internet lento', 'La conexión a internet está muy lenta hoy', 'Javier Romero', 'Red', 'ALTA', 'ABIERTO', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'WiFi no conecta', 'No puedo conectarme a la red WiFi corporativa', 'Mónica Delgado', 'Red', 'MEDIA', 'ABIERTO', SYSDATE, SYSDATE);

-- 1 ticket EN_PROGRESO
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Punto de red sin servicio', 'El punto de red del escritorio 205 no funciona', 'Ernesto Blanco', 'Red', 'ALTA', 'EN_PROGRESO', SYSDATE, SYSDATE);

-- 2 tickets RESUELTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Configuración de IP fija', 'Se asignó IP fija para servidor de pruebas', 'Adriana Salas', 'Red', 'MEDIA', 'RESUELTO', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Acceso a servidor remoto', 'Se habilitó puerto para acceso remoto', 'Hugo Espinoza', 'Red', 'ALTA', 'RESUELTO', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

-- ============================================================================
-- PASO 6: Confirmar transacción
-- ============================================================================
COMMIT;

-- ============================================================================
-- PASO 7: Verificación de datos insertados
-- ============================================================================
-- Ejecutar estas consultas para verificar los datos:

-- Total de tickets por estado
SELECT estado, COUNT(*) as cantidad
FROM tickets
GROUP BY estado
ORDER BY estado;

-- Total de tickets por categoría (solo abiertos/en progreso)
SELECT categoria, COUNT(*) as cantidad
FROM tickets
WHERE estado IN ('ABIERTO', 'EN_PROGRESO')
GROUP BY categoria
ORDER BY cantidad DESC;

-- Tickets creados hoy
SELECT COUNT(*) as tickets_creados_hoy
FROM tickets
WHERE TRUNC(fecha_creacion) = TRUNC(SYSDATE);

-- Tickets resueltos hoy
SELECT COUNT(*) as tickets_resueltos_hoy
FROM cambios_estado
WHERE estado_nuevo = 'Resuelto'
AND TRUNC(fecha_cambio) = TRUNC(SYSDATE);

-- ============================================================================
-- RESUMEN DE DATOS ESPERADOS
-- ============================================================================
--
-- TICKETS CREADOS HOY: 28 total
--   - Hardware: 17 (8 Abierto + 4 En Progreso + 3 Resuelto + 2 Cerrado)
--     Nota: Los 2 cerrados cuentan como creados pero no como abiertos
--   - Software: 10 (5 Abierto + 3 En Progreso + 2 Resuelto)
--   - Acceso: 6 (3 Abierto + 2 En Progreso + 1 Resuelto)
--   - Red: 5 (2 Abierto + 1 En Progreso + 2 Resuelto)
--
-- TICKETS RESUELTOS HOY: 8 (3 Hardware + 2 Software + 1 Acceso + 2 Red)
--
-- TOP 3 CATEGORÍAS (tickets abiertos/en progreso):
--   1. Hardware: 12 tickets (8 Abierto + 4 En Progreso)
--   2. Software: 8 tickets (5 Abierto + 3 En Progreso)
--   3. Acceso: 5 tickets (3 Abierto + 2 En Progreso)
--   (Red tiene 3 tickets: 2 Abierto + 1 En Progreso)
--
-- ============================================================================

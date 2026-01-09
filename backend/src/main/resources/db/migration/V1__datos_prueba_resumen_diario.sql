-- ============================================================================
-- Script de Migración: Datos de Prueba para Resumen Diario
-- Base de datos: Oracle 11g
-- Sistema: Help Desk Mini
-- Fecha: 2026-01-09
-- ============================================================================
-- IMPORTANTE: Los valores de estado y prioridad usan formato con primera letra
-- en mayúscula: "Abierto", "En Progreso", "Resuelto", "Cerrado"
--               "Alta", "Media", "Baja"

-- ============================================================================
-- PASO 0: Limpiar datos existentes (OPCIONAL - Descomentar si es necesario)
-- ============================================================================
-- DELETE FROM comentarios;
-- DELETE FROM cambios_estado;
-- DELETE FROM tickets;
-- COMMIT;

-- ============================================================================
-- PASO 1: Insertar Tickets de HARDWARE
-- ============================================================================
-- 8 tickets ABIERTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Monitor no enciende', 'El monitor del puesto 101 no enciende desde esta mañana', 'Carlos García', 'Hardware', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Teclado con teclas pegadas', 'Varias teclas del teclado no responden correctamente', 'María López', 'Hardware', 'Media', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Mouse inalámbrico sin conexión', 'El mouse Bluetooth no se conecta al equipo', 'Juan Pérez', 'Hardware', 'Baja', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Impresora atascada', 'La impresora del piso 3 tiene papel atascado', 'Ana Torres', 'Hardware', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'PC no arranca', 'El equipo no enciende, posible falla en fuente', 'Pedro Sánchez', 'Hardware', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Laptop sobrecalentándose', 'La laptop se apaga por sobrecalentamiento', 'Laura Martínez', 'Hardware', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Parlantes sin audio', 'Los parlantes del escritorio no emiten sonido', 'Roberto Díaz', 'Hardware', 'Baja', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Cámara web borrosa', 'La imagen de la cámara web se ve muy borrosa', 'Sandra Ruiz', 'Hardware', 'Media', 'Abierto', SYSDATE, SYSDATE);

-- 4 tickets EN PROGRESO
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Disco duro lleno', 'El disco duro está al 95% de capacidad', 'Miguel Ángel', 'Hardware', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Memoria RAM insuficiente', 'El equipo se congela por falta de memoria', 'Carmen Flores', 'Hardware', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Puerto USB dañado', 'Uno de los puertos USB no reconoce dispositivos', 'Diego Hernández', 'Hardware', 'Media', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Ventilador ruidoso', 'El ventilador del CPU hace mucho ruido', 'Patricia Vega', 'Hardware', 'Baja', 'En Progreso', SYSDATE, SYSDATE);

-- 3 tickets RESUELTOS (con cambio de estado)
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Cable de red desconectado', 'Sin conexión por cable suelto', 'Fernando Castro', 'Hardware', 'Alta', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Monitor con líneas', 'El monitor mostraba líneas horizontales', 'Sofía Morales', 'Hardware', 'Media', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Batería de laptop agotada', 'La batería no cargaba, se reemplazó', 'Andrés Jiménez', 'Hardware', 'Alta', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

-- ============================================================================
-- PASO 2: Insertar Tickets de SOFTWARE
-- ============================================================================
-- 5 tickets ABIERTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Error al abrir Excel', 'Excel muestra error al iniciar y no abre archivos', 'Lucía Mendoza', 'Software', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Outlook no sincroniza', 'El correo de Outlook no descarga nuevos mensajes', 'Ricardo Ortiz', 'Software', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Antivirus desactualizado', 'El antivirus no se actualiza hace 2 semanas', 'Elena Vargas', 'Software', 'Media', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Teams no conecta', 'Microsoft Teams muestra error de conexión', 'Gabriel Luna', 'Software', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Sistema lento después de actualización', 'El PC quedó muy lento después del último Windows Update', 'Valeria Ríos', 'Software', 'Media', 'Abierto', SYSDATE, SYSDATE);

-- 3 tickets EN PROGRESO
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Instalación de SAP', 'Requiere instalación del módulo SAP FI', 'Arturo Domínguez', 'Software', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Licencia de Adobe expirada', 'La licencia de Adobe Creative Cloud venció', 'Natalia Ponce', 'Software', 'Media', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'VPN no conecta', 'Error de autenticación al conectar VPN corporativa', 'Héctor Medina', 'Software', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

-- 2 tickets RESUELTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Actualización de drivers', 'Se actualizaron los drivers de video', 'Daniela Solís', 'Software', 'Baja', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Configuración de impresora', 'Se configuró la impresora de red', 'Mauricio Reyes', 'Software', 'Media', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

-- ============================================================================
-- PASO 3: Insertar Tickets de ACCESO
-- ============================================================================
-- 3 tickets ABIERTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Acceso a carpeta compartida', 'Necesito acceso a la carpeta de Contabilidad', 'Claudia Herrera', 'Acceso', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Reset de contraseña', 'Olvidé mi contraseña del sistema ERP', 'Raúl Guerrero', 'Acceso', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Permisos de usuario', 'Necesito permisos de administrador temporal', 'Isabel Campos', 'Acceso', 'Media', 'Abierto', SYSDATE, SYSDATE);

-- 2 tickets EN PROGRESO
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Alta de usuario nuevo', 'Crear cuenta para nuevo empleado de RRHH', 'Teresa Molina', 'Acceso', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Desbloqueo de cuenta', 'Mi cuenta quedó bloqueada por intentos fallidos', 'Oscar Fuentes', 'Acceso', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

-- 1 ticket RESUELTO
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Baja de usuario', 'Desactivar cuenta de empleado que salió', 'Rosa Navarro', 'Acceso', 'Media', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

-- ============================================================================
-- PASO 4: Insertar Tickets de RED
-- ============================================================================
-- 2 tickets ABIERTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Internet lento', 'La conexión a internet está muy lenta hoy', 'Javier Romero', 'Red', 'Alta', 'Abierto', SYSDATE, SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'WiFi no conecta', 'No puedo conectarme a la red WiFi corporativa', 'Mónica Delgado', 'Red', 'Media', 'Abierto', SYSDATE, SYSDATE);

-- 1 ticket EN PROGRESO
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Punto de red sin servicio', 'El punto de red del escritorio 205 no funciona', 'Ernesto Blanco', 'Red', 'Alta', 'En Progreso', SYSDATE, SYSDATE);

-- 2 tickets RESUELTOS
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Configuración de IP fija', 'Se asignó IP fija para servidor de pruebas', 'Adriana Salas', 'Red', 'Media', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado, fecha_creacion, fecha_actualizacion)
VALUES (seq_tickets.NEXTVAL, 'Acceso a servidor remoto', 'Se habilitó puerto para acceso remoto', 'Hugo Espinoza', 'Red', 'Alta', 'Resuelto', SYSDATE, SYSDATE);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, seq_tickets.CURRVAL, 'Abierto', 'Resuelto', SYSDATE);

-- ============================================================================
-- PASO 5: Confirmar transacción
-- ============================================================================
COMMIT;

-- ============================================================================
-- PASO 6: Verificación de datos insertados
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
WHERE estado IN ('Abierto', 'En Progreso')
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
--   - Hardware: 15 (8 Abierto + 4 En Progreso + 3 Resuelto)
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

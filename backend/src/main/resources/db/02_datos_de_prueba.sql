
-- Scripts DML - Datos de Prueba
-- Insertar tickets de ejemplo
INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado)
VALUES (seq_tickets.NEXTVAL, 'Problema con impresora HP LaserJet', 
        'La impresora del piso 3 no responde, muestra error de papel atascado pero no hay papel visible', 
        'Carlos Mendoza', 'Hardware', 'Media', 'Abierto');

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado)
VALUES (seq_tickets.NEXTVAL, 'Solicitud de acceso a carpeta compartida Ventas', 
        'Necesito acceso de lectura y escritura a la carpeta \\servidor\ventas\reportes para generar informes mensuales', 
        'Ana García', 'Acceso', 'Alta', 'Abierto');

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado)
VALUES (seq_tickets.NEXTVAL, 'Excel se cierra inesperadamente al abrir archivos grandes', 
        'Cuando intento abrir archivos de Excel mayores a 50MB, la aplicación se cierra sin previo aviso. Ya reinicié la PC', 
        'Roberto Silva', 'Software', 'Media', 'En Progreso');

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado)
VALUES (seq_tickets.NEXTVAL, 'Conexión VPN intermitente desde casa', 
        'La VPN se desconecta cada 15-20 minutos cuando trabajo desde casa. Tengo fibra óptica de 100Mbps', 
        'Laura Fernández', 'Red', 'Alta', 'En Progreso');

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado)
VALUES (seq_tickets.NEXTVAL, 'Cambio de teclado por teclas dañadas', 
        'Las teclas E, R y T no responden correctamente. Necesito reemplazo urgente para continuar trabajando', 
        'Pedro Ramírez', 'Hardware', 'Baja', 'Resuelto');

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado)
VALUES (seq_tickets.NEXTVAL, 'Instalación de software AutoCAD 2024', 
        'Requiero instalación de AutoCAD 2024 para nuevo proyecto de diseño que inicia la próxima semana', 
        'María González', 'Software', 'Media', 'Resuelto');

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado)
VALUES (seq_tickets.NEXTVAL, 'Cuenta de correo llena - no puedo recibir emails', 
        'Mi buzón está al 100% de capacidad y no puedo recibir correos nuevos. Necesito ampliación de cuota', 
        'Jorge Torres', 'Software', 'Alta', 'Cerrado');

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado)
VALUES (seq_tickets.NEXTVAL, 'Monitor con líneas verticales en pantalla', 
        'El monitor secundario muestra líneas verticales de colores. Ya cambié el cable HDMI sin mejora', 
        'Sofía Vargas', 'Hardware', 'Media', 'Abierto');

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado)
VALUES (seq_tickets.NEXTVAL, 'Acceso denegado a sistema de nómina', 
        'No puedo acceder al sistema de nómina con mis credenciales. Mensaje: usuario bloqueado', 
        'Luis Morales', 'Acceso', 'Alta', 'Abierto');

INSERT INTO tickets (ticket_id, titulo, descripcion, solicitante, categoria, prioridad, estado)
VALUES (seq_tickets.NEXTVAL, 'Lentitud general en computadora de escritorio', 
        'La computadora está muy lenta, tarda varios minutos en iniciar y las aplicaciones se congelan frecuentemente', 
        'Carmen Ruiz', 'Hardware', 'Baja', 'Abierto');

-- Insertar comentarios de ejemplo
INSERT INTO comentarios (comentario_id, ticket_id, autor, texto)
VALUES (seq_comentarios.NEXTVAL, 3, 'Técnico IT', 
        'Se verificó el equipo, el problema es por falta de memoria RAM. Excel requiere más recursos para archivos grandes.');

INSERT INTO comentarios (comentario_id, ticket_id, autor, texto)
VALUES (seq_comentarios.NEXTVAL, 3, 'Roberto Silva', 
        'Gracias por la revisión. ¿Cuándo podrían hacer la ampliación de memoria?');

INSERT INTO comentarios (comentario_id, ticket_id, autor, texto)
VALUES (seq_comentarios.NEXTVAL, 4, 'Soporte Redes', 
        'Se detectó problema con configuración de timeout. Se ajustó el perfil VPN. Favor probar y confirmar.');

INSERT INTO comentarios (comentario_id, ticket_id, autor, texto)
VALUES (seq_comentarios.NEXTVAL, 5, 'Técnico IT', 
        'Teclado reemplazado y probado exitosamente. Ticket cerrado.');

INSERT INTO comentarios (comentario_id, ticket_id, autor, texto)
VALUES (seq_comentarios.NEXTVAL, 6, 'Soporte Software', 
        'AutoCAD 2024 instalado y licencia activada. Usuario capacitado en funciones básicas.');

-- Insertar cambios de estado de ejemplo
INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, 3, 'Abierto', 'En Progreso', CURRENT_TIMESTAMP - 2);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, 4, 'Abierto', 'En Progreso', CURRENT_TIMESTAMP - 3);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, 5, 'Abierto', 'En Progreso', CURRENT_TIMESTAMP - 5);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, 5, 'En Progreso', 'Resuelto', CURRENT_TIMESTAMP - 1);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, 6, 'Abierto', 'En Progreso', CURRENT_TIMESTAMP - 4);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, 6, 'En Progreso', 'Resuelto', CURRENT_TIMESTAMP - 2);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, 7, 'Abierto', 'En Progreso', CURRENT_TIMESTAMP - 6);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, 7, 'En Progreso', 'Resuelto', CURRENT_TIMESTAMP - 4);

INSERT INTO cambios_estado (cambio_id, ticket_id, estado_anterior, estado_nuevo, fecha_cambio)
VALUES (seq_cambios_estado.NEXTVAL, 7, 'Resuelto', 'Cerrado', CURRENT_TIMESTAMP - 3);

COMMIT;


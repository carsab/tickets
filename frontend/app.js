// app.js - Frontend Logic for Help Desk Mini

const API_URL = 'http://localhost:8080/api';
let paginaActual = 0;
let ticketActualId = null;

// Inicializar aplicación
document.addEventListener('DOMContentLoaded', function() {
    cargarResumenDiario();
    mostrarSeccion('dashboard');
});

// Mostrar/ocultar secciones
function mostrarSeccion(seccion) {
    document.querySelectorAll('.seccion').forEach(s => s.style.display = 'none');
    
    // Actualizar navbar
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    
    switch(seccion) {
        case 'dashboard':
            document.getElementById('seccionDashboard').style.display = 'block';
            cargarResumenDiario();
            break;
        case 'tickets':
            document.getElementById('seccionTickets').style.display = 'block';
            cargarTickets();
            break;
        case 'crear':
            document.getElementById('seccionCrear').style.display = 'block';
            document.getElementById('formCrearTicket').reset();
            break;
        case 'detalle':
            document.getElementById('seccionDetalle').style.display = 'block';
            break;
    }
}

// Loading overlay
function mostrarLoading(mostrar) {
    const overlay = document.getElementById('loadingOverlay');
    if (mostrar) {
        overlay.classList.add('active');
    } else {
        overlay.classList.remove('active');
    }
}

// Mostrar notificación
function mostrarNotificacion(mensaje, tipo = 'success') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${tipo} alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3`;
    alertDiv.style.zIndex = '10000';
    alertDiv.style.minWidth = '300px';
    alertDiv.innerHTML = `
        ${mensaje}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(alertDiv);
    
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

// ==================== RF-05: Resumen Diario ====================
async function cargarResumenDiario() {
    try {
        mostrarLoading(true);
        const response = await fetch(`${API_URL}/resumen/diario`);
        const data = await response.json();
        
        if (data.success) {
            const resumen = data.data;
            
            document.getElementById('ticketsCreadosHoy').textContent = resumen.ticketsCreadosHoy;
            document.getElementById('ticketsResueltosHoy').textContent = resumen.ticketsResueltosHoy;
            
            const topCategoriasDiv = document.getElementById('topCategorias');
            if (resumen.topCategorias && resumen.topCategorias.length > 0) {
                topCategoriasDiv.innerHTML = resumen.topCategorias.map((cat, index) => `
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <span><strong>${index + 1}.</strong> ${cat.categoria}</span>
                        <span class="badge bg-warning text-dark">${cat.cantidad}</span>
                    </div>
                `).join('');
            } else {
                topCategoriasDiv.innerHTML = '<p class="text-muted mb-0">Sin tickets abiertos</p>';
            }
        }
    } catch (error) {
        console.error('Error al cargar resumen:', error);
        mostrarNotificacion('Error al cargar el resumen diario', 'danger');
    } finally {
        mostrarLoading(false);
    }
}

// ==================== RF-02: Listar/Filtrar Tickets ====================
async function cargarTickets(pagina = 0) {
    try {
        mostrarLoading(true);
        paginaActual = pagina;
        
        const estado = document.getElementById('filtroEstado').value;
        const categoria = document.getElementById('filtroCategoria').value;
        
        let url = `${API_URL}/tickets?page=${pagina}&size=10`;
        if (estado) url += `&estado=${encodeURIComponent(estado)}`;
        if (categoria) url += `&categoria=${encodeURIComponent(categoria)}`;
        
        const response = await fetch(url);
        const data = await response.json();
        
        if (data.success) {
            const pageData = data.data;
            mostrarListaTickets(pageData.content);
            mostrarPaginacion(pageData);
        } else {
            mostrarNotificacion(data.message, 'danger');
        }
    } catch (error) {
        console.error('Error al cargar tickets:', error);
        mostrarNotificacion('Error al cargar los tickets', 'danger');
    } finally {
        mostrarLoading(false);
    }
}

function mostrarListaTickets(tickets) {
    const listaDiv = document.getElementById('listaTickets');
    
    if (tickets.length === 0) {
        listaDiv.innerHTML = `
            <div class="col-12">
                <div class="alert alert-info">
                    <i class="bi bi-info-circle"></i> No se encontraron tickets con los filtros seleccionados
                </div>
            </div>
        `;
        return;
    }
    
    listaDiv.innerHTML = tickets.map(ticket => `
        <div class="col-md-6 col-lg-4">
            <div class="card ticket-item" onclick="verDetalleTicket(${ticket.ticketId})">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <h6 class="card-title mb-0">#${ticket.ticketId}</h6>
                        <div>
                            ${getBadgePrioridad(ticket.prioridad)}
                            ${getBadgeEstado(ticket.estado)}
                        </div>
                    </div>
                    <p class="card-text"><strong>${ticket.titulo}</strong></p>
                    <p class="card-text text-muted small">${truncar(ticket.descripcion, 80)}</p>
                    <hr>
                    <div class="d-flex justify-content-between align-items-center">
                        <small class="text-muted">
                            <i class="bi bi-tag"></i> ${ticket.categoria}
                        </small>
                        <small class="text-muted">
                            <i class="bi bi-person"></i> ${ticket.solicitante}
                        </small>
                    </div>
                    <small class="text-muted">
                        <i class="bi bi-clock"></i> ${formatearFecha(ticket.fechaCreacion)}
                    </small>
                </div>
            </div>
        </div>
    `).join('');
}

function mostrarPaginacion(pageData) {
    const paginacionDiv = document.getElementById('paginacion');
    const totalPaginas = pageData.totalPages;
    const paginaActual = pageData.pageNumber;
    
    if (totalPaginas <= 1) {
        paginacionDiv.innerHTML = '';
        return;
    }
    
    let html = '';
    
    // Botón Anterior
    html += `
        <li class="page-item ${paginaActual === 0 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="cargarTickets(${paginaActual - 1}); return false;">
                <i class="bi bi-chevron-left"></i>
            </a>
        </li>
    `;
    
    // Páginas
    for (let i = 0; i < totalPaginas; i++) {
        if (i === paginaActual || i === 0 || i === totalPaginas - 1 || 
            (i >= paginaActual - 1 && i <= paginaActual + 1)) {
            html += `
                <li class="page-item ${i === paginaActual ? 'active' : ''}">
                    <a class="page-link" href="#" onclick="cargarTickets(${i}); return false;">
                        ${i + 1}
                    </a>
                </li>
            `;
        } else if (i === paginaActual - 2 || i === paginaActual + 2) {
            html += '<li class="page-item disabled"><span class="page-link">...</span></li>';
        }
    }
    
    // Botón Siguiente
    html += `
        <li class="page-item ${paginaActual === totalPaginas - 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="cargarTickets(${paginaActual + 1}); return false;">
                <i class="bi bi-chevron-right"></i>
            </a>
        </li>
    `;
    
    paginacionDiv.innerHTML = html;
}

// ==================== RF-01: Crear Ticket ====================
async function crearTicket(event) {
    event.preventDefault();
    
    const ticketData = {
        titulo: document.getElementById('titulo').value,
        descripcion: document.getElementById('descripcion').value,
        solicitante: document.getElementById('solicitante').value,
        categoria: document.getElementById('categoria').value,
        prioridad: document.getElementById('prioridad').value
    };
    
    try {
        mostrarLoading(true);
        const response = await fetch(`${API_URL}/tickets`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(ticketData)
        });
        
        const data = await response.json();
        
        if (data.success) {
            mostrarNotificacion(data.message, 'success');
            document.getElementById('formCrearTicket').reset();
            setTimeout(() => {
                mostrarSeccion('tickets');
            }, 1000);
        } else {
            if (data.data) {
                // Errores de validación
                const errores = Object.values(data.data).join('<br>');
                mostrarNotificacion(errores, 'danger');
            } else {
                mostrarNotificacion(data.message, 'danger');
            }
        }
    } catch (error) {
        console.error('Error al crear ticket:', error);
        mostrarNotificacion('Error al crear el ticket', 'danger');
    } finally {
        mostrarLoading(false);
    }
}

// ==================== Ver Detalle de Ticket ====================
async function verDetalleTicket(ticketId) {
    try {
        mostrarLoading(true);
        ticketActualId = ticketId;
        
        const response = await fetch(`${API_URL}/tickets/${ticketId}`);
        const data = await response.json();
        
        if (data.success) {
            const ticket = data.data;
            
            document.getElementById('detalleId').textContent = ticket.ticketId;
            document.getElementById('detalletitulo').textContent = ticket.titulo;
            document.getElementById('detalleDescripcion').textContent = ticket.descripcion;
            document.getElementById('detalleSolicitante').textContent = ticket.solicitante;
            document.getElementById('detalleCategoria').textContent = ticket.categoria;
            document.getElementById('detalleFechaCreacion').textContent = formatearFecha(ticket.fechaCreacion);
            document.getElementById('detalleFechaActualizacion').textContent = formatearFecha(ticket.fechaActualizacion);
            
            document.getElementById('detallePrioridad').className = 'badge ' + getClasePrioridad(ticket.prioridad);
            document.getElementById('detallePrioridad').textContent = ticket.prioridad;
            
            document.getElementById('detalleEstado').className = 'badge ' + getClaseEstado(ticket.estado);
            document.getElementById('detalleEstado').textContent = ticket.estado;
            
            document.getElementById('nuevoEstado').value = ticket.estado;
            
            await cargarComentarios(ticketId);
            
            mostrarSeccion('detalle');
        }
    } catch (error) {
        console.error('Error al cargar detalle:', error);
        mostrarNotificacion('Error al cargar el detalle del ticket', 'danger');
    } finally {
        mostrarLoading(false);
    }
}

// ==================== RF-03: Cambiar Estado ====================
async function cambiarEstado() {
    const nuevoEstado = document.getElementById('nuevoEstado').value;
    
    try {
        mostrarLoading(true);
        const response = await fetch(`${API_URL}/tickets/${ticketActualId}/estado`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nuevoEstado })
        });
        
        const data = await response.json();
        
        if (data.success) {
            mostrarNotificacion(data.message, 'success');
            await verDetalleTicket(ticketActualId);
        } else {
            mostrarNotificacion(data.message, 'danger');
        }
    } catch (error) {
        console.error('Error al cambiar estado:', error);
        mostrarNotificacion('Error al cambiar el estado', 'danger');
    } finally {
        mostrarLoading(false);
    }
}

// ==================== RF-04: Comentarios ====================
async function cargarComentarios(ticketId) {
    try {
        const response = await fetch(`${API_URL}/tickets/${ticketId}/comentarios`);
        const data = await response.json();
        
        if (data.success) {
            const comentarios = data.data;
            const listaDiv = document.getElementById('listaComentarios');
            
            if (comentarios.length === 0) {
                listaDiv.innerHTML = '<p class="text-muted">No hay comentarios aún</p>';
            } else {
                listaDiv.innerHTML = comentarios.map(com => `
                    <div class="comentario-item mb-3">
                        <div class="d-flex justify-content-between">
                            <strong>${com.autor}</strong>
                            <small class="text-muted">${formatearFecha(com.fechaCreacion)}</small>
                        </div>
                        <p class="mb-0 mt-1">${com.texto}</p>
                    </div>
                `).join('');
            }
        }
    } catch (error) {
        console.error('Error al cargar comentarios:', error);
    }
}

async function agregarComentario(event) {
    event.preventDefault();
    
    const comentarioData = {
        autor: document.getElementById('autorComentario').value,
        texto: document.getElementById('textoComentario').value
    };
    
    try {
        mostrarLoading(true);
        const response = await fetch(`${API_URL}/tickets/${ticketActualId}/comentarios`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(comentarioData)
        });
        
        const data = await response.json();
        
        if (data.success) {
            mostrarNotificacion('Comentario agregado exitosamente', 'success');
            document.getElementById('autorComentario').value = '';
            document.getElementById('textoComentario').value = '';
            await cargarComentarios(ticketActualId);
        } else {
            mostrarNotificacion(data.message, 'danger');
        }
    } catch (error) {
        console.error('Error al agregar comentario:', error);
        mostrarNotificacion('Error al agregar el comentario', 'danger');
    } finally {
        mostrarLoading(false);
    }
}

// ==================== Utilidades ====================
function getBadgePrioridad(prioridad) {
    const clases = {
        'Alta': 'badge-prioridad-alta',
        'Media': 'badge-prioridad-media',
        'Baja': 'badge-prioridad-baja'
    };
    return `<span class="badge ${clases[prioridad]}">${prioridad}</span>`;
}

function getBadgeEstado(estado) {
    const clases = {
        'Abierto': 'badge-estado-abierto',
        'En Progreso': 'badge-estado-en-progreso',
        'Resuelto': 'badge-estado-resuelto',
        'Cerrado': 'badge-estado-cerrado'
    };
    return `<span class="badge ${clases[estado]}">${estado}</span>`;
}

function getClasePrioridad(prioridad) {
    const clases = {
        'Alta': 'badge-prioridad-alta',
        'Media': 'badge-prioridad-media',
        'Baja': 'badge-prioridad-baja'
    };
    return clases[prioridad] || 'bg-secondary';
}

function getClaseEstado(estado) {
    const clases = {
        'Abierto': 'badge-estado-abierto',
        'En Progreso': 'badge-estado-en-progreso',
        'Resuelto': 'badge-estado-resuelto',
        'Cerrado': 'badge-estado-cerrado'
    };
    return clases[estado] || 'bg-secondary';
}

function formatearFecha(fecha) {
    const date = new Date(fecha);
    return date.toLocaleString('es-ES', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function truncar(texto, longitud) {
    if (texto.length <= longitud) return texto;
    return texto.substring(0, longitud) + '...';
}
import React, { useState, useEffect } from 'react';

export default function BookingForm({ onBookingComplete, onClose }) {
  const [servicios, setServicios] = useState([]);
  const [empleados, setEmpleados] = useState([]);
  const [horarios, setHorarios] = useState([]);
  const [citasExistentes, setCitasExistentes] = useState([]);
  const [horasDisponibles, setHorasDisponibles] = useState([]);
  const [cargandoDatos, setCargandoDatos] = useState(true);

  const [formData, setFormData] = useState({
    servicioId: '',
    empleadoId: '',
    fecha: '',
    hora: '',
    notas: ''
  });

  // 1. CARGAMOS TODA LA ARTILLERÍA AL ABRIR EL MODAL
  useEffect(() => {
    const cargarDatos = async () => {
      try {
        const [resServ, resEmp, resHorarios, resCitas] = await Promise.all([
          fetch('http://localhost:8080/api/servicios'),
          fetch('http://localhost:8080/api/empleado'),
          fetch('http://localhost:8080/api/horarios'),
          fetch('http://localhost:8080/api/citas')
        ]);

        if (resServ.ok) setServicios(await resServ.json());
        if (resEmp.ok) setEmpleados(await resEmp.json());
        if (resHorarios.ok) setHorarios(await resHorarios.json());
        if (resCitas.ok) setCitasExistentes(await resCitas.json());
      } catch (error) {
        console.error("Error cargando opciones:", error);
      } finally {
        setCargandoDatos(false);
      }
    };
    cargarDatos();
  }, []);

  // 2. EL CEREBRO DEL SISTEMA: CALCULAR HORAS DISPONIBLES
  useEffect(() => {
    if (!formData.empleadoId || !formData.fecha) {
      setHorasDisponibles([]);
      return;
    }

    const fechaObj = new Date(`${formData.fecha}T12:00:00`); 
    let diaJS = fechaObj.getDay(); 
    const diaJava = diaJS === 0 ? 7 : diaJS; 

    let horarioDia = null;

    if (horarios && horarios.length > 0) {
      for (let i = 0; i < horarios.length; i++) {
        const h = horarios[i];
        
        const idEmpBackend = h.empleado?.id || h.empleado?.id_empleado || h.id_empleado || h.empleadoId;
        const diaBackend = h.diaSemana || h.dia_semana || h.dia || h.dia_Semana;

        if (String(idEmpBackend) === String(formData.empleadoId) && String(diaBackend) === String(diaJava)) {
          horarioDia = h;
          break;
        }
      }
    }

    if (!horarioDia) {
      setHorasDisponibles([]);
      return;
    }

    const extraerHoraInt = (campo) => {
      if (campo == null) return null;
      if (typeof campo === 'object' && campo.hour !== undefined) return campo.hour;
      if (typeof campo === 'string') return parseInt(campo.split(':')[0]);
      if (Array.isArray(campo)) return campo[0];
      return null;
    };

    const inicio = extraerHoraInt(horarioDia.horaInicio) ?? extraerHoraInt(horarioDia.hora_inicio) ?? 10;
    const cierre = extraerHoraInt(horarioDia.horacierre) ?? extraerHoraInt(horarioDia.hora_cierre) ?? 19;
    const colacionInicio = extraerHoraInt(horarioDia.horaInicioAlmuerzo) ?? extraerHoraInt(horarioDia.hora_in_almuerzo) ?? 14;
    const colacionFin = extraerHoraInt(horarioDia.horaFinAlmuerzo) ?? extraerHoraInt(horarioDia.hora_fin_almuerzo) ?? 15;

    let slots = [];
    for (let i = inicio; i < cierre; i++) {
      if (i >= colacionInicio && i < colacionFin) continue; 
      const horaFormateada = `${i.toString().padStart(2, '0')}:00`;
      slots.push(horaFormateada);
    }

    const citasOcupadasDelDia = citasExistentes.filter(cita => {
      if (!cita.empleado || !cita.fechaHora || cita.estado?.toLowerCase() === 'cancelado') return false;
      const idEmp = cita.empleado.id || cita.empleado.id_empleado;
      const mismaFecha = cita.fechaHora.startsWith(formData.fecha);
      return parseInt(idEmp) === parseInt(formData.empleadoId) && mismaFecha;
    });

    const horasOcupadas = citasOcupadasDelDia.map(cita => {
      const horaCita = new Date(cita.fechaHora).getHours();
      return `${horaCita.toString().padStart(2, '0')}:00`;
    });

    const slotsFinales = slots.filter(slot => !horasOcupadas.includes(slot));
    setHorasDisponibles(slotsFinales);

  }, [formData.empleadoId, formData.fecha, horarios, citasExistentes]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === 'fecha' || name === 'empleadoId') {
      setFormData({ ...formData, [name]: value, hora: '' });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.servicioId || !formData.empleadoId || !formData.fecha || !formData.hora) {
      return alert("Por favor, completa todos los campos obligatorios ✨");
    }

    const userId = localStorage.getItem('userId') || 2;
    const servicioSeleccionado = servicios.find(s => parseInt(s.id) === parseInt(formData.servicioId) || parseInt(s.id_servicio) === parseInt(formData.servicioId));
    const precioFinal = servicioSeleccionado ? servicioSeleccionado.precio : 0;
    const duracionFinal = servicioSeleccionado ? (servicioSeleccionado.duracion_min || 45) : 45;

    const fechaHoraFormateada = `${formData.fecha}T${formData.hora}:00`;

    // PAQUETE MAESTRO CON DOBLE ID PARA PREVENIR ERRORES DE LECTURA EN JAVA
    const nuevaReserva = {
      fechaHora: fechaHoraFormateada,
      estado: "Pendiente",
      notas: formData.notas,
      valorTotal: precioFinal,
      duracionTotal: duracionFinal,
      usuario: { 
        id: parseInt(userId),
        id_usuario: parseInt(userId)
      }, 
      empleado: { 
        id: parseInt(formData.empleadoId),
        id_empleado: parseInt(formData.empleadoId)
      }, 
      detalles: [{
          precioCita: precioFinal,
          servicio: { 
            id: parseInt(formData.servicioId),
            id_servicio: parseInt(formData.servicioId)
          }
      }]
    };

    try {
      const respuesta = await fetch('http://localhost:8080/api/citas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(nuevaReserva)
      });

      if (respuesta.ok) {
        alert("✅ ¡Cita agendada con éxito! Ya puedes verla en el panel.");
        onBookingComplete(formData);
      } else {
        console.error("El servidor rechazó la reserva");
        alert("Hubo un problema al guardar la reserva en la base de datos.");
      }
    } catch (error) {
      console.error("Falla de conexión:", error);
    }
  };

  return (
    <div className="relative bg-[#fff5f8] p-8 md:p-12 rounded-[3rem] shadow-2xl max-w-2xl w-full border border-pink-100">
      <button onClick={onClose} className="absolute top-6 right-6 text-[#b02a6b] hover:scale-110 transition-transform font-bold text-2xl">✕</button>

      <div className="text-center mb-10">
        <p className="text-[#f171ab] text-xs font-bold uppercase tracking-[0.2em] mb-2">Reserva Online</p>
        <h2 className="text-4xl font-serif text-[#b02a6b] italic">Tu Nueva Imagen te Espera</h2>
      </div>

      {cargandoDatos ? (
        <p className="text-center text-[#f171ab] font-bold py-10">Conectando con el sistema...</p>
      ) : (
        <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-6">
          
          <div className="flex flex-col gap-2 md:col-span-2">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Servicio</label>
            <select name="servicioId" value={formData.servicioId} onChange={handleChange} className="input-pripelu" required>
              <option value="">Selecciona lo que buscas</option>
              {servicios.map(s => (
                <option key={s.id || s.id_servicio} value={s.id || s.id_servicio}>
                  {s.nombre} - ${s.precio}
                </option>
              ))}
            </select>
          </div>

          <div className="flex flex-col gap-2 md:col-span-2">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Estilista</label>
            <select name="empleadoId" value={formData.empleadoId} onChange={handleChange} className="input-pripelu" required>
              <option value="">Elige a tu profesional</option>
              {empleados.map(emp => (
                <option key={emp.id || emp.id_empleado} value={emp.id || emp.id_empleado}>
                  {emp.nombre} {emp.apellido} - {emp.especialidad}
                </option>
              ))}
            </select>
          </div>

          <div className="flex flex-col gap-2">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Fecha</label>
            <input name="fecha" value={formData.fecha} onChange={handleChange} type="date" className="input-pripelu" required />
          </div>

          <div className="flex flex-col gap-2">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Hora preferida</label>
            <select name="hora" value={formData.hora} onChange={handleChange} className="input-pripelu" required disabled={!formData.empleadoId || !formData.fecha}>
              <option value="">
                {!formData.empleadoId || !formData.fecha 
                  ? "Selecciona fecha y estilista" 
                  : horasDisponibles.length === 0 
                  ? "❌ Sin horas disponibles" 
                  : "Selecciona una hora"}
              </option>
              {horasDisponibles.map(h => (
                <option key={h} value={h}>{h}</option>
              ))}
            </select>
          </div>

          <div className="flex flex-col gap-2 md:col-span-2">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Notas</label>
            <textarea name="notas" value={formData.notas} onChange={handleChange} placeholder="¿Alguna alergia o detalle?" className="input-pripelu h-24 resize-none"></textarea>
          </div>

          <button type="submit" className="md:col-span-2 bg-[#f171ab] text-white py-5 rounded-2xl font-bold text-lg shadow-lg hover:bg-[#d85a94] transition-all mt-4 disabled:opacity-50" disabled={horasDisponibles.length === 0 && formData.fecha}>
            Confirmar Reserva
          </button>
        </form>
      )}
    </div>
  );
}
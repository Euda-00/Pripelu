import React, { useState, useEffect } from 'react';

export default function BookingForm({ onBookingComplete, onClose }) {
  // 1. Estados para guardar las listas que vienen del backend
  const [servicios, setServicios] = useState([]);
  const [empleados, setEmpleados] = useState([]);
  const [cargandoDatos, setCargandoDatos] = useState(true);

  // 2. El estado del formulario ahora guarda IDs, no textos
  const [formData, setFormData] = useState({
    servicioId: '',
    empleadoId: '',
    fecha: '',
    hora: '',
    notas: ''
  });

  // 3. Llamamos al backend apenas se abre el modal
  useEffect(() => {
    const cargarDatos = async () => {
      try {
        const resServicios = await fetch('http://localhost:8080/api/servicios');
        if (resServicios.ok) setServicios(await resServicios.json());

        // Asumimos que Eugenio le puso /api/empleados a su controlador
        const resEmpleados = await fetch('http://localhost:8080/api/empleado');
        if (resEmpleados.ok) setEmpleados(await resEmpleados.json());
      } catch (error) {
        console.error("Error cargando opciones:", error);
      } finally {
        setCargandoDatos(false);
      }
    };
    cargarDatos();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.servicioId || !formData.empleadoId || !formData.fecha || !formData.hora) {
      return alert("Por favor, completa todos los campos obligatorios ✨");
    }

    // Buscamos el ID del usuario en el navegador. 
    // Si no lo tienes guardado aún en tu login, forzamos el ID 2 (el de Tomas) para que no explote.
    const userId = localStorage.getItem('userId') || 2;

    // Rescatamos el precio del servicio elegido para guardarlo en la boleta
    const servicioSeleccionado = servicios.find(s => s.id === parseInt(formData.servicioId) || s.id_servicio === parseInt(formData.servicioId));
    const precioFinal = servicioSeleccionado ? servicioSeleccionado.precio : 0;
    const duracionFinal = servicioSeleccionado ? (servicioSeleccionado.duracion_min || 45) : 45;

    // Juntamos la fecha y la hora para que Java la entienda (Formato ISO)
    const fechaHoraFormateada = `${formData.fecha}T${formData.hora}:00`;

    // 4. EL PAQUETE MAESTRO PARA JAVA
    const nuevaReserva = {
      fechaHora: fechaHoraFormateada,
      estado: "Pendiente",
      notas: formData.notas,
      valorTotal: precioFinal,
      duracionTotal: duracionFinal,
      usuario: { id: parseInt(userId) }, 
      empleado: { id: parseInt(formData.empleadoId) }, 
      detalles: [
        {
          servicio: { id: parseInt(formData.servicioId) }, 
          precioCita: precioFinal
        }
      ]
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
        alert("Hubo un problema al guardar la reserva. Intenta de nuevo.");
      }
    } catch (error) {
      console.error("Falla de conexión:", error);
      alert("Error de conexión con el servidor.");
    }
  };

  return (
    <div className="relative bg-[#fff5f8] p-8 md:p-12 rounded-[3rem] shadow-2xl max-w-2xl w-full border border-pink-100">
      <button 
        onClick={onClose} 
        className="absolute top-6 right-6 text-[#b02a6b] hover:scale-110 transition-transform font-bold text-2xl"
      >
        ✕
      </button>

      <div className="text-center mb-10">
        <p className="text-[#f171ab] text-xs font-bold uppercase tracking-[0.2em] mb-2">Reserva Online</p>
        <h2 className="text-4xl font-serif text-[#b02a6b] italic">Tu Nueva Imagen te Espera</h2>
      </div>

      {cargandoDatos ? (
        <p className="text-center text-[#f171ab] font-bold py-10">Cargando profesionales y servicios...</p>
      ) : (
        <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-6">
          
          {/* Servicio Dinámico */}
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

          {/* Estilista Dinámico */}
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

          {/* Fecha */}
          <div className="flex flex-col gap-2">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Fecha</label>
            <input name="fecha" value={formData.fecha} onChange={handleChange} type="date" className="input-pripelu" required />
          </div>

          {/* Hora */}
          <div className="flex flex-col gap-2">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Hora preferida</label>
            <select name="hora" value={formData.hora} onChange={handleChange} className="input-pripelu" required>
              <option value="">Selecciona una hora</option>
              <option value="10:00">10:00 AM</option>
              <option value="11:00">11:00 AM</option>
              <option value="12:00">12:00 PM</option>
              <option value="15:00">15:00 PM</option>
              <option value="16:00">16:00 PM</option>
              <option value="17:00">17:00 PM</option>
            </select>
          </div>

          {/* Notas */}
          <div className="flex flex-col gap-2 md:col-span-2">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Notas</label>
            <textarea name="notas" value={formData.notas} onChange={handleChange} placeholder="¿Alguna alergia o detalle?" className="input-pripelu h-24 resize-none"></textarea>
          </div>

          <button type="submit" className="md:col-span-2 bg-[#f171ab] text-white py-5 rounded-2xl font-bold text-lg shadow-lg hover:bg-[#d85a94] transition-all mt-4">
            Confirmar Reserva
          </button>
        </form>
      )}
    </div>
  );
}
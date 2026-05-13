import React, { useState, useEffect } from 'react';

export default function BookingForm({ onBookingComplete, onClose }) { // Añadimos onClose
  const [formData, setFormData] = useState({
    nombre: '',
    telefono: '',
    servicio: '',
    estilista: 'Sin preferencia',
    fecha: '',
    hora: '',
    notas: ''
  });

  // Guardamos cada cambio en el formulario (opcional para debug o futuras validaciones)
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (!formData.nombre || !formData.telefono) return alert("Completa los datos");

    // 1. Traer lo que ya existe en el LocalStorage o un array vacío si no hay nada
    const reservasActuales = JSON.parse(localStorage.getItem('listaReservas') || "[]");

    // 2. Crear la nueva reserva con un ID único para que no se repitan
    const nuevaReserva = { ...formData, id: Date.now() };

    // 3. Guardar la lista actualizada
    const nuevaLista = [...reservasActuales, nuevaReserva];
    localStorage.setItem('listaReservas', JSON.stringify(nuevaLista));

    alert("✅ Cita agendada con éxito");
    onBookingComplete(formData);
  };

  return (
    <div className="relative bg-[#fff5f8] p-8 md:p-12 rounded-[3rem] shadow-2xl max-w-4xl w-full border border-pink-100">
      
      {/* BOTÓN DE CERRAR (La X) */}
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

      {/* Nombre */}
      <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="flex flex-col gap-2">
          <label className="text-[#b02a6b] font-bold text-sm ml-2">Nombre completo</label>
          <input name="nombre" value={formData.nombre} onChange={handleChange} type="text" placeholder="Ej: Maria Paz" className="input-pripelu" required />
        </div>

        {/* Teléfono con formato internacional para evitar confusiones */}
        <div className="flex flex-col gap-2">
          <label className="text-[#b02a6b] font-bold text-sm ml-2">Teléfono</label>
          <input name="telefono" value={formData.telefono} onChange={handleChange} type="text" placeholder="+56 9..." className="input-pripelu" required />
        </div>

        {/* Servicio */}
        <div className="flex flex-col gap-2">
          <label className="text-[#b02a6b] font-bold text-sm ml-2">Servicio</label>
          <select name="servicio" value={formData.servicio} onChange={handleChange} className="input-pripelu" required>
            <option value="">Selecciona un servicio</option>
            <option value="Corte Dama">Corte de Dama</option>
            <option value="Colorimetría">Colorimetría</option>
            <option value="Balayage">Balayage</option>
            <option value="Peinado">Peinado Especial</option>
          </select>
        </div>

        {/* Estilista (opcional, para que puedan elegir si quieren a alguien específico o no) */}
        <div className="flex flex-col gap-2">
          <label className="text-[#b02a6b] font-bold text-sm ml-2">Estilista</label>
          <select 
            name="estilista" 
            value={formData.estilista} 
            onChange={handleChange} 
            className="input-pripelu"
          >
            <option value="Sin preferencia">Cualquier artista</option>
            <option value="Pri">Pri</option>
            <option value="Ana">Ana</option>
            <option value="Elena">Elena</option>
            <option value="Sofi">Sofi</option>
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
          <select 
            name="hora" 
            value={formData.hora} 
            onChange={handleChange} 
            className="input-pripelu"
            required
          >
            <option value="">Selecciona una hora</option>
            {/* Mañana */}
            <option value="10:00">10:00 AM</option>
            <option value="10:30">10:30 AM</option>
            <option value="11:00">11:00 AM</option>
            <option value="11:30">11:30 AM</option>
            <option value="12:00">12:00 PM</option>
            <option value="12:30">12:30 PM</option>
            <option value="13:00">13:00 PM</option>
            
            {/* Tarde (asumiendo que almuerzan de 14 a 15) */}
            <option value="15:00">15:00 PM</option>
            <option value="15:30">15:30 PM</option>
            <option value="16:00">16:00 PM</option>
            <option value="16:30">16:30 PM</option>
            <option value="17:00">17:00 PM</option>
            <option value="17:30">17:30 PM</option>
            <option value="18:00">18:00 PM</option>
            <option value="18:30">18:30 PM</option>
          </select>
        </div>

        {/* Notas (textarea para que puedan escribir cualquier detalle adicional, como alergias, preferencias, etc.) */}
        <div className="flex flex-col gap-2 md:col-span-2">
          <label className="text-[#b02a6b] font-bold text-sm ml-2">Notas</label>
          <textarea name="notas" value={formData.notas} onChange={handleChange} placeholder="¿Alguna alergia o detalle?" className="input-pripelu h-24 resize-none"></textarea>
        </div>

        <button type="submit" className="md:col-span-2 bg-[#f171ab] text-white py-5 rounded-2xl font-bold text-lg shadow-lg hover:bg-[#d85a94] transition-all mt-4">
          Confirmar Reserva
        </button>
      </form>
    </div>
  );
}
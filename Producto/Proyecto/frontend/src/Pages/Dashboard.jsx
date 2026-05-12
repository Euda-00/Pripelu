import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

export default function Dashboard() {
  const [reservas, setReservas] = useState([]);

  // Cargar las reservas del LocalStorage al entrar
useEffect(() => {
    // Leemos la lista completa
    const datos = localStorage.getItem('listaReservas');
    if (datos) {
      setReservas(JSON.parse(datos)); // Cargamos todo el array de una
    }
  }, []);

  const eliminarReserva = (id) => {
    // 1. Filtramos la lista para quitar la que tenga ese ID
    const nuevaLista = reservas.filter(res => res.id !== id);
    
    // 2. Actualizamos el estado para que desaparezca de la pantalla
    setReservas(nuevaLista);
    
    // 3. Actualizamos el LocalStorage para que el cambio sea permanente
    localStorage.setItem('listaReservas', JSON.stringify(nuevaLista));
    
    // Opcional: Un pequeño aviso
    console.log("Reserva eliminada:", id);
  };

  return (
    <div className="min-h-screen bg-[#fdf2f8] p-8">
      <div className="max-w-6xl mx-auto">
        <header className="flex justify-between items-center mb-10">
          <div>
            <h1 className="text-3xl font-bold text-gray-800 italic">Panel de Control</h1>
            <p className="text-[#f171ab] font-medium">Gestión de Citas - PriPelu Studio</p>
          </div>
          <Link to="/" className="btn-nav flex items-center gap-2">
            <span>🏠</span> Volver al Sitio
          </Link>
        </header>

        <div className="bg-white rounded-[2rem] shadow-xl overflow-hidden border border-pink-100">
          <table className="w-full text-left">
            <thead className="bg-pink-50 text-[#f171ab] uppercase text-xs font-bold">
              <tr>
                <th className="px-6 py-4">Cliente</th>
                <th className="px-6 py-4">Servicio</th>
                <th className="px-6 py-4">Fecha/Hora</th>
                <th className="px-6 py-4">Contacto</th>
                <th className="px-6 py-4 text-center">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-pink-50">
              {reservas.length > 0 ? reservas.map((res, index) => (
                <tr key={index} className="hover:bg-pink-50/30 transition-colors">
                  <td className="px-6 py-4 font-bold text-gray-700">{res.nombre}</td>
                  <td className="px-6 py-4 text-gray-500 italic">{res.servicio}</td>
                  <td className="px-6 py-4 text-gray-600">{res.fecha} - {res.hora}</td>
                  <td className="px-6 py-4 text-gray-500">{res.telefono}</td>
                  <td className="px-6 py-4 text-center">
                    <button 
                      onClick={() => eliminarReserva(res.id)} // <--- Conectamos la función
                      className="bg-red-50 text-red-500 hover:bg-red-500 hover:text-white px-4 py-2 rounded-xl font-bold transition-all text-xs"
                    >
                      Eliminar
                    </button>
                  </td>
                </tr>
              )) : (
                <tr>
                  <td colSpan="5" className="px-6 py-20 text-center text-gray-400 italic">
                    No hay reservas registradas todavía...
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
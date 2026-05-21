import React, { useState, useEffect } from 'react';

export default function Servicios() {
  const [servicios, setServicios] = useState([]);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    const obtenerServicios = async () => {
      try {
        const respuesta = await fetch('http://localhost:8080/api/servicios');
        
        if (respuesta.ok) {
          const datos = await respuesta.json();
          setServicios(datos);
        } else {
          console.error('Error al traer los servicios');
        }
      } catch (error) {
        console.error('Falla en la conexión:', error);
      } finally {
        setCargando(false);
      }
    };

    obtenerServicios();
  }, []);

  return (
    <div className="min-h-screen bg-[#fdf2f8] p-8">
      <div className="max-w-6xl mx-auto">
        <div className="text-center mb-12">
          <h2 className="text-4xl font-serif text-[#b02a6b] italic mb-4">Nuestro Catálogo</h2>
          <p className="text-gray-500 font-medium tracking-widest uppercase">Elige el servicio perfecto para ti</p>
        </div>

        {cargando ? (
          <p className="text-center text-[#f171ab] font-bold text-xl">Cargando la magia...</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {servicios.map((servicio) => (
              <div key={servicio.id_servicio || servicio.id} className="bg-white p-6 rounded-3xl shadow-xl border border-pink-100 flex flex-col justify-between hover:-translate-y-2 transition-transform duration-300">
                <div>
                  <h3 className="text-xl font-bold text-[#b02a6b] mb-2">{servicio.nombre}</h3>
                  <p className="text-gray-500 text-sm mb-4">{servicio.descripcion}</p>
                </div>
                <div className="border-t border-pink-50 pt-4 mt-4">
                  <div className="flex justify-between items-center mb-4">
                    <span className="text-[#f171ab] font-bold text-lg">${servicio.precio}</span>
                    <span className="text-xs bg-pink-100 text-[#b02a6b] px-3 py-1 rounded-full font-bold">
                      {/* Ajustado a duracion_min, aunque podría ser duracion dependiendo del backend */}
                      {servicio.duracion_min || servicio.duracion} min
                    </span>
                  </div>
                  <button className="w-full bg-[#f171ab] text-white py-3 rounded-xl font-bold shadow-md hover:bg-[#d85a94] transition-colors">
                    Reservar
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
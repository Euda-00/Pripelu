import React, { useState, useEffect } from 'react';
import { CheckCircle, Package, DollarSign, ArrowLeft } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function StaffDashboard() {
  const [misCitas, setMisCitas] = useState([]);
  const [comisiones, setComisiones] = useState(0);
  const nombreEmpleado = localStorage.getItem('userName') || "Empleado";

  useEffect(() => {
    const datos = JSON.parse(localStorage.getItem('listaReservas') || "[]");
    const filtradas = datos.filter(cita => cita.estilista === nombreEmpleado);
    setMisCitas(filtradas);

    // RF11: Calcular acumulado de comisiones (Simulado: 50% del valor_total)
    const totalComisiones = filtradas
      .filter(c => c.estado === 'Finalizado')
      .reduce((acc, c) => acc + (parseInt(c.precio.replace('$', '')) * 0.5), 0);
    setComisiones(totalComisiones);
  }, [nombreEmpleado]);

  // RF10: Registro de insumos (Simulado)
  const finalizarServicio = (id) => {
    const insumo = prompt("Ingrese producto utilizado (ej: Laca, Gel, Tinte):");
    if (insumo) {
      const nuevasCitas = misCitas.map(c => 
        c.id === id ? { ...c, estado: 'Finalizado', insumoUsado: insumo } : c
      );
      setMisCitas(nuevasCitas);
      // Actualizamos localStorage para que el Admin también lo vea
      const todas = JSON.parse(localStorage.getItem('listaReservas'));
      const todasActualizadas = todas.map(c => 
        c.id === id ? { ...c, estado: 'Finalizado', insumoUsado: insumo } : c
      );
      localStorage.setItem('listaReservas', JSON.stringify(todasActualizadas));
      alert(`Servicio finalizado. Se descontó stock de: ${insumo}`);
    }
  };

  return (
    <div className="min-h-screen bg-[#fdf2f8] p-8">
      <div className="max-w-6xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <Link to="/" className="text-pink-500 flex items-center gap-2 hover:underline">
            <ArrowLeft size={20} /> Volver al Inicio
          </Link>
          <div className="bg-white px-6 py-3 rounded-2xl shadow-sm border border-pink-100 flex items-center gap-4">
            <DollarSign className="text-green-500" />
            <div>
              <p className="text-[10px] text-gray-400 uppercase font-bold">Mis Comisiones</p>
              <p className="text-lg font-bold text-gray-700">${comisiones.toLocaleString()}</p>
            </div>
          </div>
        </div>

        <h1 className="text-3xl font-serif text-[#b02a6b] mb-8 italic">Agenda de {nombreEmpleado}</h1>

        <div className="grid gap-4">
          {misCitas.map(cita => (
            <div key={cita.id} className={`bg-white p-6 rounded-[2rem] shadow-md border-l-8 flex justify-between items-center ${cita.estado === 'Finalizado' ? 'border-green-400 opacity-75' : 'border-[#f171ab]'}`}>
              <div>
                <p className="text-sm text-gray-400">{cita.fecha} - {cita.hora}</p>
                <h3 className="text-xl font-bold text-gray-800">{cita.nombre}</h3>
                <p className="text-[#f171ab] font-medium">{cita.servicio}</p>
                {cita.insumoUsado && (
                  <p className="text-xs text-blue-500 mt-2 flex items-center gap-1">
                    <Package size={12} /> Insumo: {cita.insumoUsado}
                  </p>
                )}
              </div>

              {cita.estado !== 'Finalizado' && (
                <button 
                  onClick={() => finalizarServicio(cita.id)}
                  className="bg-green-500 text-white px-6 py-3 rounded-2xl font-bold flex items-center gap-2 hover:bg-green-600 transition-all shadow-lg"
                >
                  <CheckCircle size={20} /> Finalizar
                </button>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
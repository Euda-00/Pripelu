import React, { useState, useEffect } from 'react';
import { Package, TrendingDown, ClipboardList, ArrowLeft } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function Dashboard() {
  const [citas, setCitas] = useState([]);
  const [insumos, setInsumos] = useState([]);

  useEffect(() => {
    const datos = JSON.parse(localStorage.getItem('listaReservas') || "[]");
    setCitas(datos);

    // Extraemos solo las citas que tienen insumos registrados
    const historialInsumos = datos
      .filter(cita => cita.insumoUsado)
      .map(cita => ({
        id: cita.id,
        empleado: cita.estilista,
        producto: cita.insumoUsado,
        fecha: cita.fecha,
        servicio: cita.servicio
      }));
    
    setInsumos(historialInsumos);
  }, []);

  return (
    <div className="min-h-screen bg-[#fdf2f8] p-4 md:p-8">
      <div className="max-w-6xl mx-auto">
        
        {/* Header con retorno */}
        <div className="flex justify-between items-center mb-8">
          <Link to="/" className="text-[#f171ab] flex items-center gap-2 font-bold hover:drop-shadow-sm">
            <ArrowLeft size={20} /> Volver al Inicio
          </Link>
          <h1 className="text-2xl font-serif text-[#b02a6b] italic font-bold">Panel de Administración</h1>
        </div>

        {/* Resumen de Insumos (Estética similar al Staff) */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-10">
          <div className="bg-white p-6 rounded-[2rem] shadow-xl border border-pink-100 flex items-center gap-5">
            <div className="bg-orange-100 p-4 rounded-2xl text-orange-500">
              <Package size={30} />
            </div>
            <div>
              <p className="text-xs text-gray-400 uppercase font-black">Productos Usados</p>
              <p className="text-2xl font-bold text-gray-700">{insumos.length}</p>
            </div>
          </div>
          
          <div className="bg-white p-6 rounded-[2rem] shadow-xl border border-pink-100 flex items-center gap-5">
            <div className="bg-pink-100 p-4 rounded-2xl text-[#f171ab]">
              <ClipboardList size={30} />
            </div>
            <div>
              <p className="text-xs text-gray-400 uppercase font-black">Total Citas Hoy</p>
              <p className="text-2xl font-bold text-gray-700">{citas.length}</p>
            </div>
          </div>
        </div>

        {/* TABLA DE INSUMOS UTILIZADOS */}
        <div className="bg-white rounded-[2.5rem] shadow-2xl overflow-hidden border border-pink-50">
          <div className="bg-[#f171ab] p-6">
            <h2 className="text-white font-bold text-xl flex items-center gap-2">
              <TrendingDown size={20} /> Control de Inventario (Salidas)
            </h2>
          </div>
          
          <div className="p-6 overflow-x-auto">
            <table className="w-full text-left">
              <thead>
                <tr className="text-[#b02a6b] border-b border-pink-50">
                  <th className="p-4">Fecha</th>
                  <th className="p-4">Empleado</th>
                  <th className="p-4">Insumo Utilizado</th>
                  <th className="p-4">Servicio Relacionado</th>
                </tr>
              </thead>
              <tbody>
                {insumos.length > 0 ? insumos.map((item) => (
                  <tr key={item.id} className="border-b border-gray-50 hover:bg-pink-50/20 transition-colors">
                    <td className="p-4 text-gray-500 text-sm">{item.fecha}</td>
                    <td className="p-4 font-bold text-gray-700">{item.empleado}</td>
                    <td className="p-4">
                      <span className="bg-orange-50 text-orange-600 px-3 py-1 rounded-full text-xs font-bold border border-orange-100">
                        {item.producto}
                      </span>
                    </td>
                    <td className="p-4 text-gray-600 italic text-sm">{item.servicio}</td>
                  </tr>
                )) : (
                  <tr>
                    <td colSpan="4" className="p-10 text-center text-gray-400 italic">
                      Aún no se han registrado consumos de insumos.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
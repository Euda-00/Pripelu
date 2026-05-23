import React, { useState, useEffect } from 'react';
import { CheckCircle, Package, DollarSign, ArrowLeft } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function StaffDashboard() {
  const [misCitas, setMisCitas] = useState([]);
  const [comisiones, setComisiones] = useState(0);
  const [cargando, setCargando] = useState(true);
  const nombreEmpleado = localStorage.getItem('userName') || "Empleado";

  useEffect(() => {
    const obtenerCitas = async () => {
      try {
        // Hacemos el GET al backend
        const respuesta = await fetch('http://localhost:8080/api/citas');
        
        if (respuesta.ok) {
          const citasBackend = await respuesta.json();
          
          // Filtramos las citas que corresponden al empleado logueado
          // El JSON de Java trae el nombre en cita.empleado.nombre
          const filtradas = citasBackend.filter(cita => 
            cita.empleado && cita.empleado.nombre === nombreEmpleado
          );
          
          setMisCitas(filtradas);

          // Calculamos las comisiones sumando el "valorTotal" de las citas finalizadas
          const totalComisiones = filtradas
            .filter(c => c.estado === 'Finalizado')
            .reduce((acc, c) => acc + (c.valorTotal * 0.5), 0);
            
          setComisiones(totalComisiones);
        } else {
          console.error("Error al obtener las citas del backend");
        }
      } catch (error) {
        console.error("Falla de conexión:", error);
      } finally {
        setCargando(false);
      }
    };

    obtenerCitas();
  }, [nombreEmpleado]);

  // RF10: Registro de insumos
  // Por ahora lo mantendremos simulado en el frontend, 
  // pero lo ideal a futuro es hacer un PUT al backend para cambiar el estado a "Finalizado".
  const finalizarServicio = (id) => {
    const insumo = prompt("Ingrese producto utilizado (ej: Laca, Gel, Tinte):");
    if (insumo) {
      const nuevasCitas = misCitas.map(c => 
        c.id === id ? { ...c, estado: 'Finalizado', insumoUsado: insumo } : c
      );
      setMisCitas(nuevasCitas);
      alert(`Servicio finalizado de forma local. Se descontó stock simulado de: ${insumo}`);
    }
  };

  // Función auxiliar para extraer el nombre del servicio desde el JSON de Java
  const obtenerNombreServicio = (cita) => {
    if (cita.detalles && cita.detalles.length > 0 && cita.detalles[0].servicio) {
      return cita.detalles[0].servicio.nombre;
    }
    return "Servicio no especificado";
  };

  // Función auxiliar para formatear la fecha que viene de Java (ej: "2026-05-22T16:35:15")
  const formatearFechaHora = (fechaHoraString) => {
    if (!fechaHoraString) return { fecha: "Sin fecha", hora: "Sin hora" };
    const date = new Date(fechaHoraString);
    return {
      fecha: date.toLocaleDateString('es-CL'),
      hora: date.toLocaleTimeString('es-CL', { hour: '2-digit', minute: '2-digit' })
    };
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

        {cargando ? (
           <p className="text-center text-[#f171ab] font-bold text-xl py-10">Cargando agenda...</p>
        ) : misCitas.length === 0 ? (
           <div className="bg-white p-10 rounded-[2rem] text-center shadow-sm border border-pink-100">
             <p className="text-gray-500 font-medium">No tienes citas agendadas por el momento. ¡Tómate un café! ☕</p>
           </div>
        ) : (
          <div className="grid gap-4">
            {misCitas.map(cita => {
              const { fecha, hora } = formatearFechaHora(cita.fechaHora);
              return (
                <div key={cita.id} className={`bg-white p-6 rounded-[2rem] shadow-md border-l-8 flex justify-between items-center ${cita.estado === 'Finalizado' ? 'border-green-400 opacity-75' : 'border-[#f171ab]'}`}>
                  <div>
                    <p className="text-sm text-gray-400">{fecha} - {hora}</p>
                    <h3 className="text-xl font-bold text-gray-800">
                      {cita.usuario ? `${cita.usuario.nombre} ${cita.usuario.apellido || ''}` : 'Cliente Sin Nombre'}
                    </h3>
                    <p className="text-[#f171ab] font-medium">{obtenerNombreServicio(cita)}</p>
                    {cita.insumoUsado && (
                      <p className="text-xs text-blue-500 mt-2 flex items-center gap-1">
                        <Package size={12} /> Insumo local: {cita.insumoUsado}
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
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}
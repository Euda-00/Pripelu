import React, { useState, useEffect } from 'react';
import { CheckCircle, Package, DollarSign, ArrowLeft, XCircle, Calendar, User, Scissors } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function MisCitas() {
  const [misCitas, setMisCitas] = useState([]);
  const [comisiones, setComisiones] = useState(0);
  const [cargando, setCargando] = useState(true);
  
  const userRole = localStorage.getItem('userRole') || 'cliente';
  const userName = localStorage.getItem('userName') || 'Usuario';
  const userId = parseInt(localStorage.getItem('userId')) || 0;

  useEffect(() => {
    const obtenerCitas = async () => {
      try {
        const respuesta = await fetch('http://localhost:8080/api/citas');
        
        if (respuesta.ok) {
          const citasBackend = await respuesta.json();
          let filtradas = [];

          // FILTRO INTELIGENTE SEGÚN TU ROL
          if (userRole === 'empleado') {
            // 🚨 EL ARREGLO: Filtramos por ID o por el NOMBRE (ignorando mayúsculas por si acaso)
            filtradas = citasBackend.filter(cita => 
              cita.empleado?.id === userId || 
              cita.empleado?.id_empleado === userId ||
              cita.empleado?.nombre?.toLowerCase() === userName.toLowerCase()
            );
            
            // Calculamos comisiones solo para empleados (50% de las citas finalizadas)
            const total = filtradas
              .filter(c => c.estado?.toLowerCase() === 'finalizado')
              .reduce((acc, c) => acc + (c.valorTotal * 0.5), 0);
            setComisiones(total);

          } else {
            // Si es cliente, filtramos por su ID de usuario
            filtradas = citasBackend.filter(cita => cita.usuario?.id === userId || cita.usuario?.id_usuario === userId);
          }
          
          setMisCitas(filtradas);
        }
      } catch (error) {
        console.error("Falla de conexión:", error);
      } finally {
        setCargando(false);
      }
    };

    obtenerCitas();
  }, [userRole, userId]);

  // --- FUNCIÓN PUT (LA DIETA DEL JSON) PARA ACTUALIZAR EN LA BD REAL ---
  const actualizarEstado = async (id, nuevoEstado, notaExtra = null) => {
    const citaActual = misCitas.find(c => c.id === id);
    if (!citaActual) return;

    const notasFinales = notaExtra ? `${citaActual.notas || ''} | Insumo: ${notaExtra}` : citaActual.notas;

    const citaLimpia = {
      id: citaActual.id,
      fechaHora: citaActual.fechaHora,
      estado: nuevoEstado, 
      notas: notasFinales,
      valorTotal: citaActual.valorTotal,
      duracionTotal: citaActual.duracionTotal,
      usuario: { id: citaActual.usuario?.id || citaActual.usuario?.id_usuario },
      empleado: { id: citaActual.empleado?.id || citaActual.empleado?.id_empleado },
      detalles: citaActual.detalles?.map(d => ({
        id: d.id,
        precioCita: d.precioCita,
        servicio: { id: d.servicio?.id || d.servicio?.id_servicio }
      }))
    };

    try {
      const respuesta = await fetch(`http://localhost:8080/api/citas/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(citaLimpia)
      });

      if (respuesta.ok) {
        setMisCitas(misCitas.map(c => c.id === id ? { ...c, estado: nuevoEstado, notas: notasFinales } : c));
      } else {
        alert("Hubo un error al actualizar la reserva en el servidor.");
      }
    } catch (error) {
      console.error("Falla de red:", error);
    }
  };

  const manejarFinalizacion = (id) => {
    const insumo = prompt("Opcional: Ingrese producto utilizado (ej: Laca, Tinte) o deje en blanco:");
    actualizarEstado(id, 'Finalizado', insumo);
  };

  const obtenerNombreServicio = (cita) => {
    return cita.detalles?.[0]?.servicio?.nombre || "Servicio no especificado";
  };

  return (
    <div className="min-h-screen bg-[#fdf2f8] p-4 md:p-8">
      <div className="max-w-4xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <Link to="/" className="text-[#f171ab] flex items-center gap-2 hover:underline font-bold">
            <ArrowLeft size={20} /> Volver al Inicio
          </Link>
          
          {/* Solo mostramos comisiones si es empleado */}
          {userRole === 'empleado' && (
            <div className="bg-white px-6 py-3 rounded-2xl shadow-sm border border-pink-100 flex items-center gap-4">
              <DollarSign className="text-green-500" />
              <div>
                <p className="text-[10px] text-gray-400 uppercase font-bold">Mis Comisiones</p>
                <p className="text-lg font-bold text-gray-700">${comisiones.toLocaleString()}</p>
              </div>
            </div>
          )}
        </div>

        <h1 className="text-3xl font-serif text-[#b02a6b] mb-8 italic">
          {userRole === 'empleado' ? `Agenda de ${userName}` : `Mis Reservas, ${userName}`}
        </h1>

        {cargando ? (
           <p className="text-center text-[#f171ab] font-bold text-xl py-10">Cargando datos...</p>
        ) : misCitas.length === 0 ? (
           <div className="bg-white p-10 rounded-[2rem] text-center shadow-sm border border-pink-100">
             <p className="text-gray-500 font-medium">
               {userRole === 'empleado' ? "No tienes citas agendadas por el momento. ¡Tómate un café! ☕" : "Aún no tienes reservas con nosotros. ¡Anímate a un cambio de look! ✨"}
             </p>
           </div>
        ) : (
          <div className="grid gap-6">
            {misCitas.map(cita => (
              <div key={cita.id} className={`bg-white p-6 rounded-[2rem] shadow-md border-l-8 flex flex-col md:flex-row justify-between md:items-center gap-4 ${cita.estado?.toLowerCase() === 'finalizado' ? 'border-green-400 opacity-75' : cita.estado?.toLowerCase() === 'cancelado' ? 'border-red-400 opacity-50' : 'border-[#f171ab]'}`}>
                
                <div className="space-y-3">
                  <div className="flex items-center gap-2 text-gray-500 bg-gray-50 inline-block px-3 py-1 rounded-lg">
                    <Calendar size={16} className="text-[#f171ab]" />
                    <span className="text-sm font-bold">
                      {new Date(cita.fechaHora).toLocaleDateString('es-CL')} - {new Date(cita.fechaHora).toLocaleTimeString('es-CL', { hour: '2-digit', minute: '2-digit' })}
                    </span>
                  </div>
                  
                  <div>
                    <p className="text-[10px] text-gray-400 uppercase font-bold">
                      {userRole === 'empleado' ? 'Atender a:' : 'Estilista asignado:'}
                    </p>
                    <h3 className="text-xl font-bold text-gray-800 flex items-center gap-2">
                      {userRole === 'empleado' 
                        ? <><User size={18} className="text-[#f171ab]"/> {cita.usuario?.nombre} {cita.usuario?.apellido}</>
                        : <><Scissors size={18} className="text-[#f171ab]"/> {cita.empleado?.nombre} {cita.empleado?.apellido}</>
                      }
                    </h3>
                  </div>

                  <p className="text-[#b02a6b] font-bold italic">{obtenerNombreServicio(cita)}</p>
                  
                  <div className="flex gap-2">
                    <span className={`text-xs font-bold px-3 py-1 rounded-full ${cita.estado?.toLowerCase() === 'pendiente' ? 'bg-amber-100 text-amber-700' : cita.estado?.toLowerCase() === 'finalizado' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                      {cita.estado}
                    </span>
                  </div>
                </div>

                {/* BOTONES INTELIGENTES SEGÚN ROL Y ESTADO */}
                {cita.estado?.toLowerCase() === 'pendiente' && (
                  <div className="flex shrink-0">
                    {userRole === 'empleado' ? (
                      <button onClick={() => manejarFinalizacion(cita.id)} className="bg-green-500 text-white px-6 py-3 rounded-2xl font-bold flex items-center gap-2 hover:bg-green-600 transition-all shadow-lg w-full md:w-auto justify-center">
                        <CheckCircle size={20} /> Finalizar Cita
                      </button>
                    ) : (
                      <button onClick={() => actualizarEstado(cita.id, 'Cancelado')} className="bg-red-50 text-red-500 border border-red-200 px-6 py-3 rounded-2xl font-bold flex items-center gap-2 hover:bg-red-100 transition-all w-full md:w-auto justify-center">
                        <XCircle size={20} /> Cancelar Reserva
                      </button>
                    )}
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
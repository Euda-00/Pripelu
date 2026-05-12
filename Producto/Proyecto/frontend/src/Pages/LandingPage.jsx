import React from 'react';
import { motion } from 'framer-motion';
import { Play, MapPin, Phone, Clock } from 'lucide-react';
import { serviciosData, equipoData, floresData } from '../data';
import { TarjetaEquipo } from '../components/TarjetasEquipo';
import { FloatingFlower } from '../components/Flores';
import { Comparador } from '../components/Comparador';
import '../styles/pripelu.css'; // Importamos tus nuevas clases
import Dashboard from './Dashboard';
import { Link } from 'react-router-dom';

export default function LandingPage({ onStartBooking }) {
  return (
    <div className="min-h-screen bg-[#fdf2f8] relative overflow-x-hidden font-sans">
      
      {/* 1. DECORACIÓN DE FONDO */}
      {floresData.map((f) => (
        <FloatingFlower key={f.id} {...f} />
      ))}

      {/* 2. NAVEGACIÓN */}
      <nav className="relative z-20 flex justify-between items-center px-8 md:px-12 py-6 bg-white/30 backdrop-blur-md sticky top-0 border-b border-pink-100">
        <div className="flex items-center gap-3">
          <div className="bg-white p-2 rounded-full shadow-sm">
            <img src="/logo-pripelu.png" alt="Logo" className="w-8" />
          </div>
          <span className="text-2xl font-bold text-[#f171ab]">PriPelu</span>
        </div>
        
        <div className="hidden md:flex gap-10 text-gray-500 font-medium">
          {['Inicio', 'Servicios', 'Equipo', 'Galería'].map((item) => (
            <a key={item} href={`#${item.toLowerCase()}`} className="hover:text-[#f171ab] transition-colors">
              {item}
            </a>
          ))}
        </div>

        <button onClick={onStartBooking} className="btn-nav">
          Reservar Cita
        </button>

        
      </nav>

      {/* 3. HERO SECTION */}
      <header className="relative z-10 flex flex-col items-center justify-center min-h-[90vh] text-center px-4 pt-10">
        <motion.div 
          initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}
          className="bg-pink-100/60 backdrop-blur-sm text-[#f171ab] px-5 py-1 rounded-full text-xs font-bold mb-8 flex items-center gap-2"
        >
          ✨ Experiencia Premium en Belleza
        </motion.div>
       {/* BOTÓN TEMPORAL AL ADMIN */}
        <Link 
          to="/admin" 
          className="bg-gray-100 hover:bg-gray-200 text-gray-500 px-4 py-2 rounded-lg transition-all border border-dashed border-gray-300"
        >
          ⚙️ Acceso Admin (Temporal)
        </Link>
  
        <motion.img
          initial={{ opacity: 0, scale: 0.9 }} animate={{ opacity: 1, scale: 1 }}
          src="/logo-pripelu-gold.png" alt="PriPelu Gold"
          className="w-[280px] md:w-[500px] drop-shadow-2xl mb-10"
        />

        <p className="max-w-2xl text-gray-600 text-lg md:text-xl font-medium mb-12">
          Donde tu estilo cobra vida. Transformamos tu imagen con técnicas innovadoras y productos de alta gama.
        </p>

        <div className="flex flex-col md:flex-row gap-5 mb-16">
          <button onClick={onStartBooking} className="btn-primary">
            Reserva tu Transformación
          </button>
          <button className="btn-secondary">
            <div className="bg-[#f171ab] p-1 rounded-full text-white"><Play size={16} fill="white" /></div>
            Ver Video
          </button>
        </div>

        {/* Scroll Indicator */}
        <div className="mt-auto pb-10">
          <motion.div animate={{ y: [0, 10, 0] }} transition={{ repeat: Infinity, duration: 2 }} className="border-2 border-pink-300 w-7 h-12 rounded-full flex justify-center p-1.5">
            <motion.div animate={{ y: [0, 15, 0] }} transition={{ repeat: Infinity, duration: 1.5 }} className="w-1 h-3 bg-pink-400 rounded-full" />
          </motion.div>
        </div>
      </header>

      {/* 4. SECCIÓN SERVICIOS */}
      <section id="servicios" className="section-container bg-white rounded-t-[4rem] shadow-2xl">
        <div className="section-title-wrapper">
          <p className="section-subtitle">Nuestros Servicios</p>
          <h2 className="section-title">Experiencias Únicas</h2>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-10 max-w-7xl mx-auto">
          {serviciosData.map((s) => (
            <motion.div key={s.id} whileHover={{ y: -10 }} className="card-pripelu group">
              <div className="w-16 h-16 bg-pink-50 rounded-2xl flex items-center justify-center text-[#f171ab] mb-6 group-hover:bg-[#f171ab] group-hover:text-white transition-all">
                {React.cloneElement(s.icon, { size: 28 })}
              </div>
              <h3 className="text-2xl font-bold text-gray-800 mb-3">{s.nombre}</h3>
              <p className="text-gray-500 text-sm italic mb-8">{s.desc}</p>
              <div className="flex justify-between items-center border-t border-pink-50 pt-8">
                <span className="text-[#f171ab] font-bold text-xl italic">Desde {s.precio}</span>
                <button className="text-[#f171ab] hover:translate-x-2 transition-transform"><Play size={18} fill="currentColor" /></button>
              </div>
            </motion.div>
          ))}
        </div>
      </section>

      {/* 5. SECCIÓN EQUIPO */}
      <section id="equipo" className="section-container bg-[#fdf2f8]">
        <div className="section-title-wrapper">
          <p className="section-subtitle">Profesionales</p>
          <h2 className="section-title text-[#f171ab]">Artistas del Cabello</h2>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-12 max-w-7xl mx-auto">
          {equipoData.map((persona) => (
            <TarjetaEquipo key={persona.id} persona={persona} />
          ))}
        </div>
      </section>

      {/* 6. GALERÍA (ANTES/DESPUÉS) */}
      <section id="galería" className="section-container bg-white">
        <div className="section-title-wrapper">
          <p className="section-subtitle">Resultados Reales</p>
          <h2 className="section-title">Transformaciones Mágicas</h2>
        </div>
        <Comparador antes="/tu-foto-antes.jpg" despues="/tu-foto-despues.jpg" />
      </section>

      {/* 7. CONTACTO Y UBICACIÓN */}
      <section className="section-container bg-[#fdf2f8]">
        <div className="max-w-4xl mx-auto bg-white rounded-[3rem] shadow-xl overflow-hidden border border-pink-100 flex flex-col md:flex-row">
          <div className="p-12 md:w-1/2">
            <p className="section-subtitle">Encuéntranos</p>
            <h2 className="text-3xl font-bold text-gray-800 mb-8 italic">Visítanos en Maipú</h2>
            
            <div className="space-y-6">
              <ContactInfo icon={<MapPin size={20}/>} title="Dirección" content="Calle Falsa 123, Maipú, Santiago." />
              <ContactInfo icon={<Phone size={20}/>} title="WhatsApp" content="+56 9 1234 5678" />
              <ContactInfo icon={<Clock size={20}/>} title="Horario" content="Mar - Sáb: 10:00 - 19:00 hrs" />
            </div>
          </div>

          <div className="bg-[#f171ab] p-12 md:w-1/2 flex flex-col items-center justify-center text-center text-white">
            <h3 className="text-2xl font-bold mb-4 italic">¿Lista para un cambio?</h3>
            <p className="text-pink-100 mb-8 text-sm leading-relaxed">Asegura tu atención personalizada hoy mismo.</p>
            <button onClick={onStartBooking} className="bg-white text-[#f171ab] px-10 py-4 rounded-full font-bold shadow-lg hover:bg-pink-50 transition-all">
              Agendar ahora
            </button>
          </div>
        </div>
      </section>

      {/* 8. FOOTER */}
      <footer className="bg-white py-12 text-center text-gray-400 text-xs border-t border-pink-50">
        <p>© 2026 PriPelu Studio. Maipú, Santiago. Diseñado con ✨</p>
      </footer>
    </div>
  );
}

// Sub-componente para limpiar la sección de contacto
const ContactInfo = ({ icon, title, content }) => (
  <div className="flex items-start gap-4">
    <div className="bg-pink-50 p-3 rounded-2xl text-[#f171ab]">{icon}</div>
    <div>
      <h4 className="font-bold text-gray-800">{title}</h4>
      <p className="text-gray-500 text-sm italic">{content}</p>
    </div>
  </div>
);
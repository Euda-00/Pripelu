import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

export default function Register() {
  const [formData, setFormData] = useState({
    nombre: '',
    email: '',
    password: '',
    confirmPassword: ''
  });
  const navigate = useNavigate();

  const handleRegister = (e) => {
    e.preventDefault();
    
    if (formData.password !== formData.confirmPassword) {
      alert("Las contraseñas no coinciden");
      return;
    }

    // Aquí irá el fetch a tu base de datos MySQL más adelante
    console.log("Registrando usuario:", formData);
    alert("Cuenta creada con éxito (simulado)");
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-[#fdf2f8] flex items-center justify-center p-4">
      <div className="bg-white p-10 rounded-[3rem] shadow-2xl w-full max-w-md border border-pink-100">
        <div className="text-center mb-8">
          <h2 className="text-3xl font-serif text-[#b02a6b] italic mb-2">Únete a PriPelu</h2>
          <p className="text-gray-400 text-sm font-medium uppercase tracking-widest">Crea tu cuenta para agendar</p>
        </div>

        <form onSubmit={handleRegister} className="flex flex-col gap-4">
          <input 
            type="text" 
            placeholder="Nombre Completo"
            className="p-4 rounded-2xl border border-pink-100 outline-none focus:border-[#f171ab] bg-pink-50/30"
            onChange={(e) => setFormData({...formData, nombre: e.target.value})}
            required
          />
          <input 
            type="email" 
            placeholder="Email"
            className="p-4 rounded-2xl border border-pink-100 outline-none focus:border-[#f171ab] bg-pink-50/30"
            onChange={(e) => setFormData({...formData, email: e.target.value})}
            required
          />
          <input 
            type="password" 
            placeholder="Contraseña"
            className="p-4 rounded-2xl border border-pink-100 outline-none focus:border-[#f171ab] bg-pink-50/30"
            onChange={(e) => setFormData({...formData, password: e.target.value})}
            required
          />
          <input 
            type="password" 
            placeholder="Confirmar Contraseña"
            className="p-4 rounded-2xl border border-pink-100 outline-none focus:border-[#f171ab] bg-pink-50/30"
            onChange={(e) => setFormData({...formData, confirmPassword: e.target.value})}
            required
          />

          <button type="submit" className="bg-[#f171ab] text-white py-4 rounded-2xl font-bold text-lg shadow-lg hover:bg-[#d85a94] transition-all mt-2">
            Registrarme
          </button>
        </form>

        <p className="text-center mt-6 text-gray-500 text-sm">
          ¿Ya tienes cuenta? <Link to="/login" className="text-[#f171ab] font-bold">Inicia sesión aquí</Link>
        </p>
      </div>
    </div>
  );
}
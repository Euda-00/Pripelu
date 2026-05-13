import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    
    // Lógica temporal (Mock) hasta que conectes tu DB
    if (email === 'admin@pripelu.cl' && password === '123456') {
      localStorage.setItem('isAuthenticated', 'true');
      navigate('/admin'); // Nos vamos al dashboard
    } else {
      alert('Credenciales incorrectas.');
    }
  };

  return (
    <div className="min-h-screen bg-[#fdf2f8] flex items-center justify-center p-4">
      <div className="bg-white p-10 rounded-[3rem] shadow-2xl w-full max-w-md border border-pink-100">
        <div className="text-center mb-8">
          <h2 className="text-3xl font-serif text-[#b02a6b] italic mb-2">Bienvenida, Pri</h2>
          <p className="text-gray-400 text-sm font-medium uppercase tracking-widest">Inicia sesión para gestionar</p>
        </div>

        <form onSubmit={handleLogin} className="flex flex-col gap-5">
          <div className="flex flex-col gap-2">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Email</label>
            <input 
              type="email" 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="p-4 rounded-2xl border border-pink-100 outline-none focus:border-[#f171ab] bg-pink-50/30"
              placeholder="tu@email.com"
            />
          </div>

          <div className="flex flex-col gap-2">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Contraseña</label>
            <input 
              type="password" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="p-4 rounded-2xl border border-pink-100 outline-none focus:border-[#f171ab] bg-pink-50/30"
              placeholder="••••••••"
            />
          </div>

          <button 
            type="submit"
            className="bg-[#f171ab] text-white py-4 rounded-2xl font-bold text-lg shadow-lg hover:bg-[#d85a94] transition-all mt-4"
          >
            Entrar al Panel
          </button>
          <p className="text-center mt-6 text-gray-500 text-sm">
            ¿No tienes cuenta? <a href="/register" className="text-[#f171ab] font-bold">Regístrate aquí</a>
          </p>
        </form>
      </div>
    </div>
  );
}
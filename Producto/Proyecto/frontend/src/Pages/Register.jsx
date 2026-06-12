import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Register() {
  const [nombre, setNombre] = useState('');
  const [apellido, setApellido] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [telefono, setTelefono] = useState('');
  const [errorMensaje, setErrorMensaje] = useState(''); // <-- Nuevo estado para mostrar errores bonitos
  
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    setErrorMensaje(''); // Limpiamos errores anteriores al volver a intentar

    // ==========================================
    // 🛡️ VALIDACIONES DE DATOS
    // ==========================================

    // 1. Campos vacíos
    if (!nombre.trim() || !apellido.trim() || !email.trim() || !password.trim() || !telefono.trim()) {
      setErrorMensaje('Por favor, rellena todos los campos obligatorios.');
      return;
    }

    // 2. Nombre y Apellido (solo letras y espacios)
    const letrasRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
    if (!letrasRegex.test(nombre) || !letrasRegex.test(apellido)) {
      setErrorMensaje('El nombre y apellido solo deben contener letras.');
      return;
    }

    // 3. Email (debe tener @ y un dominio)
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setErrorMensaje('Ingresa un correo electrónico válido (ejemplo: tu@email.com).');
      return;
    }

    // 4. Teléfono (Formato chileno: 9 dígitos o con +569)
    const telefonoLimpio = telefono.replace(/\s/g, ''); // Quitamos espacios si los puso
    const telRegex = /^(\+?56)?9\d{8}$/;
    if (!telRegex.test(telefonoLimpio)) {
      setErrorMensaje('Ingresa un celular válido (ej: +56912345678 o 912345678).');
      return;
    }

    // 5. Contraseña (mínimo 6 caracteres)
    if (password.length < 6) {
      setErrorMensaje('La contraseña debe tener al menos 6 caracteres por seguridad.');
      return;
    }

    // ==========================================
    // 🚀 ENVÍO AL BACKEND
    // ==========================================
    try {
      const respuesta = await fetch('http://localhost:8080/api/usuarios', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nombre: nombre.trim(),
          apellido: apellido.trim(),
          email: email.trim(),
          contrasena: password, 
          telefono: telefonoLimpio,
          rol: 'cliente' 
        }),
      });

      if (respuesta.ok) {
        alert('¡Cuenta creada con éxito, máquina! Ahora inicia sesión.');
        navigate('/login'); 
      } else {
        const errorData = await respuesta.json().catch(() => ({}));
        setErrorMensaje(errorData.message || 'El correo ya está registrado o hubo un error.');
      }
    } catch (error) {
      console.error('Error en el registro:', error);
      setErrorMensaje('Hubo un problema de red al conectar con el servidor.');
    }
  };

  return (
    <div className="min-h-screen bg-[#fdf2f8] flex items-center justify-center p-4">
      <div className="bg-white p-10 rounded-[3rem] shadow-2xl w-full max-w-md border border-pink-100">
        <div className="text-center mb-6">
          <h2 className="text-3xl font-serif text-[#b02a6b] italic mb-2">Únete a PriPelu</h2>
          <p className="text-gray-400 text-sm font-medium uppercase tracking-widest">Crea tu cuenta de cliente</p>
        </div>

        <form onSubmit={handleRegister} className="flex flex-col gap-4">
          <div className="flex gap-3">
            <div className="flex flex-col gap-1 w-1/2">
              <label className="text-[#b02a6b] font-bold text-xs ml-2">Nombre</label>
              <input 
                type="text" 
                value={nombre}
                onChange={(e) => setNombre(e.target.value)}
                className="p-3 rounded-xl border border-pink-100 outline-none focus:border-[#f171ab] bg-pink-50/30 text-sm"
                placeholder="Tu nombre"
              />
            </div>
            <div className="flex flex-col gap-1 w-1/2">
              <label className="text-[#b02a6b] font-bold text-xs ml-2">Apellido</label>
              <input 
                type="text" 
                value={apellido}
                onChange={(e) => setApellido(e.target.value)}
                className="p-3 rounded-xl border border-pink-100 outline-none focus:border-[#f171ab] bg-pink-50/30 text-sm"
                placeholder="Tu apellido"
              />
            </div>
          </div>

          <div className="flex flex-col gap-1">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Email</label>
            <input 
              type="email" 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="p-3 rounded-xl border border-pink-100 outline-none focus:border-[#f171ab] bg-pink-50/30"
              placeholder="tu@email.com"
            />
          </div>

          <div className="flex flex-col gap-1">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Teléfono</label>
            <input 
              type="tel" 
              value={telefono}
              onChange={(e) => setTelefono(e.target.value)}
              className="p-3 rounded-xl border border-pink-100 outline-none focus:border-[#f171ab] bg-pink-50/30"
              placeholder="+56912345678"
            />
          </div>

          <div className="flex flex-col gap-1">
            <label className="text-[#b02a6b] font-bold text-sm ml-2">Contraseña</label>
            <input 
              type="password" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="p-3 rounded-xl border border-pink-100 outline-none focus:border-[#f171ab] bg-pink-50/30"
              placeholder="••••••••"
            />
          </div>

          {/* MENSAJE DE ERROR EN PANTALLA */}
          {errorMensaje && (
            <p className="text-red-500 text-xs text-center font-bold px-2">
              {errorMensaje}
            </p>
          )}

          <button 
            type="submit"
            className="bg-[#f171ab] text-white py-3 rounded-xl font-bold text-lg shadow-lg hover:bg-[#d85a94] transition-all mt-3"
          >
            Registrarse
          </button>
          
          <p className="text-center mt-4 text-gray-500 text-sm">
            ¿Ya tienes una cuenta? <a href="/login" className="text-[#f171ab] font-bold">Inicia sesión aquí</a>
          </p>
        </form>
      </div>
    </div>
  );
}
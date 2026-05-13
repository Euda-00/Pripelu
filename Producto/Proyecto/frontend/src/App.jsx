import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LandingPage from './Pages/LandingPage';
import Dashboard from './Pages/Dashboard';
import BookingForm from './Pages/BookingForm'; 
import Login from './Pages/Login';
import ProtectedRoute from './components/ProtectedRute';
import Register from './Pages/Register';


function App() {
  const [showModal, setShowModal] = useState(false);

  // Esta es la función que abre el modal
  const handleOpenModal = () => {
  const isAuth = localStorage.getItem('isAuthenticated') === 'true';
  
  if (isAuth) {
    setShowModal(true); // Si está logueado, abre el form
  } else {
    alert("Para agendar tu cita, primero debes iniciar sesión o crear una cuenta ✨");
    navigate('/login'); // Si no, mándalo a loguearse
  }
  };
  
  // Esta lo cierra cuando terminan
  const handleCloseModal = () => setShowModal(false);

  return (
    <Router>
      <Routes>
        {/* PASO CLAVE: Pasamos la función a la LandingPage */}
        <Route path="/" element={<LandingPage onStartBooking={handleOpenModal} />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route 
          path="/admin"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
        }/>
      </Routes>

      
      {showModal && (
      <div className="fixed inset-0 z-[100] flex items-center justify-center bg-black/40 backdrop-blur-sm p-4">
        <BookingForm 
          onBookingComplete={handleCloseModal} 
          onClose={handleCloseModal} // <--- Pasamos la función aquí
        />
      </div>
    )}
    </Router>
  );
}

export default App;
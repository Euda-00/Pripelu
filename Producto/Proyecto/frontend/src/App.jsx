import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LandingPage from './Pages/LandingPage';
import Dashboard from './Pages/Dashboard';
import BookingForm from './Pages/BookingForm'; // Importa tu form

function App() {
  const [showModal, setShowModal] = useState(false);

  // Esta es la función que abre el modal
  const handleOpenModal = () => setShowModal(true);
  
  // Esta lo cierra cuando terminan
  const handleCloseModal = () => setShowModal(false);

  return (
    <Router>
      <Routes>
        {/* PASO CLAVE: Pasamos la función a la LandingPage */}
        <Route path="/" element={<LandingPage onStartBooking={handleOpenModal} />} />
        <Route path="/admin" element={<Dashboard />} />
      </Routes>

      /* MOSTRAMOS EL FORMULARIO COMO UN MODAL SOBRE LA LANDING PAGE */
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
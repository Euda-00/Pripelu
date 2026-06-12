import React from 'react';
import { describe, test, expect, afterEach, vi } from 'vitest';
import { render, screen, cleanup, waitFor, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';

import BookingForm from "../Pages/BookingForm.jsx"; 

afterEach(() => {
  cleanup();
  vi.restoreAllMocks();
});

describe('Pruebas Funcionales - Formulario de Reservas', () => {

  test('CP-09: Debe bloquear las fechas en el pasado asignando la fecha de hoy como mínima', async () => {
    // Fingimos que carga rápido para no esperar
    global.fetch = vi.fn(() => Promise.resolve({ ok: true, json: () => Promise.resolve([]) }));

    render(<BookingForm onBookingComplete={() => {}} onClose={() => {}} />);

    await waitFor(() => {
      expect(screen.queryByText(/Conectando con el sistema/i)).not.toBeInTheDocument();
    });

    const fechaActual = new Date();
    const anio = fechaActual.getFullYear();
    const mes = String(fechaActual.getMonth() + 1).padStart(2, '0');
    const dia = String(fechaActual.getDate()).padStart(2, '0');
    const fechaMinimaEsperada = `${anio}-${mes}-${dia}`;

    const inputFecha = document.querySelector('input[type="date"]');
    expect(inputFecha).toBeInTheDocument();
    expect(inputFecha).toHaveAttribute('min', fechaMinimaEsperada);
  });

  test('CP-10: Debe bloquear la reserva mostrando "❌ Sin horas" si se selecciona un día no laborable (ej: Domingo)', async () => {
    
    // 1. EL TRUCO MAESTRO: Simulamos un backend completo, pero el empleado SOLO trabaja los Lunes (diaSemana: 1)
    global.fetch = vi.fn((url) => {
      if (url.includes('/api/servicios')) {
        return Promise.resolve({ ok: true, json: () => Promise.resolve([{ id: 1, nombre: 'Corte', precio: 15000 }]) });
      }
      if (url.includes('/api/empleado')) {
        return Promise.resolve({ ok: true, json: () => Promise.resolve([{ id: 1, nombre: 'Ana', apellido: 'Gómez' }]) });
      }
      if (url.includes('/api/horarios')) {
        // Le damos horario SOLO para el Lunes (1)
        return Promise.resolve({ ok: true, json: () => Promise.resolve([
          { empleado: { id: 1 }, diaSemana: 1, horaInicio: '10:00', horacierre: '18:00' }
        ]) });
      }
      return Promise.resolve({ ok: true, json: () => Promise.resolve([]) });
    });

    render(<BookingForm onBookingComplete={() => {}} onClose={() => {}} />);

    // Esperamos a que la pantalla cargue
    await waitFor(() => {
      expect(screen.queryByText(/Conectando con el sistema/i)).not.toBeInTheDocument();
    });

    // 2. Simulamos que el usuario interactúa con el formulario
    const selectServicio = document.querySelector('select[name="servicioId"]');
    const selectEmpleado = document.querySelector('select[name="empleadoId"]');
    const inputFecha = document.querySelector('input[type="date"]');

    fireEvent.change(selectServicio, { target: { value: '1' } });
    fireEvent.change(selectEmpleado, { target: { value: '1' } });
    
    // ATENCIÓN AQUÍ: Elegimos el 14 de Junio de 2026, que cae día DOMINGO
    fireEvent.change(inputFecha, { target: { value: '2026-06-14' } });

    // 3. RESULTADOS ESPERADOS (Según el Excel de QA)
    // El sistema debe detectar que no hay turnos el domingo y mostrar "❌ Sin horas"
    await waitFor(() => {
      expect(screen.getByText(/❌ Sin horas/i)).toBeInTheDocument();
    });

    // Y como no hay horas, el botón para ir a pagar debe estar completamente deshabilitado
    const btnContinuar = screen.getByRole('button', { name: /Continuar al Pago/i });
    expect(btnContinuar).toBeDisabled();
  });

  test('CP-11: Debe excluir el bloque de almuerzo de las horas disponibles', async () => {
    // 1. EL TRUCO: Simulamos un Lunes laboral, pero con colación de 14:00 a 15:00
    global.fetch = vi.fn((url) => {
      if (url.includes('/api/servicios')) {
        return Promise.resolve({ ok: true, json: () => Promise.resolve([{ id: 1, nombre: 'Tintura', precio: 25000 }]) });
      }
      if (url.includes('/api/empleado')) {
        return Promise.resolve({ ok: true, json: () => Promise.resolve([{ id: 1, nombre: 'Ana', apellido: 'Gómez' }]) });
      }
      if (url.includes('/api/horarios')) {
        return Promise.resolve({ ok: true, json: () => Promise.resolve([
          { 
            empleado: { id: 1 }, 
            diaSemana: 1, // Lunes
            horaInicio: '10:00', 
            horacierre: '18:00',
            horaInicioAlmuerzo: '14:00', // <-- Aquí está la trampa
            horaFinAlmuerzo: '15:00'
          }
        ]) });
      }
      return Promise.resolve({ ok: true, json: () => Promise.resolve([]) });
    });

    render(<BookingForm onBookingComplete={() => {}} onClose={() => {}} />);

    // Esperamos a que la pantalla cargue
    await waitFor(() => {
      expect(screen.queryByText(/Conectando con el sistema/i)).not.toBeInTheDocument();
    });

    const selectServicio = document.querySelector('select[name="servicioId"]');
    const selectEmpleado = document.querySelector('select[name="empleadoId"]');
    const inputFecha = document.querySelector('input[type="date"]');

    // 2. Interactuamos: Elegimos el 15 de Junio de 2026 (que cae Lunes)
    fireEvent.change(selectServicio, { target: { value: '1' } });
    fireEvent.change(selectEmpleado, { target: { value: '1' } });
    fireEvent.change(inputFecha, { target: { value: '2026-06-15' } });

    // 3. RESULTADOS ESPERADOS (Según Excel)
    await waitFor(() => {
      const selectHora = document.querySelector('select[name="hora"]');
      expect(selectHora.options.length).toBeGreaterThan(1);
    });

    // Verificamos que la hora de almuerzo (14:00) haya sido ELIMINADA de las opciones
    const opcionAlmuerzo = screen.queryByRole('option', { name: '14:00' });
    expect(opcionAlmuerzo).not.toBeInTheDocument();

    // Verificamos que las horas de los costados (13:00 y 15:00) SÍ existan para agendar
    const opcionAntes = screen.getByRole('option', { name: '13:00' });
    const opcionDespues = screen.getByRole('option', { name: '15:00' });
    
    expect(opcionAntes).toBeInTheDocument();
    expect(opcionDespues).toBeInTheDocument();
  });

});
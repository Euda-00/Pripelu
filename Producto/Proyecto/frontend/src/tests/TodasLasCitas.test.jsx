import React from 'react';
import { describe, test, expect, afterEach, beforeEach, vi } from 'vitest';
import { render, screen, cleanup, waitFor, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { MemoryRouter } from 'react-router-dom';

// Ajusta la ruta a la carpeta donde tengas TodasLasCitas.jsx
import TodasLasCitas from '../Pages/TodasLasCitas.jsx'; 

beforeEach(() => {
  // Silenciamos los alert() y auto-aceptamos los confirm() por si tu código pide confirmación antes de borrar
  window.alert = vi.fn();
  window.confirm = vi.fn(() => true); 
});

afterEach(() => {
  cleanup();
  vi.restoreAllMocks(); 
});

describe('Pruebas Funcionales - Gestión de Citas (Plan QA)', () => {

  test('CP-04: Debe permitir la cancelación de una cita activa (Pendiente)', async () => {
    
    // 1. EL MOCK MULTIPROPÓSITO: Fingimos la carga inicial y luego la petición de borrado
    global.fetch = vi.fn((url, options) => {
      // A) Si el componente está pidiendo las citas al inicio (GET)
      if (!options || options.method === 'GET') {
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve([
            {
              id: 99,
              estado: 'pendiente',
              valorTotal: 15000,
              usuario: { nombre: 'Tomas', apellido: 'Prueba' }
            }
          ])
        });
      }
      
      // B) Si el componente está intentando CANCELAR la cita (PUT, PATCH o DELETE)
      if (options && (options.method === 'PUT' || options.method === 'PATCH' || options.method === 'DELETE')) {
         return Promise.resolve({ ok: true });
      }
      
      return Promise.resolve({ ok: true, json: () => Promise.resolve([]) });
    });

    // 2. Renderizamos el componente envuelto en MemoryRouter por si usa Links
    render(
      <MemoryRouter>
        <TodasLasCitas />
      </MemoryRouter>
    );

    // 3. Esperamos a que la cita falsa de "Tomas Prueba" aparezca en pantalla
    const nombreCliente = await screen.findByText(/Tomas Prueba/i);
    expect(nombreCliente).toBeInTheDocument();

    // 4. Buscamos el botón rojo de cancelar y hacemos clic
    const btnCancelar = await screen.findByRole('button', { name: /Cancelar/i });
    fireEvent.click(btnCancelar);

    // 5. RESULTADO ESPERADO (Según Excel): El sistema debe disparar la petición al backend para actualizar el estado
    await waitFor(() => {
      // El fetch se debió llamar al menos 2 veces: 
      // 1 vez para cargar la lista, y 1 vez al hacer clic en el botón cancelar.
      expect(global.fetch).toHaveBeenCalledTimes(2);
    });
  });

});
import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { VideojuegoApi } from './services/videojuego-api';

@Component({
  imports: [RouterOutlet],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('frontend-bequianbuster');

  // PRUEBA TEMPORAL: se elimina al crear la página de catálogo
  private readonly api = inject(VideojuegoApi);

  constructor() {
    this.api.listar().subscribe({
      next: datos => console.log('Videojuegos recibidos:', datos),
      error: err => console.error('Error al consultar ms-videojuegos:', err)
    });
  }
}
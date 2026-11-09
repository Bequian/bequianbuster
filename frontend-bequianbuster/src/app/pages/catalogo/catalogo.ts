import { Component, OnInit, inject } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CatalogoFacade } from '../../facades/catalogo-facade';
import { PLATAFORMAS } from '../../models/videojuego';

/**
 * Vista del catálogo (V de MVC).
 * Solo muestra el estado de la fachada y reenvía las acciones del usuario.
 */
@Component({
  selector: 'app-catalogo',
  imports: [FormsModule, CurrencyPipe],
  templateUrl: './catalogo.html',
  styleUrl: './catalogo.css'
})
export class Catalogo implements OnInit {

  protected readonly facade = inject(CatalogoFacade);
  protected readonly plataformas = PLATAFORMAS;

  // Filtros del formulario de búsqueda
  protected filtroTitulo = '';
  protected filtroPlataforma = '';
  protected filtroGenero = '';

  ngOnInit(): void {
    this.facade.cargar();
  }

  protected buscar(): void {
    this.facade.buscar({
      titulo: this.filtroTitulo,
      plataforma: this.filtroPlataforma,
      genero: this.filtroGenero
    });
  }

  protected limpiar(): void {
    this.filtroTitulo = '';
    this.filtroPlataforma = '';
    this.filtroGenero = '';
    this.facade.cargar();
  }
}
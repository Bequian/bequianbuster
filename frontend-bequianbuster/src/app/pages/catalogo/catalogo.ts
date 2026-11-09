import { Component, OnInit, inject } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { CatalogoFacade } from '../../facades/catalogo-facade';
import { PLATAFORMAS } from '../../models/videojuego';

@Component({
  selector: 'app-catalogo',
  imports: [FormsModule, CurrencyPipe, RouterLink],
  templateUrl: './catalogo.html',
  styleUrl: './catalogo.css'
})
export class Catalogo implements OnInit {

  protected readonly facade = inject(CatalogoFacade);
  private readonly router = inject(Router);
  protected readonly plataformas = PLATAFORMAS;

  protected filtroTitulo = '';
  protected filtroPlataforma = '';
  protected filtroGenero = '';
  protected idBuscado: number | null = null;

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

  /** Navega al detalle del ID ingresado. */
  protected verPorId(): void {
    if (this.idBuscado && this.idBuscado > 0) {
      this.router.navigate(['/videojuego', this.idBuscado]);
    }
  }
}
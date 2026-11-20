import { Component, OnInit, inject } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { CatalogoFacade } from '../../facades/catalogo-facade';
import { Auth } from '../../services/auth';

/**
 * Vista de detalle de un videojuego.
 * Lee el ID desde la URL (/videojuego/:id) y le pide los datos a la fachada.
 */
@Component({
  selector: 'app-detalle',
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './detalle.html',
  styleUrl: './detalle.css'
})
export class Detalle implements OnInit {

  protected readonly facade = inject(CatalogoFacade);
  protected readonly auth = inject(Auth);
  private readonly route = inject(ActivatedRoute);

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      this.facade.cargarDetalle(id);
    });
  }

  /** Si la carátula falla, muestra el logo como respaldo. */
  protected sinImagen(evento: Event): void {
    const img = evento.target as HTMLImageElement;
    img.onerror = null;
    img.src = 'logo.svg';
    img.classList.add('sin-imagen');
  }
}
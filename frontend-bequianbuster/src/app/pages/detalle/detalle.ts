import { Component, OnInit, inject } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { CatalogoFacade } from '../../facades/catalogo-facade';

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
  private readonly route = inject(ActivatedRoute);

  ngOnInit(): void {
    // Se suscribe a los parámetros para recargar si cambia el ID en la URL
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      this.facade.cargarDetalle(id);
    });
  }
}
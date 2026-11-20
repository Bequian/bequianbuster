import { Component, inject } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';

import { Carrito as CarritoService } from '../../services/carrito';

/** Vista del carrito: cantidades, subtotales y total. */
@Component({
  selector: 'app-carrito',
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './carrito.html',
  styleUrl: './carrito.css'
})
export class Carrito {
  protected readonly carrito = inject(CarritoService);
}
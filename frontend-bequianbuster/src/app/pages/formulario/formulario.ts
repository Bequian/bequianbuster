import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { CatalogoFacade } from '../../facades/catalogo-facade';
import { FormularioVideojuego, PLATAFORMAS } from '../../models/videojuego';

/**
 * Formulario para crear (POST) y editar (PUT) videojuegos.
 * Rutas: /admin/nuevo y /admin/editar/:id
 */
@Component({
  selector: 'app-formulario',
  imports: [FormsModule, RouterLink],
  templateUrl: './formulario.html',
  styleUrl: './formulario.css'
})
export class Formulario implements OnInit {

  protected readonly facade = inject(CatalogoFacade);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  protected readonly plataformas = PLATAFORMAS;
  protected id: number | null = null;
  protected form: FormularioVideojuego = this.facade.formularioVacio();

  ngOnInit(): void {
    this.facade.limpiarMensajes();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.id = Number(idParam);
      this.facade.cargarParaEditar(this.id, form => this.form = form);
    }
  }

  protected get esEdicion(): boolean {
    return this.id !== null;
  }

  protected guardar(): void {
    this.facade.guardar(this.id, this.form, videojuego =>
      this.router.navigate(['/videojuego', videojuego.id])
    );
  }
}
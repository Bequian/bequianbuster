import { TestBed } from '@angular/core/testing';
import { CatalogoFacade } from './catalogo-facade';

describe('CatalogoFacade', () => {
  let service: CatalogoFacade;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CatalogoFacade);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

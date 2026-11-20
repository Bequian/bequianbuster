import { TestBed } from '@angular/core/testing';
import { UsuarioApi } from './usuario-api';

describe('UsuarioApi', () => {
  let service: UsuarioApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsuarioApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

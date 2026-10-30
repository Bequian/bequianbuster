package com.example.ms_usuarios.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.ms_usuarios.dto.UserCreateRequest;
import com.example.ms_usuarios.model.Rol;
import com.example.ms_usuarios.repository.UserRepository;
import com.example.ms_usuarios.service.UserService;


/**
 * Inicializacion educativa: crea tres cuentas en Oracle SOLO si esta vacia.
 * Requiere contrasenas en variables de entorno, nunca las escribe en los logs.
 */
@Component
public class InitUsers implements CommandLineRunner {
    private static final Logger log=LoggerFactory.getLogger(InitUsers.class);
    private final UserRepository repo;
    private final UserService servicio;
    private final String admin,cliente1,cliente2;
    public InitUsers(UserRepository repo,UserService servicio,
             @Value("${bequianbuster.seed.admin:}") String admin,
             @Value("${bequianbuster.seed.cliente1:}") String cliente1,
             @Value("${bequianbuster.seed.cliente2:}") String cliente2) {
        this.repo=repo;this.servicio=servicio;
        this.admin=admin;this.cliente1=cliente1;this.cliente2=cliente2;
    }
    @Override
    public void run(String... args) {
        if(repo.count()>0){log.info("Ya existen cuentas: no se ejecuta la carga inicial");return;}
        if(admin.length()<8 || cliente1.length()<8 || cliente2.length()<8) {
            log.warn("No se crearon cuentas: faltan BB_ADMIN_PASS / BB_CLIENTE1_PASS / BB_CLIENTE2_PASS");
            return;
        }
        // Cuentas ficticias: una administradora y dos clientes.
        servicio.crear(new UserCreateRequest("admin","admin@correo.cl",admin,Rol.ADMIN));
        servicio.crear(new UserCreateRequest("cliente1","cliente1@correo.cl",cliente1,Rol.CLIENTE));
        servicio.crear(new UserCreateRequest("cliente2","cliente2@correo.cl",cliente2,Rol.CLIENTE));
        log.info("Tres cuentas de demostracion creadas con contrasenas hasheadas");
    }
}

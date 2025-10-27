package com.espaciosdeportivos.service;

import com.espaciosdeportivos.model.Administrador;
import com.espaciosdeportivos.model.Cliente;
import com.espaciosdeportivos.model.Persona;
import com.espaciosdeportivos.model.Role.RoleName;
import com.espaciosdeportivos.repository.AdministradorRepository;
import com.espaciosdeportivos.repository.ClienteRepository;
import com.espaciosdeportivos.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AprobacionService {

    private static final Logger logger = LoggerFactory.getLogger(AprobacionService.class);

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private AdministradorRepository adminRepo;

    @Autowired
    private PersonaRepository personaRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void crearEntidadEspecifica(Persona persona, RoleName rol) {
        logger.info("Creando entidad específica para persona ID: {}, rol: {}", persona.getId(), rol);
        
        try {
            if (rol == RoleName.ROL_CLIENTE) {
                crearClienteSeguro(persona);
            } else if (rol == RoleName.ROL_ADMINISTRADOR) {
                crearAdministradorSeguro(persona);
            } else if (rol == RoleName.ROL_SUPERUSUARIO) {
                logger.info("Rol SUPERUSUARIO - No se requiere entidad específica para persona ID: {}", persona.getId());
            }
        } catch (Exception e) {
            logger.error("Error al crear entidad específica para persona ID {}: {}", persona.getId(), e.getMessage());
        }
    }

    private void crearClienteSeguro(Persona persona) {
        try {
            if (!clienteRepo.existsById(persona.getId())) {
                Cliente cliente = new Cliente();
                cliente.setId(persona.getId());
                cliente.setCategoria("REGULAR");
                cliente.setEstado(true);
                
                clienteRepo.save(cliente);
                logger.info("Cliente creado exitosamente para persona ID: {}", persona.getId());
            } else {
                logger.info("Cliente ya existe para persona ID: {}", persona.getId());
            }
        } catch (Exception e) {
            logger.error("Error al crear cliente para persona ID {}: {}", persona.getId(), e.getMessage());
        }
    }

    private void crearAdministradorSeguro(Persona persona) {
        try {
            if (!adminRepo.existsById(persona.getId())) {
                Administrador admin = new Administrador();
                admin.setId(persona.getId());
                admin.setCargo("Administrador General");
                admin.setDireccion("Por asignar");
                admin.setEstado(true);
                
                adminRepo.save(admin);
                logger.info("Administrador creado exitosamente para persona ID: {}", persona.getId());
            } else {
                logger.info("Administrador ya existe para persona ID: {}", persona.getId());
            }
        } catch (Exception e) {
            logger.error("Error al crear administrador para persona ID {}: {}", persona.getId(), e.getMessage());
        }
    }

    public boolean existeEntidadEspecifica(Long personaId, RoleName rol) {
        try {
            if (rol == RoleName.ROL_CLIENTE) {
                return clienteRepo.existsById(personaId);
            } else if (rol == RoleName.ROL_ADMINISTRADOR) {
                return adminRepo.existsById(personaId);
            }
            return true;
        } catch (Exception e) {
            logger.error("Error al verificar existencia de entidad específica: {}", e.getMessage());
            return false;
        }
    }
}
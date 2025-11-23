package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.RegistroDTO.*;

public interface RegistrationService {
    String registrarCliente(RegistroClienteRequest request);
    String registrarAdministrador(RegistroAdministradorRequest request);
}
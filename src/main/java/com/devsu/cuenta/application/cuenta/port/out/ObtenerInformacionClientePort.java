package com.devsu.cuenta.application.cuenta.port.out;

import com.devsu.cuenta.exception.CuentaException;
import reactor.core.publisher.Mono;

public interface ObtenerInformacionClientePort {

    Mono<ClienteDto> obtenerInformacionCliente(String identificacion) throws CuentaException;

    record ClienteDto(Long id, String identificacion, String nombre) {
    }

}

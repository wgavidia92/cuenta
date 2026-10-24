package com.devsu.cuenta.application.port.cuenta.out;

import com.devsu.cuenta.exception.CuentaException;

public interface ObtenerInformacionClientePort {

    ClienteDto obtenerInformacionCliente(String identificacion) throws CuentaException;

    record ClienteDto(Long id, String identificacion, String nombre) {
    }

}

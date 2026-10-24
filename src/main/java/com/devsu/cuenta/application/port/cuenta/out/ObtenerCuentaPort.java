package com.devsu.cuenta.application.port.cuenta.out;

import com.devsu.cuenta.application.cuenta.model.CuentaDto;

import java.util.List;

public interface ObtenerCuentaPort {

    List<CuentaDto> obtenerCuentaPorClienteId(Long clienteId,  String nombreCliente);

}

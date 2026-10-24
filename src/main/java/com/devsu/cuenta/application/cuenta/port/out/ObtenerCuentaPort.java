package com.devsu.cuenta.application.cuenta.port.out;

import com.devsu.cuenta.application.cuenta.model.CuentaDto;
import com.devsu.cuenta.exception.CuentaException;
import com.devsu.cuenta.exception.MovimientoException;

import java.util.List;
import java.util.Optional;

public interface ObtenerCuentaPort {

    Optional<CuentaDto> obtenerCuentaPorNumero(String numero) throws CuentaException;

    List<CuentaDto> obtenerCuentaPorClienteId(Long clienteId, String nombreCliente);

}

package com.devsu.cuenta.application.port.cuenta.out;

import com.devsu.cuenta.application.cuenta.model.CuentaDto;
import com.devsu.cuenta.exception.CuentaException;

public interface ModificarCuentaPort {

    CuentaDto crearCuenta(CuentaDto cuentaDto, Long clienteId);

    CuentaDto actualizarCuenta(String numeroCuenta, CuentaDto cuenta) throws CuentaException;

}

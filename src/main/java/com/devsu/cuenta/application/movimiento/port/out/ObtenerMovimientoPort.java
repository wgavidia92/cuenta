package com.devsu.cuenta.application.movimiento.port.out;

import com.devsu.cuenta.application.movimiento.model.MovimientoDto;

import java.util.List;
import java.util.Optional;

public interface ObtenerMovimientoPort {

    List<MovimientoDto> obtenerMovimientosPorCuenta(Long cuentaId, String numeroCuenta);

    Optional<MovimientoDto> obtenerUltimoMovimientoPorCuenta(Long cuentaId, String numeroCuenta);

}

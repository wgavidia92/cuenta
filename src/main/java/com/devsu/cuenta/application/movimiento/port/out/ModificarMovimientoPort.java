package com.devsu.cuenta.application.movimiento.port.out;

import com.devsu.cuenta.application.movimiento.model.MovimientoDto;
import com.devsu.cuenta.exception.MovimientoException;

public interface ModificarMovimientoPort {

    MovimientoDto crearMovimiento(MovimientoDto movimiento, Long cuentaId);

    MovimientoDto modificarMovimiento(MovimientoDto movimientoDto, Long cuentaId) throws MovimientoException;

}

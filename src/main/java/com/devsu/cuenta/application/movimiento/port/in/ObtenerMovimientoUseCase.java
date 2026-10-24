package com.devsu.cuenta.application.movimiento.port.in;

import com.devsu.cuenta.application.movimiento.model.MovimientoDto;
import com.devsu.cuenta.exception.CuentaException;
import com.devsu.cuenta.exception.MovimientoException;
import org.springframework.util.StringUtils;

import java.util.List;

public interface ObtenerMovimientoUseCase {

    String ERROR_NUMERO_CUENTA = "Error con el numero de cuenta";

    List<MovimientoDto> obtenerMovimientos(MovimientoQuery query) throws MovimientoException, CuentaException;

    record MovimientoQuery(String numeroCuenta) {
        public void validar() throws MovimientoException {
            if (!StringUtils.hasText(numeroCuenta))
                throw new MovimientoException(ERROR_NUMERO_CUENTA);
        }
    }

}

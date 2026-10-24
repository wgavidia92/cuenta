package com.devsu.cuenta.application.movimiento.port.in;

import com.devsu.cuenta.application.movimiento.model.MovimientoDto;
import com.devsu.cuenta.exception.MovimientoException;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public interface ModificarMovimientoUseCase {

    String ERROR_VALIDACION_DATOS = "Error con la informacion del movimiento";

    MovimientoDto crearMovimiento(NuevoMovimientoCommand command) throws MovimientoException;

    MovimientoDto actualizarMovimiento(MovimientoCommand command) throws MovimientoException;

    record NuevoMovimientoCommand(MovimientoDto movimiento, String numeroCuenta) {
        public void validar() throws MovimientoException {
            validarDato(numeroCuenta);
            validarDato(movimiento().tipoMovimiento());
            if (movimiento.valor() == null || movimiento.valor().compareTo(BigDecimal.ZERO) == 0)
                throw new MovimientoException(ERROR_VALIDACION_DATOS);
        }

        private void validarDato(String dato) throws MovimientoException {
            if (!StringUtils.hasText(dato))
                throw new MovimientoException(ERROR_VALIDACION_DATOS);
        }
    }


    record MovimientoCommand(MovimientoDto movimiento, String numeroCuenta) {
        public void validar() throws MovimientoException {
            validarDato(numeroCuenta);
            validarDato(movimiento().tipoMovimiento());
            if (movimiento.valor() == null || movimiento.valor().compareTo(BigDecimal.ZERO) == 0)
                throw new MovimientoException(ERROR_VALIDACION_DATOS);
        }

        private void validarDato(String dato) throws MovimientoException {
            if (!StringUtils.hasText(dato))
                throw new MovimientoException(ERROR_VALIDACION_DATOS);
        }
    }

}

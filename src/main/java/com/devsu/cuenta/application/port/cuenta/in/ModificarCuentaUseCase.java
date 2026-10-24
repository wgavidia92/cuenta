package com.devsu.cuenta.application.port.cuenta.in;

import com.devsu.cuenta.application.cuenta.model.CuentaDto;
import com.devsu.cuenta.exception.CuentaException;
import org.springframework.util.StringUtils;

public interface ModificarCuentaUseCase {

    String ERROR_INFORMACION_CUENTA = "Informacion de cuenta incompleta";

    CuentaDto crearCuenta(NuevaCuentaCommand command) throws CuentaException;

    CuentaDto actualizarCuenta(CuentaCommand command) throws CuentaException;

    record NuevaCuentaCommand(CuentaDto cuenta, String identificacionCliente) {
        public void validar() throws CuentaException {
            validarDato(identificacionCliente);
            validarDato(cuenta.numero());
            validarDato(cuenta.tipoCuenta());
            if (cuenta.saldoInicial() == null)
                throw new CuentaException(ERROR_INFORMACION_CUENTA);
        }

        private void validarDato(String dato) throws CuentaException {
            if (!StringUtils.hasText(dato))
                throw new CuentaException(ERROR_INFORMACION_CUENTA);
        }
    }

    record CuentaCommand(CuentaDto cuenta, String identificacionCliente) {
        public void validar() throws CuentaException {
            validarDato(identificacionCliente);
            validarDato(cuenta.tipoCuenta());
        }

        private void validarDato(String dato) throws CuentaException {
            if (!StringUtils.hasText(dato))
                throw new CuentaException(ERROR_INFORMACION_CUENTA);
        }
    }

}

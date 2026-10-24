package com.devsu.cuenta.application.port.cuenta.in;

import com.devsu.cuenta.application.cuenta.model.CuentaDto;
import com.devsu.cuenta.exception.CuentaException;
import org.springframework.util.StringUtils;

import java.util.List;

public interface ObtenerCuentaUseCase {

    String ERROR_VALIDACION_DATOS = "Error validando datos";

    List<CuentaDto> obtenerCuentasPorIdentificacionCliente(IdentificacionQuery query) throws CuentaException;

    record IdentificacionQuery(String identificacionCliente) {
        public void validar() throws CuentaException {
            if (!StringUtils.hasText(identificacionCliente))
                throw new CuentaException(ERROR_VALIDACION_DATOS);
        }
    }

}

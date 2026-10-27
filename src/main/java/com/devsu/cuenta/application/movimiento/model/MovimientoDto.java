package com.devsu.cuenta.application.movimiento.model;

import com.devsu.cuenta.exception.MovimientoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoDto(
        Long movimientoId,
        String numeroCuenta,
        LocalDateTime fecha,
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo
) {

    public enum TipoMovimientoEnum {
        RETIRO,
        DEPOSITO;

        public static final String ERROR_TIPO_MOVIMIENTO = "Tipo de movimiento no permitido";

        public static TipoMovimientoEnum from(String tipoMovimiento) throws MovimientoException {
            if (RETIRO.name().equalsIgnoreCase(tipoMovimiento))
                return RETIRO;

            if (DEPOSITO.name().equalsIgnoreCase(tipoMovimiento))
                return DEPOSITO;

            throw new MovimientoException(ERROR_TIPO_MOVIMIENTO);
        }

    }

}

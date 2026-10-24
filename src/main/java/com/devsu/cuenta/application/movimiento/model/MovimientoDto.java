package com.devsu.cuenta.application.movimiento.model;

import org.springframework.data.relational.core.mapping.Column;

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
}

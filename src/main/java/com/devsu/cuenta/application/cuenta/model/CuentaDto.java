package com.devsu.cuenta.application.cuenta.model;

import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

public record CuentaDto(
        Long cuentaId,
        String nombreCliente,
        String numero,
        String tipoCuenta,
        BigDecimal saldoInicial,
        boolean estado
) {
}

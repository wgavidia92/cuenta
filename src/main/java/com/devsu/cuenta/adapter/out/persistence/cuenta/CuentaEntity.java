package com.devsu.cuenta.adapter.out.persistence.cuenta;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("CUENTA")
record CuentaEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column("CUENTA_ID")
        Long cuentaId,
        @Column("CLIENTE_ID")
        Long clienteId,
        String numero,
        @Column("TIPO_CUENTA")
        String tipoCuenta,
        @Column("SALDO_INICIAL")
        BigDecimal saldoInicial,
        boolean estado
) {
}

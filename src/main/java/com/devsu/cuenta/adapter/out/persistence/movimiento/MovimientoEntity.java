package com.devsu.cuenta.adapter.out.persistence.movimiento;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("MOVIMIENTO")
record MovimientoEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column("MOVIMIENTO_ID")
        Long movimientoId,
        @Column("CUENTA_ID")
        Long cuentaId,
        LocalDateTime fecha,
        @Column("TIPO_MOVIMIENTO")
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo
) {
}

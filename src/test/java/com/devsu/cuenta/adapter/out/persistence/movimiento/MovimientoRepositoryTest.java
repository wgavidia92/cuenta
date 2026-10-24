package com.devsu.cuenta.adapter.out.persistence.movimiento;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@DataJdbcTest
@ActiveProfiles("test")
class MovimientoRepositoryTest {

    @Autowired
    private MovimientoRepository repository;

    @Test
    void findAllByClienteIdTest() {
        var movimientos = repository.findByCuentaId(1L);
        Assertions.assertFalse(movimientos.isEmpty());
        Assertions.assertEquals(2, movimientos.size());
    }

    @Test
    void findFirstByCuentaIdOrderByMovimientoIdDescTest() {
        var movimiento = repository.findFirstByCuentaIdOrderByMovimientoIdDesc(1L);
        Assertions.assertTrue(movimiento.isPresent());
        Assertions.assertEquals("DEPOSITO", movimiento.get().tipoMovimiento());
        Assertions.assertEquals(new BigDecimal("1500.00"), movimiento.get().saldo());
    }

}

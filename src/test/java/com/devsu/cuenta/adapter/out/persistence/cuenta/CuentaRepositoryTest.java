package com.devsu.cuenta.adapter.out.persistence.cuenta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;

@DataJdbcTest
@ActiveProfiles("test")
class CuentaRepositoryTest {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Test
    void findByClienteIdTest() {
        var cuentas =  cuentaRepository.findByClienteId(1L);
        Assertions.assertFalse(cuentas.isEmpty());
        Assertions.assertEquals(1, cuentas.size());
    }

    @Test
    void findByNumeroCuentaTest() {
        var cuentas =  cuentaRepository.findByNumero("12345");
        Assertions.assertTrue(cuentas.isEmpty());
    }

}

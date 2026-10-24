package com.devsu.cuenta.adapter.out.persistence.cuenta;

import com.devsu.cuenta.UtilsTest;
import com.devsu.cuenta.exception.CuentaException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class CuentaAdapterTest {

    @MockitoBean
    private CuentaRepository cuentaRepository;

    private CuentaAdapter cuentaAdapter;

    @BeforeEach
    void init() {
        cuentaAdapter = new CuentaAdapter(cuentaRepository);
    }

    @Test
    void obtenerCuentaPorClienteIdTest() {
        Mockito.when(cuentaRepository.findByClienteId(Mockito.anyLong()))
                .thenReturn(List.of(crearCuentaEntity(true)));
        Assertions.assertDoesNotThrow(() -> cuentaAdapter.obtenerCuentaPorClienteId(1L, "Cliente"));
    }

    @Test
    void obtenerCuentaPorNumeroTest() {
        Mockito.when(cuentaRepository.findByNumero(Mockito.anyString()))
                .thenReturn(Optional.of(crearCuentaEntity(true)));
        Assertions.assertDoesNotThrow(() -> cuentaAdapter.obtenerCuentaPorNumero("1234"));
    }

    @Test
    void crearCuentaTest() {
        Mockito.when(cuentaRepository.save(Mockito.any(CuentaEntity.class)))
                .thenReturn(crearCuentaEntity(true));
        var cuenta = cuentaAdapter.crearCuenta(UtilsTest.generarCuenta(), 1L);
        Assertions.assertEquals(1L, cuenta.cuentaId());
        Assertions.assertEquals("Cliente", cuenta.nombreCliente());
    }

    @Test
    void actualizarCuentaTest() throws CuentaException {
        Mockito.when(cuentaRepository.findByNumero(Mockito.anyString()))
                .thenReturn(Optional.of(crearCuentaEntity(false)));
        Mockito.when(cuentaRepository.save(Mockito.any(CuentaEntity.class)))
                .thenReturn(crearCuentaEntity(true));
        var cuenta = cuentaAdapter.actualizarCuenta("1", UtilsTest.generarCuenta());
        Assertions.assertEquals(1L, cuenta.cuentaId());
        Assertions.assertTrue(cuenta.estado());
    }

    @Test
    void actualizarCuentaConNumeroDeCuentaInexistenteTest() {
        Mockito.when(cuentaRepository.findByNumero(Mockito.anyString()))
                .thenReturn(Optional.empty());
        var exception = Assertions.assertThrows(CuentaException.class,
                () -> cuentaAdapter.actualizarCuenta("1", UtilsTest.generarCuenta()));
        Assertions.assertEquals(CuentaAdapter.ERROR_ACTUALIZANDO_CUENTA, exception.getMessage());
    }

    private CuentaEntity crearCuentaEntity(boolean estado) {
        return new CuentaEntity(
                1L,
                1L,
                "1",
                "AHORROS",
                BigDecimal.TEN,
                estado
        );
    }

}

package com.devsu.cuenta.application.movimiento.domain;

import com.devsu.cuenta.UtilsTest;
import com.devsu.cuenta.application.cuenta.port.out.ObtenerCuentaPort;
import com.devsu.cuenta.application.movimiento.port.in.ObtenerMovimientoUseCase;
import com.devsu.cuenta.application.movimiento.port.out.ObtenerMovimientoPort;
import com.devsu.cuenta.exception.CuentaException;
import com.devsu.cuenta.exception.MovimientoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
class ObtenerMovimientoServiceTest {

    @MockitoBean
    private ObtenerMovimientoPort obtenerMovimientoPort;
    @MockitoBean
    private ObtenerCuentaPort obtenerCuentaPort;

    private ObtenerMovimientoUseCase obtenerMovimientoUseCase;

    @BeforeEach
    void init() {
        obtenerMovimientoUseCase = new ObtenerMovimientoService(obtenerMovimientoPort, obtenerCuentaPort);
    }

    @Test
    void obtenerMovimientosConCuentaNoValidaTest() throws CuentaException {
        Mockito.when(obtenerCuentaPort.obtenerCuentaPorNumero(Mockito.anyString()))
                .thenReturn(Optional.empty());
        var query = new ObtenerMovimientoUseCase.MovimientoQuery("1234");
        var excepcion = Assertions.assertThrows(MovimientoException.class, () -> obtenerMovimientoUseCase.obtenerMovimientos(query));
        Assertions.assertEquals(ObtenerMovimientoService.ERROR_ACTUALIZANDO_MOVIMIENTO, excepcion.getMessage());
    }

    @Test
    void obtenerMovimientosTest() throws CuentaException {
        Mockito.when(obtenerCuentaPort.obtenerCuentaPorNumero(Mockito.anyString()))
                .thenReturn(Optional.of(UtilsTest.generarCuenta()));
        var query = new ObtenerMovimientoUseCase.MovimientoQuery("1234");
        Assertions.assertDoesNotThrow(() -> obtenerMovimientoUseCase.obtenerMovimientos(query));
    }

    @Test
    void obtenerMovimientosSinNumeroCuentaTest() {
        var query = new ObtenerMovimientoUseCase.MovimientoQuery("");
        var excepcion = Assertions.assertThrows(MovimientoException.class,
                () -> obtenerMovimientoUseCase.obtenerMovimientos(query));
        Assertions.assertEquals(ObtenerMovimientoUseCase.ERROR_NUMERO_CUENTA, excepcion.getMessage());
    }

}

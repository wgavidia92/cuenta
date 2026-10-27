package com.devsu.cuenta.application.movimiento.domain;

import com.devsu.cuenta.UtilsTest;
import com.devsu.cuenta.application.movimiento.model.MovimientoDto;
import com.devsu.cuenta.application.movimiento.port.in.ModificarMovimientoUseCase;
import com.devsu.cuenta.exception.CuentaException;
import com.devsu.cuenta.exception.MovimientoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class ModificarMovimientoServiceActualizarMovimientoTest extends ModificarMovimientoServiceAbstractTest {

    @Test
    void modificarMovimientoTest() throws CuentaException {
        Mockito.when(obtenerCuentaPort.obtenerCuentaPorNumero(Mockito.anyString()))
                .thenReturn(Optional.of(UtilsTest.generarCuenta()));
        Mockito.when(obtenerMovimientoPort.obtenerUltimoMovimientoPorCuenta(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(Optional.of(UtilsTest.generarMovimiento("DEPOSITO")));
        var command = new ModificarMovimientoUseCase.MovimientoCommand(
                UtilsTest.generarMovimiento("RETIRO"), "1234");
        Assertions.assertDoesNotThrow(() -> modificarMovimientoUseCase.actualizarMovimiento(command));
    }

    @Test
    void modificarMovimientoCuentaInvalidaTest() throws CuentaException {
        Mockito.when(obtenerCuentaPort.obtenerCuentaPorNumero(Mockito.anyString()))
                .thenReturn(Optional.empty());
        var command = new ModificarMovimientoUseCase.MovimientoCommand(
                UtilsTest.generarMovimiento("RETIRO"), "1234");
        var excepcion = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.actualizarMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoService.ERROR_ACTUALIZAR_MOVIMIENTO, excepcion.getMessage());
    }

    @Test
    void modificarMovimientoSinMovimientoAnteriorTest() throws CuentaException {
        Mockito.when(obtenerCuentaPort.obtenerCuentaPorNumero(Mockito.anyString()))
                .thenReturn(Optional.of(UtilsTest.generarCuenta()));
        Mockito.when(obtenerMovimientoPort.obtenerUltimoMovimientoPorCuenta(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        var command = new ModificarMovimientoUseCase.MovimientoCommand(
                UtilsTest.generarMovimiento("RETIRO"), "1234");
        var excepcion = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.actualizarMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoService.ERROR_ACTUALIZAR_MOVIMIENTO, excepcion.getMessage());
    }

    @Test
    void modificarMovimientoRetiroConSaldoNegativoTest() throws CuentaException {
        Mockito.when(obtenerCuentaPort.obtenerCuentaPorNumero(Mockito.anyString()))
                .thenReturn(Optional.of(UtilsTest.generarCuenta()));
        Mockito.when(obtenerMovimientoPort.obtenerUltimoMovimientoPorCuenta(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(Optional.of(UtilsTest.generarMovimiento("RETIRO")));
        var movimiento = new MovimientoDto(
                1L,
                "",
                null,
                "RETIRO",
                new BigDecimal("10.1"),
                null);
        var command = new ModificarMovimientoUseCase.MovimientoCommand(movimiento, "1234");
        var excepcion = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.actualizarMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoService.ERROR_ACTUALIZAR_MOVIMIENTO, excepcion.getMessage());
    }

    @Test
    void modificarMovmientoSinCuentaTest() {
        var command = new ModificarMovimientoUseCase.MovimientoCommand(null, "");
        var excepcion = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.actualizarMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoUseCase.ERROR_VALIDACION_DATOS, excepcion.getMessage());
    }

    @Test
    void modificarMovmientoSinTipoMovimientoTest() {
        var movimiento = new MovimientoDto(1l, "", null, "", null, null);
        var command = new ModificarMovimientoUseCase.MovimientoCommand(movimiento, "1234");
        var excepcion = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.actualizarMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoUseCase.ERROR_VALIDACION_DATOS, excepcion.getMessage());
    }

    @Test
    void modificarMovmientoSinValorTest() {
        var movimiento = new MovimientoDto(1l, "", null, "Test", null, null);
        var command = new ModificarMovimientoUseCase.MovimientoCommand(movimiento, "1234");
        var excepcion = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.actualizarMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoUseCase.ERROR_VALIDACION_DATOS, excepcion.getMessage());
    }

    @Test
    void modificarMovmientoConValorNegativoTest() {
        var movimiento = new MovimientoDto(1l, "", null, "Test", new BigDecimal("-1"), null);
        var command = new ModificarMovimientoUseCase.MovimientoCommand(movimiento, "1234");
        var excepcion = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.actualizarMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoUseCase.ERROR_VALIDACION_DATOS, excepcion.getMessage());
    }

}

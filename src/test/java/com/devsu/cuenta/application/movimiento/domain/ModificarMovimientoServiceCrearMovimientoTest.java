package com.devsu.cuenta.application.movimiento.domain;

import com.devsu.cuenta.UtilsTest;
import com.devsu.cuenta.application.cuenta.model.CuentaDto;
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
class ModificarMovimientoServiceCrearMovimientoTest extends ModificarMovimientoServiceAbstractTest {

    @Test
    void crearMovimientoDepositoTest() throws CuentaException {
        Mockito.when(obtenerCuentaPort.obtenerCuentaPorNumero(Mockito.anyString()))
                .thenReturn(Optional.of(UtilsTest.generarCuenta()));
        Mockito.when(obtenerMovimientoPort.obtenerUltimoMovimientoPorCuenta(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(modificarMovimientoPort.crearMovimiento(Mockito.any(MovimientoDto.class), Mockito.anyLong()))
                .thenReturn(UtilsTest.generarMovimiento("DEPOSITO"));
        var command = new ModificarMovimientoUseCase.NuevoMovimientoCommand(
                UtilsTest.generarMovimiento("DEPOSITO"),
                "1234");
        Assertions.assertDoesNotThrow(() -> modificarMovimientoUseCase.crearMovimiento(command));
    }

    @Test
    void crearMovimientoRetiroTest() throws CuentaException {
        Mockito.when(obtenerCuentaPort.obtenerCuentaPorNumero(Mockito.anyString()))
                .thenReturn(Optional.of(UtilsTest.generarCuenta()));
        Mockito.when(obtenerMovimientoPort.obtenerUltimoMovimientoPorCuenta(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(modificarMovimientoPort.crearMovimiento(Mockito.any(MovimientoDto.class), Mockito.anyLong()))
                .thenReturn(UtilsTest.generarMovimiento("RETIRO"));
        var command = new ModificarMovimientoUseCase.NuevoMovimientoCommand(
                UtilsTest.generarMovimiento("RETIRO"),
                "1234");
        Assertions.assertDoesNotThrow(() -> modificarMovimientoUseCase.crearMovimiento(command));
    }

    @Test
    void crearMovimientoDepositoCuentaDesactivadaTest() throws CuentaException {
        var cuentaDesactivada = Mockito.mock(CuentaDto.class);
        Mockito.when(obtenerCuentaPort.obtenerCuentaPorNumero(Mockito.anyString()))
                .thenReturn(Optional.of(cuentaDesactivada));
        Mockito.when(cuentaDesactivada.estado())
                .thenReturn(false);
        var command = new ModificarMovimientoUseCase.NuevoMovimientoCommand(
                UtilsTest.generarMovimiento("DEPOSITO"),
                "1234");
        var excepcion = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.crearMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoService.ERROR_ACTUALIZAR_MOVIMIENTO, excepcion.getMessage());
    }

    @Test
    void crearMovimientoSinNumeroCuentaTest() {
        var command = new ModificarMovimientoUseCase.NuevoMovimientoCommand(null, "");
        var exception = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.crearMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoUseCase.ERROR_VALIDACION_DATOS, exception.getMessage());
    }

    @Test
    void crearMovimientoSinTipoMovimientoTest() {
        var movimiento = UtilsTest.generarMovimiento("");
        var command = new ModificarMovimientoUseCase.NuevoMovimientoCommand(movimiento, "123");
        var exception = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.crearMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoUseCase.ERROR_VALIDACION_DATOS, exception.getMessage());
    }

    @Test
    void crearMovimientoSinValorTest() {
        var movimiento = new MovimientoDto(1l, "", null, "Test", null, null);
        var command = new ModificarMovimientoUseCase.NuevoMovimientoCommand(movimiento, "123");
        var exception = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.crearMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoUseCase.ERROR_VALIDACION_DATOS, exception.getMessage());
    }

    @Test
    void crearMovimientoConValorCeroTest() {
        var movimiento = new MovimientoDto(1l, "", null, "Test", BigDecimal.ZERO, null);
        var command = new ModificarMovimientoUseCase.NuevoMovimientoCommand(movimiento, "123");
        var exception = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.crearMovimiento(command));
        Assertions.assertEquals(ModificarMovimientoUseCase.ERROR_VALIDACION_DATOS, exception.getMessage());
    }


    @Test
    void crearMovimientoConTipoMovimientoIncorrectoTest() throws CuentaException {
        Mockito.when(obtenerCuentaPort.obtenerCuentaPorNumero(Mockito.anyString()))
                .thenReturn(Optional.of(UtilsTest.generarCuenta()));
        Mockito.when(obtenerMovimientoPort.obtenerUltimoMovimientoPorCuenta(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        var command = new ModificarMovimientoUseCase.NuevoMovimientoCommand(
                UtilsTest.generarMovimiento("Test"),
                "1234");
        var exception = Assertions.assertThrows(MovimientoException.class,
                () -> modificarMovimientoUseCase.crearMovimiento(command));
        Assertions.assertEquals(MovimientoDto.TipoMovimientoEnum.ERROR_TIPO_MOVIMIENTO, exception.getMessage());
    }


}

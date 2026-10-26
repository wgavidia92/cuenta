package com.devsu.cuenta.application.cuenta.domain;

import com.devsu.cuenta.UtilsTest;
import com.devsu.cuenta.application.cuenta.model.CuentaDto;
import com.devsu.cuenta.application.cuenta.port.in.ModificarCuentaUseCase;
import com.devsu.cuenta.application.cuenta.port.out.ModificarCuentaPort;
import com.devsu.cuenta.application.cuenta.port.out.ObtenerInformacionClientePort;
import com.devsu.cuenta.exception.CuentaException;
import com.devsu.cuenta.exception.IntegrationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
class ModificarCuentaUseCaseTest {

    @MockitoBean
    private ModificarCuentaPort modificarCuentaPort;
    @MockitoBean
    private ObtenerInformacionClientePort obtenerInformacionClientePort;

    private ModificarCuentaUseCase modificarCuentaUseCase;

    @BeforeEach
    void init() {
        modificarCuentaUseCase = new ModificarCuentaService(modificarCuentaPort, obtenerInformacionClientePort);
    }

    @Test
    void crearCuentaTest() throws CuentaException {
        Mockito.when(obtenerInformacionClientePort.obtenerInformacionCliente(Mockito.anyString()))
                .thenReturn(Mono.fromSupplier(UtilsTest::generarInformacionCliente));
        Mockito.when(modificarCuentaPort.crearCuenta(Mockito.any(CuentaDto.class), Mockito.anyLong()))
                .thenReturn(UtilsTest.generarCuenta());
        var command = new ModificarCuentaUseCase.NuevaCuentaCommand(UtilsTest.generarCuenta(), "1234");
        Assertions.assertDoesNotThrow(() -> modificarCuentaUseCase.crearCuenta(command));
    }

    @Test
    void crearCuentaSinIdentificacionTest() {
        var cuenta = new CuentaDto(0L, "", "", "", BigDecimal.TEN, true);
        var command = new ModificarCuentaUseCase.NuevaCuentaCommand(cuenta, "");
        var excepcion = Assertions.assertThrows(CuentaException.class, () -> modificarCuentaUseCase.crearCuenta(command));
        Assertions.assertEquals(ModificarCuentaUseCase.ERROR_INFORMACION_CUENTA, excepcion.getMessage());
    }

    @Test
    void crearCuentaSinNumeroDeCuentaTest() {
        var cuenta = new CuentaDto(0L, "", "", "", BigDecimal.TEN, true);
        var command = new ModificarCuentaUseCase.NuevaCuentaCommand(cuenta, "1234");
        var excepcion = Assertions.assertThrows(CuentaException.class, () -> modificarCuentaUseCase.crearCuenta(command));
        Assertions.assertEquals(ModificarCuentaUseCase.ERROR_INFORMACION_CUENTA, excepcion.getMessage());
    }

    @Test
    void crearCuentaSinTipoDeCuentaTest() {
        var cuenta = new CuentaDto(0L, "", "1234", "", BigDecimal.TEN, true);
        var command = new ModificarCuentaUseCase.NuevaCuentaCommand(cuenta, "1234");
        var excepcion = Assertions.assertThrows(CuentaException.class, () -> modificarCuentaUseCase.crearCuenta(command));
        Assertions.assertEquals(ModificarCuentaUseCase.ERROR_INFORMACION_CUENTA, excepcion.getMessage());
    }

    @Test
    void crearCuentaSinSaldoInicialTest() {
        var cuenta = new CuentaDto(0L, "", "1234", "", null, true);
        var command = new ModificarCuentaUseCase.NuevaCuentaCommand(cuenta, "1234");
        var excepcion = Assertions.assertThrows(CuentaException.class, () -> modificarCuentaUseCase.crearCuenta(command));
        Assertions.assertEquals(ModificarCuentaUseCase.ERROR_INFORMACION_CUENTA, excepcion.getMessage());
    }

    @Test
    void modificarCuentaTest() throws CuentaException {
        Mockito.when(modificarCuentaPort.actualizarCuenta(Mockito.anyString(), Mockito.any(CuentaDto.class)))
                .thenReturn(UtilsTest.generarCuenta());
        var command = new ModificarCuentaUseCase.CuentaCommand(UtilsTest.generarCuenta(), "1234");
        Assertions.assertDoesNotThrow(() -> modificarCuentaUseCase.actualizarCuenta(command));
    }

    @Test
    void modificarCuentaSinIdentificacionTest() {
        var command = new ModificarCuentaUseCase.CuentaCommand(UtilsTest.generarCuenta(), "");
        var excepcion = Assertions.assertThrows(CuentaException.class, () -> modificarCuentaUseCase.actualizarCuenta(command));
        Assertions.assertEquals(ModificarCuentaUseCase.ERROR_INFORMACION_CUENTA, excepcion.getMessage());
    }

    @Test
    void modificarCuentaSinTipoCuentaTest() {
        var cuenta = new CuentaDto(0L, "", "1234", "", null, true);
        var command = new ModificarCuentaUseCase.CuentaCommand(cuenta, "");
        var excepcion = Assertions.assertThrows(CuentaException.class, () -> modificarCuentaUseCase.actualizarCuenta(command));
        Assertions.assertEquals(ModificarCuentaUseCase.ERROR_INFORMACION_CUENTA, excepcion.getMessage());
    }

    @Test
    void crearCuentaConExcepcionIntegracionTest() throws CuentaException {
        Mockito.when(obtenerInformacionClientePort.obtenerInformacionCliente(Mockito.anyString()))
                .thenThrow(new IntegrationException("Error integracion"));

        var command = new ModificarCuentaUseCase.NuevaCuentaCommand(UtilsTest.generarCuenta(), "1234");
        var excepcion = Assertions.assertThrows(CuentaException.class, () -> modificarCuentaUseCase.crearCuenta(command));
        Assertions.assertEquals("Error integracion", excepcion.getMessage());
    }

}

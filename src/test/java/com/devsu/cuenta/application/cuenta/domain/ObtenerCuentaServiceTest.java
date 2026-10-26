package com.devsu.cuenta.application.cuenta.domain;

import com.devsu.cuenta.UtilsTest;
import com.devsu.cuenta.application.cuenta.port.in.ObtenerCuentaUseCase;
import com.devsu.cuenta.application.cuenta.port.out.ObtenerCuentaPort;
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

import java.util.List;

@ExtendWith(SpringExtension.class)
class ObtenerCuentaServiceTest {

    @MockitoBean
    private ObtenerCuentaPort obtenerCuentaPort;
    @MockitoBean
    private ObtenerInformacionClientePort obtenerInformacionClientePort;

    private ObtenerCuentaUseCase obtenerCuentaUseCase;

    @BeforeEach
    void init() {
        obtenerCuentaUseCase = new ObtenerCuentaService(obtenerCuentaPort, obtenerInformacionClientePort);
    }

    @Test
    void obtenerCuentasPorIdentificacionClienteTest() throws CuentaException {
        var identificacionQuery = new ObtenerCuentaUseCase.IdentificacionQuery("1234");
        Mockito.when(obtenerInformacionClientePort.obtenerInformacionCliente(Mockito.anyString()))
                .thenReturn(Mono.fromSupplier(UtilsTest::generarInformacionCliente));
        Mockito.when(obtenerCuentaPort.obtenerCuentaPorClienteId(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(List.of());
        Assertions.assertDoesNotThrow(
                () -> obtenerCuentaUseCase.obtenerCuentasPorIdentificacionCliente(identificacionQuery));
    }

    @Test
    void obtenerCuentasPorIdentificacionClienteSinValorTest() {
        var identificacionQuery = new ObtenerCuentaUseCase.IdentificacionQuery(null);
        var excepcion = Assertions.assertThrows(CuentaException.class,
                () -> obtenerCuentaUseCase.obtenerCuentasPorIdentificacionCliente(identificacionQuery));
        Assertions.assertEquals(ObtenerCuentaUseCase.ERROR_VALIDACION_DATOS, excepcion.getMessage());
    }

    @Test
    void obtenerCuentasPorIdentificacionClienteConExcepcionIntegracionTest() throws CuentaException {
        var identificacionQuery = new ObtenerCuentaUseCase.IdentificacionQuery("1234");
        Mockito.when(obtenerInformacionClientePort.obtenerInformacionCliente(Mockito.anyString()))
                .thenThrow(new IntegrationException("Error integracion"));
        var excepcion = Assertions.assertThrows(CuentaException.class,
                () -> obtenerCuentaUseCase.obtenerCuentasPorIdentificacionCliente(identificacionQuery));
        Assertions.assertEquals("Error integracion", excepcion.getMessage());
    }

}

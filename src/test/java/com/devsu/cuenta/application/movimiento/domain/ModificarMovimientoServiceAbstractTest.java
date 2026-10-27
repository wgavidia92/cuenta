package com.devsu.cuenta.application.movimiento.domain;

import com.devsu.cuenta.application.cuenta.port.out.ObtenerCuentaPort;
import com.devsu.cuenta.application.movimiento.port.in.ModificarMovimientoUseCase;
import com.devsu.cuenta.application.movimiento.port.out.ModificarMovimientoPort;
import com.devsu.cuenta.application.movimiento.port.out.ObtenerMovimientoPort;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

abstract class ModificarMovimientoServiceAbstractTest {

    @MockitoBean
    protected ModificarMovimientoPort modificarMovimientoPort;
    @MockitoBean
    protected ObtenerMovimientoPort obtenerMovimientoPort;
    @MockitoBean
    protected ObtenerCuentaPort obtenerCuentaPort;

    protected ModificarMovimientoUseCase modificarMovimientoUseCase;

    @BeforeEach
    void init() {
        modificarMovimientoUseCase = new ModificarMovimientoService(
                modificarMovimientoPort,
                obtenerMovimientoPort,
                obtenerCuentaPort);
    }

}

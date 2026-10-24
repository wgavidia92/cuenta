package com.devsu.cuenta;

import com.devsu.cuenta.application.cuenta.model.CuentaDto;
import com.devsu.cuenta.application.cuenta.port.out.ObtenerInformacionClientePort;

import java.math.BigDecimal;

public class UtilsTest {

    private UtilsTest() {
    }

    public static ObtenerInformacionClientePort.ClienteDto generarInformacionCliente() {
        return new ObtenerInformacionClientePort.ClienteDto(1L, "1234", "Cliente");
    }

    public static CuentaDto generarCuenta() {
        return new CuentaDto(
                1L,
                "Cliente",
                "1",
                "AHORROS",
                BigDecimal.TEN,
                true
        );
    }

}

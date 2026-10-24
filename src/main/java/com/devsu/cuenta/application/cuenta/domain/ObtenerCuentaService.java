package com.devsu.cuenta.application.cuenta.domain;

import com.devsu.cuenta.application.cuenta.model.CuentaDto;
import com.devsu.cuenta.application.cuenta.port.in.ObtenerCuentaUseCase;
import com.devsu.cuenta.application.cuenta.port.out.ObtenerCuentaPort;
import com.devsu.cuenta.application.cuenta.port.out.ObtenerInformacionClientePort;
import com.devsu.cuenta.exception.CuentaException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ObtenerCuentaService implements ObtenerCuentaUseCase {

    private final ObtenerCuentaPort obtenerCuentaPort;
    private final ObtenerInformacionClientePort obtenerInformacionClientePort;

    public ObtenerCuentaService(ObtenerCuentaPort obtenerCuentaPort,
                                ObtenerInformacionClientePort obtenerInformacionClientePort) {
        this.obtenerCuentaPort = obtenerCuentaPort;
        this.obtenerInformacionClientePort = obtenerInformacionClientePort;
    }

    @Override
    public List<CuentaDto> obtenerCuentasPorIdentificacionCliente(IdentificacionQuery query) throws CuentaException {
        query.validar();
        var cliente = obtenerInformacionClientePort.obtenerInformacionCliente(query.identificacionCliente());
        return obtenerCuentaPort.obtenerCuentaPorClienteId(cliente.id(), cliente.nombre());
    }

}

package com.devsu.cuenta.application.cuenta.domain;

import com.devsu.cuenta.application.cuenta.model.CuentaDto;
import com.devsu.cuenta.application.cuenta.port.in.ModificarCuentaUseCase;
import com.devsu.cuenta.application.cuenta.port.out.ModificarCuentaPort;
import com.devsu.cuenta.application.cuenta.port.out.ObtenerInformacionClientePort;
import com.devsu.cuenta.exception.CuentaException;
import com.devsu.cuenta.exception.IntegrationException;
import org.springframework.stereotype.Service;

@Service
class ModificarCuentaService implements ModificarCuentaUseCase {

    private final ModificarCuentaPort modificarCuentaPort;
    private final ObtenerInformacionClientePort obtenerInformacionClientePort;

    ModificarCuentaService(ModificarCuentaPort modificarCuentaPort, ObtenerInformacionClientePort obtenerInformacionClientePort) {
        this.modificarCuentaPort = modificarCuentaPort;
        this.obtenerInformacionClientePort = obtenerInformacionClientePort;
    }

    @Override
    public CuentaDto crearCuenta(NuevaCuentaCommand command) throws CuentaException {
        try {
            command.validar();
            var informacionCliente = obtenerInformacionClientePort
                    .obtenerInformacionCliente(command.identificacionCliente())
                    .block();
            var cuenta = new CuentaDto(
                    command.cuenta().cuentaId(),
                    informacionCliente.nombre(),
                    command.cuenta().numero(),
                    command.cuenta().tipoCuenta(),
                    command.cuenta().saldoInicial(),
                    command.cuenta().estado()
            );
            return modificarCuentaPort.crearCuenta(cuenta, informacionCliente.id());
        } catch (IntegrationException e) {
            throw new CuentaException(e.getMessage());
        }
    }

    @Override
    public CuentaDto actualizarCuenta(CuentaCommand command) throws CuentaException {
        command.validar();
        return modificarCuentaPort.actualizarCuenta(command.cuenta().numero(), command.cuenta());
    }

}

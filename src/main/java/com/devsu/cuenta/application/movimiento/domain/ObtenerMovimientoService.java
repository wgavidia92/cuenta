package com.devsu.cuenta.application.movimiento.domain;

import com.devsu.cuenta.application.cuenta.port.out.ObtenerCuentaPort;
import com.devsu.cuenta.application.movimiento.model.MovimientoDto;
import com.devsu.cuenta.application.movimiento.port.in.ObtenerMovimientoUseCase;
import com.devsu.cuenta.application.movimiento.port.out.ObtenerMovimientoPort;
import com.devsu.cuenta.exception.CuentaException;
import com.devsu.cuenta.exception.MovimientoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ObtenerMovimientoService implements ObtenerMovimientoUseCase {

    static final String ERROR_ACTUALIZANDO_MOVIMIENTO = "Error actualizando movimiento";

    private final Logger log = LoggerFactory.getLogger(ObtenerMovimientoService.class);

    private final ObtenerMovimientoPort obtenerMovimientoPort;
    private final ObtenerCuentaPort obtenerCuentaPort;

    ObtenerMovimientoService(ObtenerMovimientoPort obtenerMovimientoPort,
                             ObtenerCuentaPort obtenerCuentaPort) {
        this.obtenerMovimientoPort = obtenerMovimientoPort;
        this.obtenerCuentaPort = obtenerCuentaPort;
    }

    @Override
    public List<MovimientoDto> obtenerMovimientos(MovimientoQuery query) throws MovimientoException, CuentaException {
        query.validar();
        var cuentaDB = obtenerCuentaPort.obtenerCuentaPorNumero(query.numeroCuenta());
        if (cuentaDB.isEmpty()) {
            log.error("Cuenta no encontrada");
            throw new MovimientoException(ERROR_ACTUALIZANDO_MOVIMIENTO);
        }

        return obtenerMovimientoPort.obtenerMovimientosPorCuenta(cuentaDB.get().cuentaId(), query.numeroCuenta());
    }

}

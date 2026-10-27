package com.devsu.cuenta.application.movimiento.domain;

import com.devsu.cuenta.application.cuenta.model.CuentaDto;
import com.devsu.cuenta.application.cuenta.port.out.ObtenerCuentaPort;
import com.devsu.cuenta.application.movimiento.model.MovimientoDto;
import com.devsu.cuenta.application.movimiento.port.in.ModificarMovimientoUseCase;
import com.devsu.cuenta.application.movimiento.port.out.ModificarMovimientoPort;
import com.devsu.cuenta.application.movimiento.port.out.ObtenerMovimientoPort;
import com.devsu.cuenta.exception.CuentaException;
import com.devsu.cuenta.exception.MovimientoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
class ModificarMovimientoService implements ModificarMovimientoUseCase {

    static final String ERROR_ACTUALIZAR_MOVIMIENTO = "Error creando o actualizando movimiento";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ModificarMovimientoPort modificarMovimientoPort;
    private final ObtenerMovimientoPort obtenerMovimientoPort;
    private final ObtenerCuentaPort obtenerCuentaPort;

    ModificarMovimientoService(ModificarMovimientoPort modificarMovimientoPort,
                               ObtenerMovimientoPort obtenerMovimientoPort,
                               ObtenerCuentaPort obtenerCuentaPort) {
        this.modificarMovimientoPort = modificarMovimientoPort;
        this.obtenerMovimientoPort = obtenerMovimientoPort;
        this.obtenerCuentaPort = obtenerCuentaPort;
    }

    @Override
    public MovimientoDto crearMovimiento(NuevoMovimientoCommand command) throws MovimientoException, CuentaException {
        command.validar();
        var cuenta = obtenerCuentaPorNumero(command.numeroCuenta());
        var ultimoMovimiento = obtenerMovimientoPort.obtenerUltimoMovimientoPorCuenta(cuenta.cuentaId(), cuenta.numero());
        var movimiento = generarNuevoMovimiento(cuenta, command.movimiento(), ultimoMovimiento);
        return modificarMovimientoPort.crearMovimiento(movimiento, cuenta.cuentaId());
    }

    @Override
    public MovimientoDto actualizarMovimiento(MovimientoCommand command) throws MovimientoException, CuentaException {
        command.validar();
        var cuenta = obtenerCuentaPorNumero(command.numeroCuenta());
        var ultimoMovimientoDB = obtenerMovimientoPort.obtenerUltimoMovimientoPorCuenta(cuenta.cuentaId(), cuenta.numero());
        if (ultimoMovimientoDB.isEmpty()) {
            log.error("No existe movimiento para actualizar");
            throw new MovimientoException(ERROR_ACTUALIZAR_MOVIMIENTO);
        }

        var movimiento = generarNuevoMovimiento(cuenta, command.movimiento(), ultimoMovimientoDB);
        return modificarMovimientoPort.crearMovimiento(movimiento, cuenta.cuentaId());
    }

    private CuentaDto obtenerCuentaPorNumero(String numeroCuenta) throws MovimientoException, CuentaException {
        var cuenta = obtenerCuentaPort.obtenerCuentaPorNumero(numeroCuenta);

        if (cuenta.isEmpty() || !cuenta.get().estado()) {
            log.error("Cuenta no existente o desactivada");
            throw new MovimientoException(ERROR_ACTUALIZAR_MOVIMIENTO);
        }

        return cuenta.get();
    }

    private MovimientoDto generarNuevoMovimiento(CuentaDto cuenta, MovimientoDto movimientoDto,
                                                 Optional<MovimientoDto> ultimoMovimientoDB) throws MovimientoException {
        var saldoAnterior = cuenta.saldoInicial();
        if (ultimoMovimientoDB.isPresent())
            saldoAnterior = ultimoMovimientoDB.get().saldo();

        var saldo = calcularNuevoSaldo(
                saldoAnterior,
                movimientoDto.valor(),
                MovimientoDto.TipoMovimientoEnum.from(movimientoDto.tipoMovimiento()));

        return new MovimientoDto(
                movimientoDto.movimientoId(),
                cuenta.numero(),
                LocalDateTime.now(),
                movimientoDto.tipoMovimiento(),
                movimientoDto.valor(),
                saldo
        );
    }

    private BigDecimal calcularNuevoSaldo(BigDecimal saldoInicial, BigDecimal valorMovimiento,
                                          MovimientoDto.TipoMovimientoEnum tipoMovimiento) throws MovimientoException {
        if (MovimientoDto.TipoMovimientoEnum.DEPOSITO.equals(tipoMovimiento))
            return saldoInicial.add(valorMovimiento);

        BigDecimal nuevoSaldo = saldoInicial.subtract(valorMovimiento);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0)
            throw new MovimientoException(ERROR_ACTUALIZAR_MOVIMIENTO);

        return nuevoSaldo;
    }

}

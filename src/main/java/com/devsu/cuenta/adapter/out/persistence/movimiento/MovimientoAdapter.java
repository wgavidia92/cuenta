package com.devsu.cuenta.adapter.out.persistence.movimiento;

import com.devsu.cuenta.application.movimiento.model.MovimientoDto;
import com.devsu.cuenta.application.port.movimiento.out.ModificarMovimientoPort;
import com.devsu.cuenta.application.port.movimiento.out.ObtenerMovimientoPort;
import com.devsu.cuenta.exception.MovimientoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
class MovimientoAdapter implements ObtenerMovimientoPort, ModificarMovimientoPort {

    static final String ERROR_ACTUALIZAR_MOVIMIENTO = "Error actualizando registro de movimiento";


    private final static Logger log = LoggerFactory.getLogger(MovimientoAdapter.class);
    private final MovimientoRepository movimientoRepository;

    MovimientoAdapter(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public List<MovimientoDto> obtenerMovimientosPorCuenta(Long cuentaId, String numeroCuenta) {
        return movimientoRepository.findByCuentaId(cuentaId)
                .stream()
                .map(entity -> from(entity, numeroCuenta))
                .toList();
    }

    @Override
    public Optional<MovimientoDto> obtenerUltimoMovimientoPorCuenta(Long cuentaId, String numeroCuenta) {
        return movimientoRepository.findFirstByCuentaIdOrderByMovimientoIdDesc(cuentaId)
                .map(entity -> from(entity, numeroCuenta));
    }

    @Override
    public MovimientoDto crearMovimiento(MovimientoDto movimiento, Long cuentaId) {
        var movimientoDB = new MovimientoEntity(
                null,
                cuentaId,
                movimiento.fecha(),
                movimiento.tipoMovimiento(),
                movimiento.valor(),
                movimiento.saldo()
        );

        return from(movimientoRepository.save(movimientoDB), movimiento.numeroCuenta());
    }

    @Override
    public MovimientoDto modificarMovimiento(MovimientoDto movimientoDto, Long cuentaId) throws MovimientoException {
        var ultimoMovimiento = movimientoRepository.findFirstByCuentaIdOrderByMovimientoIdDesc(cuentaId);
        if (ultimoMovimiento.isEmpty()) {
            log.error("No existe movimiento para modificar");
            throw new MovimientoException(ERROR_ACTUALIZAR_MOVIMIENTO);
        }

        var movimientoDB = ultimoMovimiento.get();
        var movimientoActualizado = new MovimientoEntity(
                movimientoDB.movimientoId(),
                cuentaId,
                LocalDateTime.now(),
                movimientoDto.tipoMovimiento(),
                movimientoDto.valor(),
                movimientoDto.saldo()
        );
        return from(movimientoRepository.save(movimientoActualizado), movimientoDto.numeroCuenta());
    }

    private MovimientoDto from(MovimientoEntity entity, String numeroCuenta) {
        return new MovimientoDto(
                entity.movimientoId(),
                numeroCuenta,
                entity.fecha(),
                entity.tipoMovimiento(),
                entity.valor(),
                entity.saldo()
        );
    }

}

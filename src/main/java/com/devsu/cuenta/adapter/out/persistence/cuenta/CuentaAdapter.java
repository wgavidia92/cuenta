package com.devsu.cuenta.adapter.out.persistence.cuenta;

import com.devsu.cuenta.application.cuenta.model.CuentaDto;
import com.devsu.cuenta.application.port.cuenta.out.ModificarCuentaPort;
import com.devsu.cuenta.application.port.cuenta.out.ObtenerCuentaPort;
import com.devsu.cuenta.exception.CuentaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class CuentaAdapter implements ObtenerCuentaPort, ModificarCuentaPort {

    static final String ERROR_ACTUALIZANDO_CUENTA = "Error al actualizar la cuenta";

    private final CuentaRepository cuentaRepository;
    private final Logger log = LoggerFactory.getLogger(CuentaAdapter.class);

    CuentaAdapter(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public List<CuentaDto> obtenerCuentaPorClienteId(Long clienteId, String nombreCliente) {
        return cuentaRepository.findByClienteId(clienteId)
                .stream()
                .map(entity -> from(entity, nombreCliente))
                .toList();
    }

    @Override
    public CuentaDto crearCuenta(CuentaDto cuentaDto, Long clienteId) {
        var cuenta = new CuentaEntity(
                null,
                clienteId,
                cuentaDto.numero(),
                cuentaDto.tipoCuenta(),
                cuentaDto.saldoInicial(),
                cuentaDto.estado());
        var cuentaAlmacenada = cuentaRepository.save(cuenta);
        return from(cuentaAlmacenada, cuentaDto.nombreCliente());
    }

    @Override
    public CuentaDto actualizarCuenta(String numeroCuenta, CuentaDto cuenta) throws CuentaException {
        var cuentaDB = cuentaRepository.findByNumero(numeroCuenta);
        if (cuentaDB.isEmpty()) {
            log.error("Cuenta a actualizar no encontrada");
            throw new CuentaException(ERROR_ACTUALIZANDO_CUENTA);
        }

        var cuentaAlmacenada = cuentaDB.get();
        var cuentaActualizada = new CuentaEntity(
                cuentaAlmacenada.cuentaId(),
                cuentaAlmacenada.clienteId(),
                cuentaAlmacenada.numero(),
                cuenta.tipoCuenta(),
                cuentaAlmacenada.saldoInicial(),
                cuenta.estado()
        );

        return from(cuentaRepository.save(cuentaActualizada), cuenta.nombreCliente());
    }

    private CuentaDto from(CuentaEntity entity, String nombreCliente) {
        return new CuentaDto(
                entity.cuentaId(),
                nombreCliente,
                entity.numero(),
                entity.tipoCuenta(),
                entity.saldoInicial(),
                entity.estado()
        );
    }

}

package com.devsu.cuenta.adapter.out.persistence.movimiento;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface MovimientoRepository extends CrudRepository<MovimientoEntity, Long> {

    Optional<MovimientoEntity> findFirstByCuentaIdOrderByMovimientoIdDesc(long idCuenta);

    List<MovimientoEntity> findByCuentaId(Long cuentaId);

}

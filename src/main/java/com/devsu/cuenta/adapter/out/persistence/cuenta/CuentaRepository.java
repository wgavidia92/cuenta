package com.devsu.cuenta.adapter.out.persistence.cuenta;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CuentaRepository extends CrudRepository<CuentaEntity, Long> {

    List<CuentaEntity> findByClienteId(Long clienteId);

    Optional<CuentaEntity> findByNumero(String numeroCuenta);

}

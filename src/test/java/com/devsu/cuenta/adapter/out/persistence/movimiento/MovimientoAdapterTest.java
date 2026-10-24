package com.devsu.cuenta.adapter.out.persistence.movimiento;

import com.devsu.cuenta.application.movimiento.model.MovimientoDto;
import com.devsu.cuenta.exception.MovimientoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class MovimientoAdapterTest {

    @MockitoBean
    private MovimientoRepository movimientoRepository;

    private MovimientoAdapter movimientoAdapter;

    @BeforeEach
    void init() {
        movimientoAdapter = new MovimientoAdapter(movimientoRepository);
    }

    @Test
    void obtenerMovimientosPorCuentaTest() {
        Mockito.when(movimientoRepository.findByCuentaId(Mockito.anyLong()))
                .thenReturn(List.of(generarMovimientoEntity()));
        var numeroCuenta = "1234";
        var movimientos = movimientoAdapter.obtenerMovimientosPorCuenta(1L, numeroCuenta);
        Assertions.assertFalse(movimientos.isEmpty());
        Assertions.assertEquals(numeroCuenta, movimientos.getFirst().numeroCuenta());
    }

    @Test
    void obtenerUltimoMovimientoPorCuentaTest() {
        Mockito.when(movimientoRepository.findFirstByCuentaIdOrderByMovimientoIdDesc(Mockito.anyLong()))
                .thenReturn(Optional.of(generarMovimientoEntity()));
        var numeroCuenta = "1234";
        var movimiento = movimientoAdapter.obtenerUltimoMovimientoPorCuenta(1L, numeroCuenta);
        Assertions.assertFalse(movimiento.isEmpty());
        Assertions.assertEquals(numeroCuenta, movimiento.get().numeroCuenta());
    }

    @Test
    void crearMovimientoTest() {
        Mockito.when(movimientoRepository.save(Mockito.any(MovimientoEntity.class)))
                .thenReturn(generarMovimientoEntity());
        var movimiento = movimientoAdapter.crearMovimiento(generarMovimientoDto(), 1L);
        Assertions.assertEquals(1L, movimiento.movimientoId());
    }

    private MovimientoEntity generarMovimientoEntity() {
        return new MovimientoEntity(
                1L,
                1L,
                LocalDateTime.now(),
                "DEPOSITO",
                BigDecimal.ONE,
                BigDecimal.TEN
        );
    }

    @Test
    void modificarMovimientoParaCuentaSinMovimientosTest() {
        Mockito.when(movimientoRepository.findFirstByCuentaIdOrderByMovimientoIdDesc(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        var excepcion = Assertions.assertThrows(MovimientoException.class,
                () -> movimientoAdapter.modificarMovimiento(generarMovimientoDto(), 1L));

        Assertions.assertEquals(MovimientoAdapter.ERROR_ACTUALIZAR_MOVIMIENTO, excepcion.getMessage());
    }

    @Test
    void modidifcarMovimientoTest() throws MovimientoException {
        Mockito.when(movimientoRepository.findFirstByCuentaIdOrderByMovimientoIdDesc(Mockito.anyLong()))
                .thenReturn(Optional.of(generarMovimientoEntity()));
        Mockito.when(movimientoRepository.save(Mockito.any(MovimientoEntity.class)))
                .thenReturn(generarMovimientoEntity());
        var movimiento = movimientoAdapter.modificarMovimiento(generarMovimientoDto(), 1L);
        Assertions.assertEquals(1L, movimiento.movimientoId());
    }

    private MovimientoDto generarMovimientoDto() {
        return new MovimientoDto(
                2L,
                "1234",
                LocalDateTime.now(),
                "DEPOSITO",
                BigDecimal.ONE,
                BigDecimal.TEN
        );
    }

}

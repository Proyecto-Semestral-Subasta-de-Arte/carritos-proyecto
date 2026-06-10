package cl.sda1085.carritos.service;

import cl.sda1085.carritos.dto.CarritoResponseDTO;
import cl.sda1085.carritos.model.Carrito;
import cl.sda1085.carritos.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoService carritoService;

    //Carritos extraídos del DataInitializer
    private Carrito item1Mock;
    private Carrito item4Mock;
    private Carrito item6Mock;

    @BeforeEach
    void setUp() {

        //Estructura: (id, idUsuario, idProducto, cantidad, precioUnitario, fechaAgregado, estado)
        item1Mock = new Carrito(1L, 5L, 1L, 1, new BigDecimal("150000.00"), LocalDateTime.now(), "ACTIVO");
        item4Mock = new Carrito(4L, 12L, 2L, 1, new BigDecimal("300000.00"), LocalDateTime.now().minusDays(2), "COMPRADO");
        item6Mock = new Carrito(6L, 14L, 14L, 1, new BigDecimal("800000.00"), LocalDateTime.now(), "ACTIVO");
    }

    @Test
    @DisplayName("Debería retornar DTO válido para el ítem 1.")
    void obtenerItem1Exitoso() {

        //ARRANGE
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(item1Mock));

        //ACT
        CarritoResponseDTO resultado = carritoService.obtenerPorId(1L);

        //ASSERT
        assertNotNull(resultado);
        assertEquals("ACTIVO", resultado.getEstado());
        assertEquals(5L, resultado.getIdUsuario());
        assertEquals(new BigDecimal("150000.00"), resultado.getPrecioUnitario());
        verify(carritoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería retornar DTO válido para el ítem 4.")
    void obtenerItem4Exitoso() {

        //ARRANGE
        when(carritoRepository.findById(4L)).thenReturn(Optional.of(item4Mock));

        //ACT
        CarritoResponseDTO resultado = carritoService.obtenerPorId(4L);

        //ASSERT
        assertNotNull(resultado);
        assertEquals("COMPRADO", resultado.getEstado());
        assertEquals(12L, resultado.getIdUsuario());
        assertEquals(2L, resultado.getIdProducto());
        verify(carritoRepository, times(1)).findById(4L);
    }

    @Test
    @DisplayName("Debería retornar DTO válido para el ítem 6.")
    void obtenerItem6Exitoso() {

        //ARRANGE
        when(carritoRepository.findById(6L)).thenReturn(Optional.of(item6Mock));

        //ACT
        CarritoResponseDTO resultado = carritoService.obtenerPorId(6L);

        //ASSERT
        assertNotNull(resultado);
        assertEquals("ACTIVO", resultado.getEstado());
        assertEquals(14L, resultado.getIdUsuario());
        assertEquals(new BigDecimal("800000.00"), resultado.getPrecioUnitario());
        verify(carritoRepository, times(1)).findById(6L);
    }

    @Test
    @DisplayName("Debería retornar un 'optional' vacío si el ítem de carrito no existe.")
    void obtenerCarritoInexistente() {

        //ARRANGE
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThrows(RuntimeException.class, () -> {
            carritoService.obtenerPorId(99L);
        });

        verify(carritoRepository, times(1)).findById(99L);
    }
}

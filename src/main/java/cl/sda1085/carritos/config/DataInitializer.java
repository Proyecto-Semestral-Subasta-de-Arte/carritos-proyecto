package cl.sda1085.carritos.config;

import cl.sda1085.carritos.model.Carrito;
import cl.sda1085.carritos.repository.CarritoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CarritoRepository carritoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (carritoRepository.count() > 0) {
            log.info("La base de datos de carritos ya contiene datos. Omitiendo inicialización.");
            return;
        }
        log.info("Iniciando la creación de items de carrito de prueba...");


    Carrito item1 = new Carrito(
            null,
            5L,
            1L,
            1,
            new BigDecimal("150000.00"),
            LocalDateTime.now(),
            "ACTIVO"
    );

    Carrito item2 = new Carrito(
            null,
            5L,
            4L,
            2,
            new BigDecimal("220000.00"),
            LocalDateTime.now(),
            "ACTIVO"
    );

    Carrito item3 = new Carrito(
            null,
            8L,
            8L,
            1,
            new BigDecimal("270000.00"),
            LocalDateTime.now(),
            "ACTIVO"
    );

    Carrito item4 = new Carrito(
            null,
            12L,
            2L,
            1,
            new BigDecimal("300000.00"),
            LocalDateTime.now().minusDays(2),
            "COMPRADO"
    );

    Carrito item5 = new Carrito(
            null,
            6L,
            10L,
            3,
            new BigDecimal("210000.00"),
            LocalDateTime.now().minusDays(5),
            "ABANDONADO"
    );

    Carrito item6 = new Carrito(
            null,
            14L,
            14L,
            1,
            new BigDecimal("800000.00"),
            LocalDateTime.now(),
            "ACTIVO"
    );

    Carrito item7 = new Carrito(
            null,
            5L,
            3L,
            1,
            new BigDecimal("500000.00"),
            LocalDateTime.now().minusDays(3),
            "COMPRADO"
    );

    Carrito item8 = new Carrito(
            null,
            8L,
            9L,
            1,
            new BigDecimal("600000.00"),
            LocalDateTime.now().minusDays(7),
            "ABANDONADO"
    );

        //Agregamos todos los elementos a la lista de guardado
        carritoRepository.saveAll(List.of(item1, item2, item3, item4, item5, item6, item7, item8));

        log.info("¡Éxito! Se han registrado 8 registros en la base de datos.");
        log.info("Sincronización lógica completada: Productos y precios mapeados correctamente.");
    }
}

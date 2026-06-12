package cl.sda1085.carritos.util;

import cl.sda1085.carritos.dto.CarritoRequestDTO;
import cl.sda1085.carritos.dto.CarritoResponseDTO;
import cl.sda1085.carritos.model.Carrito;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CarritoDataFaker {

    private static final Faker faker = new Faker();

    public static Carrito createFakeEntity() {
        return new Carrito(
                faker.number().randomNumber(4, false),
                faker.number().randomNumber(3, false),
                faker.number().randomNumber(3, false),
                faker.number().numberBetween(1, 5),
                BigDecimal.valueOf(faker.number().randomDouble(2, 5000, 200000)),
                LocalDateTime.now(),
                "ACTIVO"
        );
    }

    public static CarritoRequestDTO createFakeRequestDTO() {
        CarritoRequestDTO dto = new CarritoRequestDTO();
        dto.setIdUsuario(faker.number().randomNumber(3, false));
        dto.setIdProducto(faker.number().randomNumber(3, false));
        dto.setCantidad(faker.number().numberBetween(1, 5));
        dto.setPrecioUnitario(BigDecimal.valueOf(faker.number().randomDouble(2, 5000, 200000)));
        return dto;
    }

    public static CarritoResponseDTO createFakeResponseDTO(Carrito entity) {
        BigDecimal total = entity.getPrecioUnitario().multiply(BigDecimal.valueOf(entity.getCantidad()));
        return new CarritoResponseDTO(
                entity.getId(),
                entity.getIdUsuario(),
                entity.getIdProducto(),
                entity.getCantidad(),
                entity.getPrecioUnitario(),
                total,
                entity.getEstado(),
                entity.getFechaAgregado()
        );
    }
}

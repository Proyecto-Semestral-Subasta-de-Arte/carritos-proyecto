package cl.sda1085.carritos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Modelo requerido (JSON) para agregar o actualizar un producto dentro del carrito.")
public class CarritoRequestDTO {

        @Schema(description = "Identificador único del usuario dueño del carrito.", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "ID de usuario es obligatorio.")
        private Long idUsuario;

        @Schema(description = "Identificador único del producto proveniente del catálogo.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "ID de producto es obligatorio.")
        private Long idProducto;

        @Schema(description = "Unidades del artículo que se desean adquirir.", example = "2", defaultValue = "1")
        @Min(value = 1, message = "La cantidad mínima es '1'.")
        private Integer cantidad;

        @Schema(description = "Precio unitario histórico del producto al momento de agregarlo.", example = "150000.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El precio es obligatorio.")
        @Positive(message = "El precio debe ser mayor a cero.")
        private BigDecimal precioUnitario;
}

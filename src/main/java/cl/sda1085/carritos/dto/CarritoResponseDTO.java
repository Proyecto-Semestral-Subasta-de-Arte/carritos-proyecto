package cl.sda1085.carritos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(description = "Estructura de salida enriquecida con hipervínculos relacionales y cálculos de subtotal.")

public class CarritoResponseDTO extends RepresentationModel<CarritoResponseDTO> {

    @Schema(description = "Identificador único incremental autogenerado en la tabla de carritos.", example = "1")
    private Long id;

    @Schema(description = "ID del usuario propietario.", example = "5")
    private Long idUsuario;

    @Schema(description = "ID del producto seleccionado.", example = "1")
    private Long idProducto;

    @Schema(description = "Cantidad de unidades guardadas.", example = "1")
    private Integer cantidad;

    @Schema(description = "Precio por unidad cobrado.", example = "150000.00")
    private BigDecimal precioUnitario;

    @Schema(description = "Monto acumulado (Precio Unitario * Cantidad).", example = "150000.00")
    private BigDecimal subtotal;  //Campo calculado

    @Schema(description = "Estado actual del registro dentro del sistema transaccional.", example = "ACTIVO", allowableValues = {"ACTIVO", "COMPRADO", "ABANDONADO"})
    private String estado;

    @Schema(description = "Fecha de la última captura cronológica del ítem.", example = "2026-06-11T21:40:00")
    private LocalDateTime fechaAgregado;
}

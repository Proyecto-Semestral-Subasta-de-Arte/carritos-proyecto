package cl.sda1085.carritos.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@EqualsAndHashCode(callSuper = false)
@Builder
public class CarritoResponseDTO extends RepresentationModel<CarritoResponseDTO> {

    private Long id;
    private Long idUsuario;
    private Long idProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;  //Campo calculado
    private String estado;
    private LocalDateTime fechaAgregado;
}

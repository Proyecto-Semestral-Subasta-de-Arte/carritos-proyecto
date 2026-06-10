package cl.sda1085.carritos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "carritos")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idUsuario;

    @Column(nullable = false)
    private Long idProducto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private LocalDateTime fechaAgregado;

    @Column(nullable = false)
    private String estado;  //ACTIVO, COMPRADO y ABANDONADO.

    @PrePersist
    protected void onCreate() {
        this.fechaAgregado = LocalDateTime.now();
        if (this.estado == null) this.estado = "ACTIVO";
    }
}

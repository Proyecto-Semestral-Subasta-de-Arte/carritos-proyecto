package cl.sda1085.carritos.service;

import cl.sda1085.carritos.repository.CarritoRepository;
import cl.sda1085.carritos.dto.CarritoRequestDTO;
import cl.sda1085.carritos.dto.CarritoResponseDTO;
import cl.sda1085.carritos.model.Carrito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CarritoService {

    private final CarritoRepository carritoRepository;

    private CarritoResponseDTO convertirADTO(Carrito carrito) {
        //cálculo subtotal (precio * cantidad)
        BigDecimal cantidadDecimal = new BigDecimal(carrito.getCantidad());
       BigDecimal subtotal = carrito.getPrecioUnitario().multiply(cantidadDecimal);

        return new CarritoResponseDTO(
                carrito.getId(),
                carrito.getIdUsuario(),
                carrito.getIdProducto(),
                carrito.getCantidad(),
                carrito.getPrecioUnitario(),
                subtotal,
                carrito.getEstado(),
                carrito.getFechaAgregado()
        );
    }

    public List<CarritoResponseDTO> obtenerTodos(){
        return carritoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<CarritoResponseDTO> obtenerPorId(Long id) {
        return carritoRepository.findById(id)
                .map(this::convertirADTO);
    }

    public CarritoResponseDTO guardar(CarritoRequestDTO dto) {
        Carrito carrito = new Carrito();
        carrito.setIdUsuario(dto.getIdUsuario());
        carrito.setIdProducto(dto.getIdProducto());
        carrito.setCantidad(dto.getCantidad() != null ? dto.getCantidad() : 1);
        carrito.setPrecioUnitario(dto.getPrecioUnitario());

        return convertirADTO(carritoRepository.save(carrito));
    }

    public Optional<CarritoResponseDTO> actualizar(Long id,CarritoRequestDTO dto) {
        return carritoRepository.findById(id).map(existente -> {
            existente.setCantidad(dto.getCantidad());
            existente.setPrecioUnitario(dto.getPrecioUnitario());

            return convertirADTO(carritoRepository.save(existente));
        });
    }

    // 5. ELIMINAR (CRUD Estándar)
    public void eliminar(Long id) {
        carritoRepository.deleteById(id);
    }

    //CRUD personalizado
    public List<CarritoResponseDTO> obtenerCarritoPorIdUsuario(Long idUsuario) {
        return carritoRepository.findByIdUsuarioAndEstado(idUsuario, "ACTIVO")
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void vaciarCarrito(Long idUsuario) {
        carritoRepository.deleteByIdUsuario(idUsuario);
    }

    //obtener el total en dinero
    public BigDecimal calcularTotalCarrito(Long idUsuario) {
        return carritoRepository.findByIdUsuarioAndEstado(idUsuario, "ACTIVO")
                .stream()
                .map(item -> item.getPrecioUnitario().multiply(new BigDecimal(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

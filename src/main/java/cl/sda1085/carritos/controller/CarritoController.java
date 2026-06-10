package cl.sda1085.carritos.controller;

import cl.sda1085.carritos.dto.CarritoRequestDTO;
import cl.sda1085.carritos.dto.CarritoResponseDTO;
import cl.sda1085.carritos.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/carritos")
@RequiredArgsConstructor
@Tag(name = "Carritos", description = "Controlador para la gestión y trazabilidad de ítems en el carrito de compras.")
public class CarritoController {

    //Conexión con 'service'
    private final CarritoService carritoService;

    @Operation(summary = "Obtener un ítem de carrito por su ID", description = "Retorna los detalles de un ítem del carrito junto con los enlaces HATEOAS de navegación.")
    @ApiResponse(responseCode = "200", description = "Ítem localizado con éxito.")
    @ApiResponse(responseCode = "404", description = "El ítem del carrito solicitado no existe.")
    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> obtenerCarritoPorId(@PathVariable Long id) {

        //Buscar y desenvolver el 'optional' de forma segura
        CarritoResponseDTO dto = carritoService.obtenerPorId(id);

        //HATEOAS
        dto.add(linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(id)).withSelfRel());
        dto.add(linkTo(methodOn(CarritoController.class).listarTodosLosCarritos()).withRel("lista-completa-carritos"));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Listar todos los ítems de carritos.", description = "Retorna el listado global de todos los registros en el sistema.")
    @GetMapping
    public ResponseEntity<List<CarritoResponseDTO>> listarTodosLosCarritos() {
        return ResponseEntity.ok(carritoService.obtenerTodos());
    }

    //------------------------------
    //CRUD estándar
    //------------------------------

    //Obtener todos los carritos
    /*@GetMapping
    public ResponseEntity<List<CarritoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(carritoService.obtenerTodos());
    }*/

    /*//Obtener carrito por ID
    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carritoService.obtenerPorId(id));
    }*/

    //Guardar (crear) nuevo carrito
    @PostMapping
    public ResponseEntity<CarritoResponseDTO> agregarItem(@Valid @RequestBody CarritoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.guardar(dto));
    }

    //Actualizar carrito existente
    @PutMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> actualizarItem(
            @PathVariable Long id, @Valid @RequestBody CarritoRequestDTO dto) {

        return ResponseEntity.ok(carritoService.actualizar(id, dto));
    }

    //Eliminar carrito
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        carritoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    //------------------------------
    //CRUD personalizado
    //------------------------------

    //Buscar ítems por usuario y por su estado
    //GET /api/carritos/usuario/1/estado/ACTIVO
    @GetMapping("/usuario/{idUsuario}/estado/{estado}")
    public ResponseEntity<List<CarritoResponseDTO>> obtenerCarritoPorUsuario(
            @PathVariable Long idUsuario, @PathVariable String estado) {

        return ResponseEntity.ok(carritoService.obtenerCarritoPorIdUsuario(idUsuario, estado));
    }

    //Contar cantidad de productos en el carrito
    //GET /api/carritos/usuario/1/conteo?estado=ACTIVO
    @GetMapping("/usuario/{idUsuario}/conteo")
    public ResponseEntity<Long> contarPorUsuarioYEstado(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "ACTIVO") String estado) {

        return ResponseEntity.ok(carritoService.contarPorUsuarioYEstado(idUsuario, estado));
    }

    //Buscar un ítem específico del catálogo en el carrito
    //GET /api/carritos/usuario/1/producto/5/estado/ACTIVO
    @GetMapping("/usuario/{idUsuario}/producto/{idProducto}/estado/{estado}")
    public ResponseEntity<CarritoResponseDTO> buscarItemEspecifico(
            @PathVariable Long idUsuario,
            @PathVariable Long idProducto,
            @PathVariable String estado) {

        return ResponseEntity.ok(carritoService.buscarItemEspecifico(idUsuario, idProducto, estado));
    }

    //Obtener el monto total a pagar por el usuario
    //GET /api/carritos/usuario/1/total
    @GetMapping("/usuario/{idUsuario}/total")
    public ResponseEntity<BigDecimal> obtenerTotalCarrito(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(carritoService.calcularTotalCarrito(idUsuario));
    }

    //Vaciar por completo el carrito de un usuario
    //DELETE /api/carritos/usuario/1
    @DeleteMapping("/usuario/{idUsuario}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long idUsuario) {
        carritoService.vaciarCarritoPorIdUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }
}

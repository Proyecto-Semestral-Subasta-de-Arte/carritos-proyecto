package cl.sda1085.carritos.controller;

import cl.sda1085.carritos.assembler.CarritoModelAssembler;
import cl.sda1085.carritos.dto.CarritoRequestDTO;
import cl.sda1085.carritos.dto.CarritoResponseDTO;
import cl.sda1085.carritos.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/carritos")
@RequiredArgsConstructor
@Tag(name = "Carritos", description = "Controlador para la gestión y trazabilidad de ítems en el carrito de compras.")
public class CarritoController {

    //Conexión con 'service'
    private final CarritoService carritoService;

    //Incorporación del ensamblador premium
    private final CarritoModelAssembler assembler;


    //------------------------------
    //CRUD estándar
    //------------------------------

    //Obtener todos los carritos
    @Operation(summary = "Listar todos los ítems de carritos.", description = "Retorna el listado global de todos los registros en el sistema.")
    @GetMapping
    public ResponseEntity<List<CarritoResponseDTO>> listarTodosLosCarritos() {
        // MODIFICACIÓN: Se añade el mapeo estructural HATEOAS en flujo de colección
        List<CarritoResponseDTO> resultados = carritoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultados);
    }

    //Obtener carrito por ID
    @Operation(summary = "Obtener un ítem de carrito por su ID", description = "Retorna los detalles de un ítem del carrito junto con los enlaces HATEOAS de navegación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ítem localizado con éxito."),
            @ApiResponse(responseCode = "404", description = "El ítem del carrito solicitado no existe.")
    })

    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> obtenerCarritoPorId(@PathVariable Long id) {
        CarritoResponseDTO dto = carritoService.obtenerPorId(id);  //Buscar y desenvolver el 'optional' de forma segura
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    //Guardar (crear) nuevo carrito
    @PostMapping
    @Operation(summary = "Agregar producto al carrito", description = "Registra un nuevo producto o incrementa la cantidad acumulada si el ítem ya existía en estado ACTIVO.")
    @ApiResponse(responseCode = "201", description = "Producto mapeado e inyectado con éxito.")
    public ResponseEntity<CarritoResponseDTO> agregarItem(@Valid @RequestBody CarritoRequestDTO dto) {
        CarritoResponseDTO creado = carritoService.guardar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(creado));
    }

    //Actualizar carrito existente
    @PutMapping("/{id}")
    @Operation(summary = "Modificar un registro existente", description = "Actualiza de forma manual las unidades o los montos históricos de un ítem.")
    public ResponseEntity<CarritoResponseDTO> actualizarItem(
            @PathVariable Long id, @Valid @RequestBody CarritoRequestDTO dto) {

        CarritoResponseDTO actualizado = carritoService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    //Eliminar carrito
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover ítem por ID", description = "Elimina físicamente el registro de la base de datos de carritos.")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        carritoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    //------------------------------
    //CRUD personalizado
    //------------------------------

    //Buscar ítems por usuario y por su estado
    //GET --> /api/carritos/usuario/1/estado/ACTIVO
    @GetMapping("/usuario/{idUsuario}/estado/{estado}")
    public ResponseEntity<List<CarritoResponseDTO>> obtenerCarritoPorUsuario(
            @PathVariable Long idUsuario, @PathVariable String estado) {

        return ResponseEntity.ok(carritoService.obtenerCarritoPorIdUsuario(idUsuario, estado));
    }

    //Contar cantidad de productos en el carrito
    //GET --> /api/carritos/usuario/1/conteo?estado=ACTIVO
    @GetMapping("/usuario/{idUsuario}/conteo")
    public ResponseEntity<Long> contarPorUsuarioYEstado(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "ACTIVO") String estado) {

        return ResponseEntity.ok(carritoService.contarPorUsuarioYEstado(idUsuario, estado));
    }

    //Buscar un ítem específico del catálogo en el carrito
    //GET --> /api/carritos/usuario/1/producto/5/estado/ACTIVO
    @GetMapping("/usuario/{idUsuario}/producto/{idProducto}/estado/{estado}")
    public ResponseEntity<CarritoResponseDTO> buscarItemEspecifico(
            @PathVariable Long idUsuario,
            @PathVariable Long idProducto,
            @PathVariable String estado) {

        return ResponseEntity.ok(carritoService.buscarItemEspecifico(idUsuario, idProducto, estado));
    }

    //Obtener el monto total a pagar por el usuario
    //GET --> /api/carritos/usuario/1/total
    @GetMapping("/usuario/{idUsuario}/total")
    public ResponseEntity<BigDecimal> obtenerTotalCarrito(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(carritoService.calcularTotalCarrito(idUsuario));
    }

    //Vaciar por completo el carrito de un usuario
    //DELETE --> /api/carritos/usuario/1
    @DeleteMapping("/usuario/{idUsuario}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long idUsuario) {
        carritoService.vaciarCarritoPorIdUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }
}

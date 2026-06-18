package cl.sda1085.carritos.controller;

import cl.sda1085.carritos.assembler.CarritoModelAssembler;
import cl.sda1085.carritos.dto.CarritoRequestDTO;
import cl.sda1085.carritos.dto.CarritoResponseDTO;
import cl.sda1085.carritos.service.CarritoService;
import cl.sda1085.carritos.util.CarritoDataFaker;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarritoController.class)
@AutoConfigureMockMvc(addFilters = false)  //Desactivar seguridad para testing puro de la API REST
@DisplayName("CarritoController – Tests Unitarios con HATEOAS.")

class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Faker faker = new Faker();

    @MockitoBean
    private CarritoService carritoService;

    @MockitoBean
    private CarritoModelAssembler assembler;

    private CarritoResponseDTO responseDTO;
    private CarritoRequestDTO requestDTO;
    private Long itemId;
    private Long usuarioId;

    @BeforeEach
    void setUp() {
        cl.sda1085.carritos.model.Carrito entidadFake = CarritoDataFaker.createFakeEntity();
        itemId = entidadFake.getId();
        usuarioId = entidadFake.getIdUsuario();

        responseDTO = CarritoDataFaker.createFakeResponseDTO(entidadFake);
        requestDTO = CarritoDataFaker.createFakeRequestDTO();

        //Configuración estandarizada del comportamiento del simulador del ensamblador
        given(assembler.toModel(any(CarritoResponseDTO.class))).willAnswer(invocation -> {
            CarritoResponseDTO dto = invocation.getArgument(0);
            dto.removeLinks();
            dto.add(Link.of("/api/carritos/" + dto.getId()).withSelfRel());
            dto.add(Link.of("/api/carritos").withRel("lista-completa-carritos"));
            return dto;
        });
    }

    @Test
    @DisplayName("GET /api/carritos/{id} → 200 OK con links HATEOAS si el ítem existe.")
    void obtenerPorId_exitoso() throws Exception {
        given(carritoService.obtenerPorId(itemId)).willReturn(responseDTO);

        mockMvc.perform(get("/api/carritos/{id}", itemId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId.intValue())))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/carritos/" + itemId)));

        verify(carritoService).obtenerPorId(itemId);
    }

    @Test
    @DisplayName("POST /api/carritos → 201 Created al agregar nuevo producto.")
    void agregarItem_exitoso() throws Exception {
        given(carritoService.guardar(any(CarritoRequestDTO.class))).willReturn(responseDTO);

        mockMvc.perform(post("/api/carritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemId.intValue())));

        verify(carritoService).guardar(any(CarritoRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /api/carritos/{id} → 200 OK al actualizar cantidad.")
    void actualizarItem_exitoso() throws Exception {
        given(carritoService.actualizar(eq(itemId), any(CarritoRequestDTO.class))).willReturn(responseDTO);

        mockMvc.perform(put("/api/carritos/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(carritoService).actualizar(eq(itemId), any(CarritoRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/carritos/{id} → 204 No Content al remover ítem.")
    void eliminarItem_exitoso() throws Exception {
        doNothing().when(carritoService).eliminar(itemId);

        mockMvc.perform(delete("/api/carritos/{id}", itemId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(carritoService).eliminar(itemId);
    }

    @Test
    @DisplayName("GET /api/carritos/usuario/{id}/total → 200 OK con monto total.")
    void obtenerTotalCarrito_exitoso() throws Exception {
        BigDecimal totalSimulado = BigDecimal.valueOf(450000.00);
        given(carritoService.calcularTotalCarrito(usuarioId)).willReturn(totalSimulado);

        mockMvc.perform(get("/api/carritos/usuario/{idUsuario}/total", usuarioId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(totalSimulado.toString()));

        verify(carritoService).calcularTotalCarrito(usuarioId);
    }
}

package cl.sda1085.carritos.assembler;

import cl.sda1085.carritos.controller.CarritoController;
import cl.sda1085.carritos.dto.CarritoResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CarritoModelAssembler implements RepresentationModelAssembler<CarritoResponseDTO, CarritoResponseDTO> {

    @Override
    public CarritoResponseDTO toModel(CarritoResponseDTO dto) {

        //Enlace autorreferencial (self)
        dto.add(linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(dto.getId())).withSelfRel());

        //Enlace al recurso de colección global
        dto.add(linkTo(methodOn(CarritoController.class).listarTodosLosCarritos()).withRel("lista-completa-carritos"));

        return dto;
    }
}

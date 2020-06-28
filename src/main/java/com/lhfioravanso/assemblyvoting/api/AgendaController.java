package com.lhfioravanso.assemblyvoting.api;

import com.lhfioravanso.assemblyvoting.dto.AgendaRequestDto;
import com.lhfioravanso.assemblyvoting.dto.AgendaResponseDto;
import com.lhfioravanso.assemblyvoting.service.AgendaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@Api(value = "agenda")
@RequestMapping(value = "api/v1/agenda", produces = "application/json")
public class AgendaController {

    private final AgendaService agendaService;

    @Autowired
    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @ApiOperation(value="get all agendas", response = AgendaResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Agendas found.")
    })
    @GetMapping()
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(this.agendaService.listAgendas());
    }

    @ApiOperation(value="get one agenda", response = AgendaResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Agenda found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id){
        return ResponseEntity.ok(this.agendaService.getAgenda(id));
    }

    @ApiOperation(value="create one agenda", response = AgendaResponseDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Agenda successfully created.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody AgendaRequestDto agenda) throws URISyntaxException {
        AgendaResponseDto response = this.agendaService.createAgenda(agenda);
        return ResponseEntity.created(new URI(response.getId())).body(response);
    }

}

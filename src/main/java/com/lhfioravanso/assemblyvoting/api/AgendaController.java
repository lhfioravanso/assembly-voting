package com.lhfioravanso.assemblyvoting.api;

import com.lhfioravanso.assemblyvoting.dto.AgendaRequestDto;
import com.lhfioravanso.assemblyvoting.dto.AgendaResponseDto;
import com.lhfioravanso.assemblyvoting.service.AgendaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "agenda")
@RequestMapping(value = "v1/agenda", produces = "application/json")
public class AgendaController {

    private final AgendaService agendaService;

    @Autowired
    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @ApiOperation(value="get all agendas", response = AgendaResponseDto.class)
    @GetMapping()
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(this.agendaService.listAgendas());
    }

    @ApiOperation(value="get one agenda", response = AgendaResponseDto.class)
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id){
        return ResponseEntity.ok(this.agendaService.getAgenda(id));
    }

    @ApiOperation(value="create one agenda", response = AgendaResponseDto.class)
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody AgendaRequestDto agenda){
        return ResponseEntity.ok(this.agendaService.createAgenda(agenda));
    }

}

package com.lhfioravanso.assemblyvoting.api;

import com.lhfioravanso.assemblyvoting.dto.AgendaRequestDto;
import com.lhfioravanso.assemblyvoting.dto.AgendaResponseDto;
import com.lhfioravanso.assemblyvoting.service.AgendaService;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class AgendaControllerTest {

    @Mock
    public AgendaService agendaService;

    @InjectMocks
    public AgendaController agendaController;

    @Test
    public void shouldReturnZeroAgendas() {
        ResponseEntity<?> resp = agendaController.getAll();

        assertEquals(resp.getStatusCode(), HttpStatus.OK);
        assertEquals(0, ((LinkedList<?>) resp.getBody()).size());
    }

    @Test
    public void shouldReturnAgendas() {
        List<AgendaResponseDto> listDto = new ArrayList<>();
        listDto.add(new AgendaResponseDto("1","test"));

        Mockito.when(agendaService.listAgendas()).thenReturn(listDto);

        ResponseEntity<?> resp = agendaController.getAll();

        assertEquals(resp.getStatusCode(), HttpStatus.OK);
        assertEquals(1, ((ArrayList<?>) resp.getBody()).size());
    }

    @Test
    public void shouldCreateAgenda() {
        String agendaName = "test";
        AgendaRequestDto req = new AgendaRequestDto(agendaName);
        AgendaResponseDto resp = new AgendaResponseDto(new ObjectId().toHexString(), agendaName);
        Mockito.when(agendaService.createAgenda(req)).thenReturn(resp);

        ResponseEntity<?> response = agendaController.create(req);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }
}
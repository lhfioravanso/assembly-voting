package com.lhfioravanso.assemblyvoting.service;

import com.lhfioravanso.assemblyvoting.dto.AgendaRequestDto;
import com.lhfioravanso.assemblyvoting.dto.AgendaResponseDto;
import com.lhfioravanso.assemblyvoting.entity.Agenda;
import com.lhfioravanso.assemblyvoting.exception.NotFoundException;
import com.lhfioravanso.assemblyvoting.repository.AgendaRepository;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class AgendaServiceTest {

    @Mock
    public AgendaRepository agendaRepository;

    @Mock
    public ModelMapper modelMapper;

    @InjectMocks
    public AgendaServiceImpl agendaService;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(agendaService, "modelMapper", new ModelMapper());
    }

    @Test
    public void shouldReturnZeroAgendas() {
        List<AgendaResponseDto> resp = agendaService.listAgendas();
        assertEquals(0, resp.size());
    }

    @Test
    public void shouldReturnAgendas() {
        List<Agenda> agendas = new ArrayList<>();
        agendas.add(new Agenda("test1"));
        agendas.add(new Agenda("test2"));

        Mockito.when(agendaRepository.findAll()).thenReturn(agendas);

        List<AgendaResponseDto> resp = agendaService.listAgendas();
        assertEquals(2, resp.size());
    }

    @Test
    public void shouldReturnOneAgenda() {
        ObjectId id = new ObjectId();
        Agenda agenda = new Agenda("test1");
        agenda.setId(id);

        Mockito.when(agendaRepository.findById(id)).thenReturn(java.util.Optional.of(agenda));

        AgendaResponseDto resp = agendaService.getAgenda(id.toHexString());
        assertEquals(id.toHexString(), resp.getId());
        assertEquals(agenda.getName(), resp.getName());
    }

    @Test
    public void shouldCreateAgenda() {
        ObjectId id = new ObjectId();
        String name = "test1";

        Agenda agenda = new Agenda(name);
        agenda.setId(id);

        Mockito.when(agendaRepository.insert(new Agenda(name))).thenReturn(agenda);

        AgendaRequestDto req = new AgendaRequestDto();
        req.setName(name);
        AgendaResponseDto resp = agendaService.createAgenda(req);

        assertEquals(id.toHexString(), resp.getId());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundException(){
        ObjectId id = new ObjectId();
        Mockito.when(agendaRepository.findById(id)).thenThrow(new NotFoundException("Agenda not found."));

        agendaService.getAgenda(id.toHexString());
    }
}
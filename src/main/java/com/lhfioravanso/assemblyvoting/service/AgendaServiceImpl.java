package com.lhfioravanso.assemblyvoting.service;

import com.lhfioravanso.assemblyvoting.dto.AgendaRequestDto;
import com.lhfioravanso.assemblyvoting.dto.AgendaResponseDto;
import com.lhfioravanso.assemblyvoting.entity.Agenda;
import com.lhfioravanso.assemblyvoting.exception.NotFoundException;
import com.lhfioravanso.assemblyvoting.repository.AgendaRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendaServiceImpl implements AgendaService {

    private final AgendaRepository agendaRepository;
    private final ModelMapper agendaMapper;

    @Autowired
    public AgendaServiceImpl(AgendaRepository agendaRepository, ModelMapper agendaMapper) {
        this.agendaRepository = agendaRepository;
        this.agendaMapper = agendaMapper;
    }

    @Override
    public List<AgendaResponseDto> listAgendas() {
        List<Agenda> agendaList = this.agendaRepository.findAll();

        return agendaList.stream().map(
            agenda -> agendaMapper.map(agenda, AgendaResponseDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public AgendaResponseDto getAgenda(String id) {
        Agenda agenda = this.agendaRepository.findById(new ObjectId(id)).
                orElseThrow(() -> new NotFoundException("Agenda not found."));

        return agendaMapper.map(agenda, AgendaResponseDto.class);
    }

    @Override
    public AgendaResponseDto createAgenda(AgendaRequestDto dto) {
        Agenda agenda = new Agenda(dto.getName());
        agenda = agendaRepository.insert(agenda);

        return agendaMapper.map(agenda, AgendaResponseDto.class);
    }
}

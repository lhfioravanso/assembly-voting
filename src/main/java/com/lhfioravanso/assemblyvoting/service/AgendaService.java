package com.lhfioravanso.assemblyvoting.service;

import com.lhfioravanso.assemblyvoting.dto.AgendaRequestDto;
import com.lhfioravanso.assemblyvoting.dto.AgendaResponseDto;

import java.util.List;

public interface AgendaService {
    List<AgendaResponseDto> listAgendas();
    AgendaResponseDto getAgenda(String id);
    AgendaResponseDto createAgenda(AgendaRequestDto dto);
}

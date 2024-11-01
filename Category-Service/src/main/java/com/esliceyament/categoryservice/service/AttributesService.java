package com.esliceyament.categoryservice.service;

import com.esliceyament.categoryservice.dto.AttributesDto;
import com.esliceyament.categoryservice.payload.AttributesPayload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttributesService {

    AttributesDto addAttribute(AttributesPayload attribute);

    void addAttributes(List<AttributesPayload> payloads);

    AttributesDto updateAttribute(Long id, AttributesDto dto);

    AttributesDto getAttribute(Long id);
    List<AttributesDto> getAllAttributes(String name);

    AttributesDto getAttributesByName(String name);
}

package com.esliceyament.categoryservice.service.implementation;

import com.esliceyament.categoryservice.dto.AttributesDto;
import com.esliceyament.categoryservice.entity.AttributeValues;
import com.esliceyament.categoryservice.entity.Attributes;
import com.esliceyament.categoryservice.entity.Category;
import com.esliceyament.categoryservice.exception.NotFoundException;
import com.esliceyament.categoryservice.mapper.AttributesMapper;
import com.esliceyament.categoryservice.payload.AttributesPayload;
import com.esliceyament.categoryservice.repository.AttributesRepository;
import com.esliceyament.categoryservice.repository.CategoryRepository;
import com.esliceyament.categoryservice.service.AttributesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AttributesServiceImpl implements AttributesService {
    private final AttributesRepository repository;
    private final CategoryRepository categoryRepository;
    private final AttributesMapper mapper;

    public AttributesDto addAttribute(AttributesPayload attribute) {
        Category category = categoryRepository.findByName(attribute.getCategoryName())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Optional<Attributes> existingAttributeOpt = category.getAttributes().stream()
                .filter(attr -> attr.getName().equals(attribute.getName())).findFirst();

        if (existingAttributeOpt.isPresent()) {
            Attributes existingAttribute = existingAttributeOpt.get();
            Set<AttributeValues> newValues = new HashSet<>(attribute.getValues());
            existingAttribute.getValues().addAll(newValues);
            repository.save(existingAttribute);
            return mapper.toDto(existingAttribute);
        }
        else {
            Attributes newAttribute = new Attributes();
            newAttribute.setName(attribute.getName());
            newAttribute.setValues(attribute.getValues());
            newAttribute.setCategory(category);
            category.getAttributes().add(newAttribute);
            repository.save(newAttribute);
            return mapper.toDto(newAttribute);
        }
    }

    public void addAttributes(List<AttributesPayload> payloads) {
        Category category = categoryRepository.findByName(payloads.get(0).getCategoryName())
                .orElseThrow(() -> new NotFoundException("Category not found!"));

        for (AttributesPayload payload : payloads) {
            if (category.getAttributes() != null) {
                Optional<Attributes> existingAttributeOpt = category.getAttributes().stream()
                        .filter(attr -> attr.getName().equals(payload.getName())).findFirst();

                if (existingAttributeOpt.isPresent()) {
                    Attributes existingAttribute = existingAttributeOpt.get();
                    Set<AttributeValues> newValues = new HashSet<>(payload.getValues());
                    existingAttribute.getValues().addAll(newValues);
                    repository.save(existingAttribute);
                } else {
                    Attributes newAttribute = new Attributes();
                    newAttribute.setName(payload.getName());
                    newAttribute.setValues(payload.getValues());
                    newAttribute.setCategory(category);
                    category.getAttributes().add(newAttribute);
                    repository.save(newAttribute);
                }
            }
        }

    }

    public AttributesDto updateAttribute(Long id, AttributesDto dto) {
        Attributes attributes = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id + " id Attribute not found!"));
        mapper.updateAttributeFromDto(dto, attributes);
        repository.save(attributes);
        return mapper.toDto(attributes);
    }

    public AttributesDto getAttribute(Long id) {
        Attributes attributes = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attribute not found!"));
        return mapper.toDto(attributes);
    }

    public List<AttributesDto> getAllAttributes(String name) {
        return repository.findAllByCategoryName(name).stream()
                .map(mapper::toDto).toList();
    }

    @Override
    public AttributesDto getAttributesByName(String name) {
        return mapper.toDto(repository.findByName(name));
    }


}

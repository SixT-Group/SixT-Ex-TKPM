package com.example.sixt.services.impl;

import com.example.sixt.models.DepartmentEntity;
import com.example.sixt.services.DataImportService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class JsonDepartmentImportService implements DataImportService<DepartmentEntity> {

    private final ObjectMapper objectMapper;

    @Autowired
    public JsonDepartmentImportService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<DepartmentEntity> importFromFile(MultipartFile file) throws Exception {
        return objectMapper.readValue(file.getInputStream(), new TypeReference<List<DepartmentEntity>>() {});
    }

    @Override
    public boolean supports(String contentType) {
        return contentType != null && contentType.equals("application/json");
    }
} 
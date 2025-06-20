package com.example.sixt.services.impl;

import com.example.sixt.models.DepartmentEntity;
import com.example.sixt.services.DataExportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class JsonDepartmentExportService implements DataExportService<DepartmentEntity> {

    private final ObjectMapper objectMapper;

    @Autowired
    public JsonDepartmentExportService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public InputStreamResource exportToStream(List<DepartmentEntity> departments, String filename) {
        byte[] jsonBytes = exportToBytes(departments);
        return new InputStreamResource(new ByteArrayInputStream(jsonBytes));
    }

    @Override
    public byte[] exportToBytes(List<DepartmentEntity> departments) {
        try {
            return objectMapper.writeValueAsBytes(departments);
        } catch (Exception e) {
            throw new RuntimeException("Error converting departments to JSON", e);
        }
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public String getFileExtension() {
        return ".json";
    }
} 
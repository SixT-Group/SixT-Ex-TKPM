package com.example.sixt.services.impl;

import com.example.sixt.models.DepartmentEntity;
import com.example.sixt.services.DataExportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CsvDepartmentExportService implements DataExportService<DepartmentEntity> {

    @Override
    public InputStreamResource exportToStream(List<DepartmentEntity> departments, String filename) {
        byte[] csvBytes = exportToBytes(departments);
        return new InputStreamResource(new ByteArrayInputStream(csvBytes));
    }

    @Override
    public byte[] exportToBytes(List<DepartmentEntity> departments) {
        StringWriter writer = new StringWriter();
        writer.append("ID,Name\n");

        for (DepartmentEntity department : departments) {
            writer.append(String.valueOf(department.getId()))
                  .append(",")
                  .append(department.getName())
                  .append("\n");
        }

        return writer.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getContentType() {
        return "text/csv;charset=UTF-8";
    }

    @Override
    public String getFileExtension() {
        return ".csv";
    }
} 
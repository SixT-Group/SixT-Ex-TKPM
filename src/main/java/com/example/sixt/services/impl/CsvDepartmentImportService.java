package com.example.sixt.services.impl;

import com.example.sixt.models.DepartmentEntity;
import com.example.sixt.services.DataImportService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvDepartmentImportService implements DataImportService<DepartmentEntity> {

    @Override
    public List<DepartmentEntity> importFromFile(MultipartFile file) throws Exception {
        List<DepartmentEntity> departments = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                
                String[] data = line.split(",");
                if (data.length >= 2) {
                    DepartmentEntity department = new DepartmentEntity();
                    department.setName(data[1].trim());
                    departments.add(department);
                }
            }
        }
        
        return departments;
    }

    @Override
    public boolean supports(String contentType) {
        return contentType != null && 
               (contentType.equals("text/csv") || 
                contentType.equals("application/csv") ||
                contentType.equals("text/plain"));
    }
} 
package com.example.sixt.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DataImportService<T> {
    List<T> importFromFile(MultipartFile file) throws Exception;
    boolean supports(String contentType);
} 
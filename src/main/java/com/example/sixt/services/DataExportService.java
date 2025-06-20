package com.example.sixt.services;

import org.springframework.core.io.InputStreamResource;

import java.util.List;

public interface DataExportService<T> {
    InputStreamResource exportToStream(List<T> data, String filename);
    byte[] exportToBytes(List<T> data);
    String getContentType();
    String getFileExtension();
} 
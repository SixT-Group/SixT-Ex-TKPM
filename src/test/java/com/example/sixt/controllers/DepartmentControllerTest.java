package com.example.sixt.controllers;

import com.example.sixt.controllers.requests.DepartmentCreationRequest;
import com.example.sixt.exceptions.InvalidDataException;
import com.example.sixt.models.DepartmentEntity;
import com.example.sixt.services.DepartmentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    @Mock
    private DepartmentService departmentService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private DepartmentController departmentController;

    private DepartmentEntity testDepartment;
    private DepartmentCreationRequest creationRequest;

    @BeforeEach
    void setUp() {
        testDepartment = new DepartmentEntity();
        testDepartment.setId(1L);
        testDepartment.setName("IT Department");

        creationRequest = new DepartmentCreationRequest();
        creationRequest.setName("IT Department");
    }

    @Test
    void updateProgram_Success() throws InvalidDataException {
        when(departmentService.updateDepartment(anyLong(), anyString())).thenReturn(testDepartment);

        Map<String, Object> response = departmentController.updateProgram(1L, "IT Department");

        assertEquals("201", response.get("status"));
        assertEquals("Department updated successfully", response.get("message"));
        assertEquals(testDepartment, response.get("data"));
    }

    @Test
    void updateProgram_InvalidDataException() throws InvalidDataException {
        when(departmentService.updateDepartment(anyLong(), anyString()))
                .thenThrow(new InvalidDataException("Invalid department name"));

        Map<String, Object> response = departmentController.updateProgram(1L, "");

        assertEquals("409", response.get("status"));
        assertEquals("Invalid department name", response.get("message"));
        assertEquals(0, response.get("data"));
    }

    @Test
    void updateProgram_GeneralException() throws InvalidDataException {
        when(departmentService.updateDepartment(anyLong(), anyString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        Map<String, Object> response = departmentController.updateProgram(1L, "IT Department");

        assertEquals("500", response.get("status"));
        assertEquals("Unexpected error", response.get("message"));
        assertEquals(0, response.get("data"));
    }

    @Test
    void addProgram_Success() throws InvalidDataException {
        when(departmentService.addDepartment(any(DepartmentCreationRequest.class))).thenReturn(testDepartment);

        Map<String, Object> response = departmentController.addProgram(creationRequest);

        assertEquals("201", response.get("status"));
        assertEquals("Department added successfully", response.get("message"));
        assertEquals(testDepartment, response.get("data"));
    }

    @Test
    void addProgram_InvalidDataException() throws InvalidDataException {
        when(departmentService.addDepartment(any(DepartmentCreationRequest.class)))
                .thenThrow(new InvalidDataException("Department name already exists"));

        Map<String, Object> response = departmentController.addProgram(creationRequest);

        assertEquals("409", response.get("status"));
        assertEquals("Department name already exists", response.get("message"));
        assertEquals(0, response.get("data"));
    }

    @Test
    void addProgram_GeneralException() throws InvalidDataException {
        when(departmentService.addDepartment(any(DepartmentCreationRequest.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        Map<String, Object> response = departmentController.addProgram(creationRequest);

        assertEquals("500", response.get("status"));
        assertEquals("Database connection failed", response.get("message"));
        assertEquals(0, response.get("data"));
    }

    @Test
    void getDepartmentById_Success() throws InvalidDataException {
        when(departmentService.getDepartmentById(anyLong())).thenReturn(testDepartment);

        Map<String, Object> response = departmentController.getDepartmentById(1L);

        assertEquals("200", response.get("status"));
        assertEquals("Department fetched successfully", response.get("message"));
        assertEquals(testDepartment, response.get("data"));
    }

    @Test
    void getDepartmentById_InvalidDataException() throws InvalidDataException {
        when(departmentService.getDepartmentById(anyLong()))
                .thenThrow(new InvalidDataException("Department not found"));

        Map<String, Object> response = departmentController.getDepartmentById(1L);

        assertEquals("409", response.get("status"));
        assertEquals("Department not found", response.get("message"));
        assertEquals(0, response.get("data"));
    }

    @Test
    void getDepartmentById_GeneralException() throws InvalidDataException {
        when(departmentService.getDepartmentById(anyLong()))
                .thenThrow(new RuntimeException("Service unavailable"));

        Map<String, Object> response = departmentController.getDepartmentById(1L);

        assertEquals("500", response.get("status"));
        assertEquals("Service unavailable", response.get("message"));
        assertEquals(0, response.get("data"));
    }

    @Test
    void importCsv_Success() throws Exception {
        MultipartFile file = new MockMultipartFile(
                "file",
                "departments.csv",
                "text/csv",
                "id,name\n1,IT Department".getBytes(StandardCharsets.UTF_8)
        );

        doNothing().when(departmentService).importCsv(any(MultipartFile.class));

        Map<String, Object> response = departmentController.importCsv(file);

        assertEquals("200", response.get("status"));
        assertEquals("Import CSV successfully!", response.get("message"));
        assertEquals(1, response.get("data"));
    }

    @Test
    void importCsv_Failure() throws Exception {
        MultipartFile file = new MockMultipartFile(
                "file",
                "departments.csv",
                "text/csv",
                "id,name\n1,IT Department".getBytes(StandardCharsets.UTF_8)
        );

        doThrow(new IOException("File processing error")).when(departmentService).importCsv(any(MultipartFile.class));

        Map<String, Object> response = departmentController.importCsv(file);

        assertEquals("500", response.get("status"));
        assertEquals("File processing error", response.get("message"));
        assertEquals(0, response.get("data"));
    }

   

    @Test
    void exportCsv_Failure() throws IOException {
        when(httpServletResponse.getWriter()).thenThrow(new IOException("Output error"));

        Map<String, Object> response = departmentController.exportCsv(httpServletResponse);

        assertEquals("500", response.get("status"));
        assertEquals("Output error", response.get("message"));
        assertEquals(0, response.get("data"));
    }

    @Test
    void importJson_Success() throws IOException {
        String jsonContent = "[{\"id\":1,\"name\":\"IT Department\"}]";
        MultipartFile file = new MockMultipartFile(
                "file",
                "departments.json",
                "application/json",
                jsonContent.getBytes(StandardCharsets.UTF_8)
        );

        List<DepartmentEntity> departments = Arrays.asList(testDepartment);
        when(objectMapper.readValue(any(ByteArrayInputStream.class), any(TypeReference.class))).thenReturn(departments);
        doReturn(departments).when(departmentService).saveAll(anyList());

        Map<String, Object> response = departmentController.importJson(file);

        assertEquals("200", response.get("status"));
        assertEquals("Import JSON successfully!", response.get("message"));
        assertEquals(1, response.get("data"));
    }

    @Test
    void importJson_Failure() throws IOException {
        MultipartFile file = new MockMultipartFile(
                "file",
                "departments.json",
                "application/json",
                "invalid json".getBytes(StandardCharsets.UTF_8)
        );

        when(objectMapper.readValue(any(ByteArrayInputStream.class), any(TypeReference.class)))
                .thenThrow(new IOException("Invalid JSON format"));

        Map<String, Object> response = departmentController.importJson(file);

        assertEquals("500", response.get("status"));
        assertEquals("Invalid JSON format", response.get("message"));
        assertEquals(0, response.get("data"));
    }

    @Test
    void exportJson_Success() throws Exception {
        List<DepartmentEntity> departments = Arrays.asList(testDepartment);
        byte[] jsonBytes = "[{\"id\":1,\"name\":\"IT Department\"}]".getBytes(StandardCharsets.UTF_8);

        when(departmentService.getAllDepartments()).thenReturn(departments);
        when(objectMapper.writeValueAsBytes(any())).thenReturn(jsonBytes);

        ResponseEntity<byte[]> response = departmentController.exportJson();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(jsonBytes, response.getBody());
        assertEquals("attachment; filename=departments.json", 
                     response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("application/json", 
                     response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    void exportJson_Failure() throws Exception {
        when(departmentService.getAllDepartments()).thenThrow(new RuntimeException("Data access error"));

        ResponseEntity<byte[]> response = departmentController.exportJson();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}
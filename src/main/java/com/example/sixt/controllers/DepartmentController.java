package com.example.sixt.controllers;

import com.example.sixt.controllers.requests.DepartmentCreationRequest;
import com.example.sixt.controllers.requests.ProgramCreationRequest;
import com.example.sixt.exceptions.InvalidDataException;
import com.example.sixt.models.DepartmentEntity;
import com.example.sixt.models.ProgramEntity;
import com.example.sixt.services.DepartmentService;
import com.example.sixt.util.MessageUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/departments")
@Tag(name = "Department Controller", description = "API endpoints for managing departments with internationalization support")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final ObjectMapper objectMapper;
    private final MessageUtil messageUtil;
    private static final Logger log = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    public DepartmentController(DepartmentService departmentService, ObjectMapper objectMapper, MessageUtil messageUtil) {
        this.departmentService = departmentService;
        this.objectMapper = objectMapper;
        this.messageUtil = messageUtil;
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a department",
        description = "Updates a department with the given ID. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Department updated successfully",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Invalid data provided",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public Map<String, Object> updateProgram(
        @Parameter(description = "ID of the department to update")
        @PathVariable Long id, 
        @Parameter(description = "New department name")
        @RequestParam String department
    ) {
        try {
            log.info("Updating department for id: {}", id);
            DepartmentEntity updatedDepartment = departmentService.updateDepartment(id, department);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "201");
            response.put("message", messageUtil.getMessage("department.update.success"));
            response.put("data", updatedDepartment);
            return response;
        }
        catch (InvalidDataException e) {
            log.error("Error updating department: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "409");
            response.put("message", e.getMessage());
            response.put("data", 0);
            return response;
        }
        catch (Exception e) {
            log.error("Error updating department: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "500");
            response.put("message", messageUtil.getMessage("system.error.bad.request"));
            response.put("data", 0);
            return response;
        }
    }

    @PostMapping
    @Operation(
        summary = "Add a new department",
        description = "Creates a new department and returns the created department details. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Department created successfully",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Invalid data provided",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public Map<String, Object> addProgram(@RequestBody DepartmentCreationRequest departmentCreationRequest) {
        try {
            log.info("Adding new Department: {}", departmentCreationRequest.getName());
            DepartmentEntity newDepartment = departmentService.addDepartment(departmentCreationRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "201");
            response.put("message", messageUtil.getMessage("department.create.success"));
            response.put("data", newDepartment);
            return response;
        }
        catch (InvalidDataException e) {
            log.error("Error adding department: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "409");
            response.put("message", e.getMessage());
            response.put("data", 0);
            return response;
        }
        catch (Exception e) {
            log.error("Error adding department: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "500");
            response.put("message", messageUtil.getMessage("system.error.bad.request"));
            response.put("data", 0);
            return response;
        }
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get department by ID",
        description = "Retrieves a department by its ID. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Department fetched successfully",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Department not found",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public Map<String, Object> getDepartmentById(
        @Parameter(description = "ID of the department to retrieve")
        @PathVariable Long id
    ) {
        try {
            log.info("Fetching department by id: {}", id);
            DepartmentEntity departmentEntity = departmentService.getDepartmentById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "200");
            response.put("message", messageUtil.getMessage("department.search.success"));
            response.put("data", departmentEntity);
            return response;
        }
        catch (InvalidDataException e) {
            log.error("Error fetching department: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "409");
            response.put("message", e.getMessage());
            response.put("data", 0);
            return response;
        }
        catch (Exception e) {
            log.error("Error fetching department: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "500");
            response.put("message", messageUtil.getMessage("system.error.bad.request"));
            response.put("data", 0);
            return response;
        }
    }

    // Import CSV
    @PostMapping(value = "/import/csv", consumes = "multipart/form-data")
    @Operation(
        summary = "Import departments from CSV",
        description = "Imports department data from a CSV file. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "CSV imported successfully",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error importing CSV",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public Map<String, Object> importCsv(
        @Parameter(description = "CSV file to import")
        @RequestParam("file") MultipartFile file
    ) {
        try {
            departmentService.importCsv(file);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "200");
            response.put("message", messageUtil.getMessage("common.success"));
            response.put("data", 1);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "500");
            response.put("message", messageUtil.getMessage("system.error.bad.request"));
            response.put("data", 0);
            return response;
        }
    }

    // Export CSV
    @GetMapping(value = "/export/csv", produces = "text/csv;charset=ISO-8859-1")
    @Operation(
        summary = "Export departments to CSV",
        description = "Exports all departments to a CSV file. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "CSV exported successfully",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error exporting CSV",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public Map<String, Object> exportCsv(HttpServletResponse response) {
        try {
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=departments.csv");
            departmentService.exportCsv(response);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", "200");
            responseBody.put("message", messageUtil.getMessage("common.success"));
            responseBody.put("data", 1);

            return responseBody;
        }
        catch (IOException e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", "500");
            responseBody.put("message", messageUtil.getMessage("system.error.bad.request"));
            responseBody.put("data", 0);

            return responseBody;
        }
    }

    @PostMapping(value = "/import/json", consumes = "multipart/form-data")
    @Operation(
        summary = "Import departments from JSON",
        description = "Imports department data from a JSON file. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "JSON imported successfully",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error importing JSON",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public Map<String, Object> importJson(
        @Parameter(description = "JSON file to import")
        @RequestParam("file") MultipartFile file
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DepartmentEntity> departments = objectMapper.readValue(file.getInputStream(), new TypeReference<List<DepartmentEntity>>() {});

            departmentService.saveAll(departments);

            response.put("status", "200");
            response.put("message", messageUtil.getMessage("common.success"));
            response.put("data", departments.size());
        } catch (Exception e) {
            response.put("status", "500");
            response.put("message", messageUtil.getMessage("system.error.bad.request"));
            response.put("data", 0);
        }
        return response;
    }

    @GetMapping("/export/json")
    @Operation(
        summary = "Export departments to JSON",
        description = "Exports all departments to a JSON file."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "JSON exported successfully"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error exporting JSON"
        )
    })
    public ResponseEntity<byte[]> exportJson() {
        try {
            List<DepartmentEntity> departments = departmentService.getAllDepartments();
            ObjectMapper objectMapper = new ObjectMapper();

            byte[] jsonData = objectMapper.writeValueAsBytes(departments);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=departments.json");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

            return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

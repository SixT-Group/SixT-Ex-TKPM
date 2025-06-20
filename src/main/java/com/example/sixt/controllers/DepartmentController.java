package com.example.sixt.controllers;

import com.example.sixt.controllers.requests.DepartmentCreationRequest;
import com.example.sixt.controllers.responses.ApiResponse;
import com.example.sixt.models.DepartmentEntity;
import com.example.sixt.services.DepartmentService;
import com.example.sixt.services.impl.CsvDepartmentExportService;
import com.example.sixt.services.impl.JsonDepartmentExportService;
import com.example.sixt.services.impl.CsvDepartmentImportService;
import com.example.sixt.services.impl.JsonDepartmentImportService;
import com.example.sixt.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/departments")
@Tag(name = "Department Controller", description = "API endpoints for managing departments with internationalization support")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final CsvDepartmentExportService csvExportService;
    private final JsonDepartmentExportService jsonExportService;
    private final CsvDepartmentImportService csvImportService;
    private final JsonDepartmentImportService jsonImportService;
    private final MessageUtil messageUtil;
    private static final Logger log = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    public DepartmentController(DepartmentService departmentService,
                               CsvDepartmentExportService csvExportService,
                               JsonDepartmentExportService jsonExportService,
                               CsvDepartmentImportService csvImportService,
                               JsonDepartmentImportService jsonImportService,
                               MessageUtil messageUtil) {
        this.departmentService = departmentService;
        this.csvExportService = csvExportService;
        this.jsonExportService = jsonExportService;
        this.csvImportService = csvImportService;
        this.jsonImportService = jsonImportService;
        this.messageUtil = messageUtil;
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a department",
        description = "Updates a department with the given ID. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Department updated successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Invalid data provided",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<DepartmentEntity>> updateDepartment(
        @Parameter(description = "ID of the department to update")
        @PathVariable Long id, 
        @Parameter(description = "New department name")
        @RequestParam String departmentName
    ) {
        log.info("Updating department for id: {}", id);
        DepartmentEntity updatedDepartment = departmentService.updateDepartment(id, departmentName);
        return ApiResponse.ok(updatedDepartment, messageUtil.getMessage("department.update.success"));
    }

    @PostMapping
    @Operation(
        summary = "Add a new department",
        description = "Creates a new department and returns the created department details. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Department created successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Invalid data provided",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<DepartmentEntity>> addDepartment(
        @RequestBody DepartmentCreationRequest departmentCreationRequest
    ) {
        log.info("Adding new Department: {}", departmentCreationRequest.getName());
        DepartmentEntity newDepartment = departmentService.addDepartment(departmentCreationRequest);
        return ApiResponse.created(newDepartment, messageUtil.getMessage("department.create.success"));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get department by ID",
        description = "Retrieves a department by its ID. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Department fetched successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Department not found",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<DepartmentEntity>> getDepartmentById(
        @Parameter(description = "ID of the department to retrieve")
        @PathVariable Long id
    ) {
        log.info("Fetching department by id: {}", id);
        DepartmentEntity departmentEntity = departmentService.getDepartmentById(id);
        return ApiResponse.ok(departmentEntity, messageUtil.getMessage("department.search.success"));
    }

    // Import CSV
    @PostMapping(value = "/import/csv", consumes = "multipart/form-data")
    @Operation(
        summary = "Import departments from CSV",
        description = "Imports department data from a CSV file. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "CSV imported successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<Integer>> importCsv(
        @Parameter(description = "CSV file to import")
        @RequestParam("file") MultipartFile file
    ) throws Exception {
        List<DepartmentEntity> departments = csvImportService.importFromFile(file);
        departmentService.saveAll(departments);
        return ApiResponse.ok(departments.size(), messageUtil.getMessage("common.success"));
    }

    // Export CSV
    @GetMapping(value = "/export/csv")
    @Operation(
        summary = "Export departments to CSV",
        description = "Exports all departments to a CSV file. Messages are displayed in the language specified by Accept-Language header."
    )
    public ResponseEntity<InputStreamResource> exportCsv() {
        List<DepartmentEntity> departments = departmentService.getAllDepartments();
        InputStreamResource resource = csvExportService.exportToStream(departments, "departments.csv");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=departments.csv");
        headers.setContentType(org.springframework.http.MediaType.parseMediaType(csvExportService.getContentType()));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/import/json", consumes = "multipart/form-data")
    @Operation(
        summary = "Import departments from JSON",
        description = "Imports department data from a JSON file. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "JSON imported successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<Integer>> importJson(
        @Parameter(description = "JSON file to import")
        @RequestParam("file") MultipartFile file
    ) throws Exception {
        List<DepartmentEntity> departments = jsonImportService.importFromFile(file);
        departmentService.saveAll(departments);
        return ApiResponse.ok(departments.size(), messageUtil.getMessage("common.success"));
    }

    @GetMapping("/export/json")
    @Operation(
        summary = "Export departments to JSON",
        description = "Exports all departments to a JSON file."
    )
    public ResponseEntity<InputStreamResource> exportJson() {
        List<DepartmentEntity> departments = departmentService.getAllDepartments();
        InputStreamResource resource = jsonExportService.exportToStream(departments, "departments.json");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=departments.json");
        headers.setContentType(org.springframework.http.MediaType.parseMediaType(jsonExportService.getContentType()));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}

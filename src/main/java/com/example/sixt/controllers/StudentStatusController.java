package com.example.sixt.controllers;

import com.example.sixt.controllers.requests.StatusCreationRequest;
import com.example.sixt.exceptions.InvalidDataException;
import com.example.sixt.models.StudentStatusEntity;
import com.example.sixt.services.StudentStatusService;
import com.example.sixt.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/student-statuses")
@Tag(name = "Student Status Controller", description = "API endpoints for managing student statuses with internationalization support")
public class StudentStatusController {
    private final StudentStatusService studentStatusService;
    private final MessageUtil messageUtil;
    private static final Logger log = LoggerFactory.getLogger(StudentStatusController.class);

    @Autowired
    public StudentStatusController(StudentStatusService studentStatusService, MessageUtil messageUtil) {
        this.studentStatusService = studentStatusService;
        this.messageUtil = messageUtil;
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a student status",
        description = "Updates a student status with the given ID. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Student status updated successfully",
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
    public Map<String, Object> updateStatus(
        @Parameter(description = "ID of the student status to update")
        @PathVariable Long id, 
        @Parameter(description = "New status name")
        @RequestParam String status
    ) {
        try {
            log.info("Updating student status for id: {}", id);
            StudentStatusEntity updatedStatus = studentStatusService.updateStatus(id, status);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "201");
            response.put("message", messageUtil.getMessage("status.update.success"));
            response.put("data", updatedStatus);
            return response;
        }
        catch (InvalidDataException e) {
            log.error("Error updating student status: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "409");
            response.put("message", e.getMessage());
            response.put("data", 0);
            return response;
        }
        catch (Exception e) {
            log.error("Error updating student status: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "500");
            response.put("message", messageUtil.getMessage("system.error.bad.request"));
            response.put("data", 0);
            return response;
        }
    }

    @PostMapping
    @Operation(
        summary = "Add a new student status",
        description = "Creates a new student status and returns the created status details. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Student status created successfully",
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
    public Map<String, Object> addStatus(
        @Parameter(description = "Status details to create")
        @RequestBody StatusCreationRequest statusRequest
    ) {
        try {
            log.info("Adding new student status: {}", statusRequest.getName());
            StudentStatusEntity newStatus = studentStatusService.addStatus(statusRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "201");
            response.put("message", messageUtil.getMessage("status.create.success"));
            response.put("data", newStatus);
            return response;
        }
        catch (InvalidDataException e) {
            log.error("Error adding student status: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "409");
            response.put("message", e.getMessage());
            response.put("data", 0);
            return response;
        }
        catch (Exception e) {
            log.error("Error adding student status: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "500");
            response.put("message", messageUtil.getMessage("system.error.bad.request"));
            response.put("data", 0);
            return response;
        }
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get student status by ID",
        description = "Retrieves a student status by its ID. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Student status fetched successfully",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Student status not found",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public Map<String, Object> getStatusById(
        @Parameter(description = "ID of the student status to retrieve")
        @PathVariable Long id
    ) {
        try {
            log.info("Fetching student status by id: {}", id);
            StudentStatusEntity status = studentStatusService.getStatusById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "200");
            response.put("message", messageUtil.getMessage("status.search.success"));
            response.put("data", status);
            return response;
        }
        catch (InvalidDataException e) {
            log.error("Error fetching student status: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "409");
            response.put("message", e.getMessage());
            response.put("data", 0);
            return response;
        }
        catch (Exception e) {
            log.error("Error fetching student status: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "500");
            response.put("message", messageUtil.getMessage("system.error.bad.request"));
            response.put("data", 0);
            return response;
        }
    }
}

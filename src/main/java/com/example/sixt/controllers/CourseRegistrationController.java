package com.example.sixt.controllers;

import com.example.sixt.controllers.requests.CourseRegistrationRequest;
import com.example.sixt.models.CourseRegistrationEntity;
import com.example.sixt.services.CourseRegistrationService;
import com.example.sixt.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/course-registrations")
@Tag(name = "Course Registration Controller", description = "API endpoints for managing course registrations with internationalization support")
public class CourseRegistrationController {
    private final CourseRegistrationService registrationService;
    private final MessageUtil messageUtil;

    @Autowired
    public CourseRegistrationController(CourseRegistrationService registrationService, MessageUtil messageUtil) {
        this.registrationService = registrationService;
        this.messageUtil = messageUtil;
    }

    @PostMapping("/register")
    @Operation(
        summary = "Register student for a course",
        description = "Registers a student for a course in a specific semester and academic year. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Course registration successful",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid data provided",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, Object>> registerCourse(
            @Parameter(description = "ID of the student to register", required = true)
            @RequestParam Long studentId,
            @Parameter(description = "Course registration details", required = true)
            @Valid @RequestBody CourseRegistrationRequest request) {
        CourseRegistrationEntity registration = registrationService.registerCourse(
            studentId,
            request.getCourseId(),
            request.getSemester(),
            request.getAcademicYear()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("status", "201");
        response.put("message", messageUtil.getMessage("registration.create.success"));
        response.put("data", registration);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    @Operation(
        summary = "Get student course registrations",
        description = "Retrieves course registrations for a student in a specific semester and academic year. Messages are displayed in the language specified by Accept-Language header."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Registrations retrieved successfully",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid parameters provided",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<Map<String, Object>> getStudentRegistrations(
            @Parameter(description = "ID of the student", required = true)
            @PathVariable Long studentId,
            @Parameter(description = "Semester (e.g., 'Fall', 'Spring')", required = true)
            @RequestParam String semester,
            @Parameter(description = "Academic year (e.g., 2023)", required = true)
            @RequestParam Integer academicYear) {
        List<CourseRegistrationEntity> registrations = registrationService
            .getStudentRegistrations(studentId, semester, academicYear);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "200");
        response.put("message", messageUtil.getMessage("registration.search.success"));
        response.put("data", registrations);
        
        return ResponseEntity.ok(response);
    }
}
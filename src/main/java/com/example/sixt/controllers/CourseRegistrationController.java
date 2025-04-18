package com.example.sixt.controllers;

import com.example.sixt.controllers.requests.CourseRegistrationRequest;
import com.example.sixt.models.CourseRegistrationEntity;
import com.example.sixt.services.CourseRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/course-registrations")
public class CourseRegistrationController {
    private final CourseRegistrationService registrationService;

    public CourseRegistrationController(CourseRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerCourse(
            @RequestParam Long studentId,
            @Valid @RequestBody CourseRegistrationRequest request) {
        CourseRegistrationEntity registration = registrationService.registerCourse(
            studentId,
            request.getCourseId(),
            request.getSemester(),
            request.getAcademicYear()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("status", "201");
        response.put("message", "Course registration successful");
        response.put("data", registration);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentRegistrations(
            @PathVariable Long studentId,
            @RequestParam String semester,
            @RequestParam Integer academicYear) {
        List<CourseRegistrationEntity> registrations = registrationService
            .getStudentRegistrations(studentId, semester, academicYear);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "200");
        response.put("message", "Registrations retrieved successfully");
        response.put("data", registrations);
        
        return ResponseEntity.ok(response);
    }
}
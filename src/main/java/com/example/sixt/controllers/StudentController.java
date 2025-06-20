package com.example.sixt.controllers;

import com.example.sixt.controllers.requests.StudentCreationRequest;
import com.example.sixt.controllers.requests.StudentUpdateRequest;
import com.example.sixt.controllers.responses.ApiResponse;
import com.example.sixt.controllers.responses.StudentResponse;
import com.example.sixt.services.StudentService;
import com.example.sixt.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@Tag(name = "Student Controller", description = "API endpoints for managing students with internationalization support")
@Validated
public class StudentController {

  private final StudentService studentService;
  private final MessageUtil messageUtil;
  private static final Logger log = LoggerFactory.getLogger(StudentController.class);

  public StudentController(StudentService studentService, MessageUtil messageUtil) {
    this.studentService = studentService;
    this.messageUtil = messageUtil;
  }

  @PostMapping
  @Operation(
      summary = "Add a new student",
      description = "Creates a new student and returns the created student details. Messages are displayed in the language specified by Accept-Language header."
  )
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "201",
          description = "Student created successfully",
          content = @Content(schema = @Schema(implementation = ApiResponse.class))
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "409",
          description = "Invalid data provided",
          content = @Content(schema = @Schema(implementation = ApiResponse.class))
      )
  })
  public ResponseEntity<ApiResponse<StudentResponse>> addStudent(
      @RequestBody @Valid StudentCreationRequest student
  ) {
    StudentResponse studentResponse = studentService.addStudent(student);
    log.info("Student added successfully");
    return ApiResponse.created(studentResponse, messageUtil.getMessage("student.create.success"));
  }

  @DeleteMapping("/{studentId}")
  @Operation(
      summary = "Delete a student",
      description = "Deletes a student with the given ID. Messages are displayed in the language specified by Accept-Language header."
  )
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "Student deleted successfully",
          content = @Content(schema = @Schema(implementation = ApiResponse.class))
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "409",
          description = "Invalid student ID",
          content = @Content(schema = @Schema(implementation = ApiResponse.class))
      )
  })
  public ResponseEntity<ApiResponse<String>> deleteStudent(
      @Parameter(description = "ID of the student to delete")
      @PathVariable String studentId
  ) {
    studentService.deleteStudent(studentId);
    log.info("Student deleted successfully");
    return ApiResponse.ok("Deleted", messageUtil.getMessage("student.delete.success"));
  }

  @PutMapping("/{studentId}")
  @Operation(
      summary = "Update a student",
      description = "Updates a student with the given ID. Messages are displayed in the language specified by Accept-Language header."
  )
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "Student updated successfully",
          content = @Content(schema = @Schema(implementation = ApiResponse.class))
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "409",
          description = "Invalid data provided",
          content = @Content(schema = @Schema(implementation = ApiResponse.class))
      )
  })
  public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
      @Parameter(description = "ID of the student to update")
      @PathVariable String studentId,
      @RequestBody @Valid StudentUpdateRequest student
  ) {
    StudentResponse studentResponse = studentService.updateStudent(studentId, student);
    log.info("Student updated successfully");
    return ApiResponse.ok(studentResponse, messageUtil.getMessage("student.update.success"));
  }

  @GetMapping("/search")
  @Operation(
      summary = "Search students",
      description = "Searches for students by keyword and optional department filter. Messages are displayed in the language specified by Accept-Language header."
  )
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "Search completed successfully",
          content = @Content(schema = @Schema(implementation = ApiResponse.class))
      )
  })
  public ResponseEntity<ApiResponse<List<StudentResponse>>> searchStudents(
      @Parameter(description = "Keyword to search for in student records")
      @RequestParam(required = false) String keyword,
      @Parameter(description = "Department to filter by")
      @RequestParam(required = false) String department
  ) {
    List<StudentResponse> studentResponses;
    
    if (department != null && !department.isEmpty()) {
      studentResponses = studentService.searchStudentsByDepartmentAndName(keyword, department);
    } else if (keyword != null && !keyword.isEmpty()) {
      studentResponses = studentService.searchStudents(keyword);
    } else {
      // If no parameters provided, could return all students or return empty list
      studentResponses = List.of();
    }

    log.info("Found {} students", studentResponses.size());
    return ApiResponse.ok(studentResponses, messageUtil.getMessage("student.search.success"));
  }
}

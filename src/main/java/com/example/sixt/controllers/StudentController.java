package com.example.sixt.controllers;

import com.example.sixt.controllers.requests.StudentCreationRequest;
import com.example.sixt.controllers.requests.StudentUpdateRequest;
import com.example.sixt.controllers.responses.StudentResponse;
import com.example.sixt.exceptions.InvalidDataException;
import com.example.sixt.services.StudentService;
import com.example.sixt.util.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

  @PostMapping("/add")
  @Operation(
      summary = "Add a new student",
      description = "Creates a new student and returns the created student details. Messages are displayed in the language specified by Accept-Language header."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Student created successfully",
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
  public Map<String, Object> addStudent(@RequestBody @Valid StudentCreationRequest student) {
    try {
      StudentResponse studentEntity = studentService.addStudent(student);

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.CREATED.value());
      response.put("message", messageUtil.getMessage("student.create.success"));
      response.put("data", studentEntity);

      log.info("Student added successfully");

      return response;
    } catch (InvalidDataException e) {
      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.CONFLICT.value());
      response.put("message", e.getMessage());
      response.put("data", 0);

      log.error(e.getMessage());

      return response;
    } catch (Exception e) {
      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.put("message", messageUtil.getMessage("system.error.bad.request"));
      response.put("data", 0);

      log.error(e.getMessage());

      return response;
    }
  }

  @DeleteMapping("/delete/{studentId}")
  @Operation(
      summary = "Delete a student",
      description = "Deletes a student with the given ID. Messages are displayed in the language specified by Accept-Language header."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Student deleted successfully",
          content = @Content(schema = @Schema(implementation = Map.class))
      ),
      @ApiResponse(
          responseCode = "409",
          description = "Invalid student ID",
          content = @Content(schema = @Schema(implementation = Map.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal server error",
          content = @Content(schema = @Schema(implementation = Map.class))
      )
  })
  public Map<String, Object> deleteStudent(
      @Parameter(description = "ID of the student to delete")
      @PathVariable String studentId
  ) {
    try {
      studentService.deleteStudent(studentId);

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.OK.value());
      response.put("message", messageUtil.getMessage("student.delete.success"));
      response.put("data", 1);

      log.info("Student deleted successfully");

      return response;
    } catch (InvalidDataException e) {
      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.CONFLICT.value());
      response.put("message", e.getMessage());
      response.put("data", 0);

      log.error(e.getMessage());

      return response;
    } catch (Exception e) {
      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.put("message", messageUtil.getMessage("system.error.bad.request"));
      response.put("data", 0);

      log.error(e.getMessage());

      return response;
    }
  }

  @PatchMapping("/update/{studentId}")
  @Operation(
      summary = "Update a student",
      description = "Updates a student with the given ID. Messages are displayed in the language specified by Accept-Language header."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Student updated successfully",
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
  public Map<String, Object> updateStudent(
      @Parameter(description = "ID of the student to update")
      @PathVariable String studentId,
      @RequestBody @Valid StudentUpdateRequest student
  ) {
    try {
      StudentResponse studentResponse = studentService.updateStudent(studentId, student);

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.CREATED.value());
      response.put("message", messageUtil.getMessage("student.update.success"));
      response.put("data", studentResponse);

      log.info("Student updated successfully");

      return response;
    } catch (InvalidDataException e) {
      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.CONFLICT.value());
      response.put("message", e.getMessage());
      response.put("data", 0);

      log.error(e.getMessage());

      return response;
    } catch (Exception e) {
      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.put("message", messageUtil.getMessage("system.error.bad.request"));
      response.put("data", 0);

      log.error(e.getMessage());

      return response;
    }
  }

  @GetMapping("/search/{keyword}")
  @Operation(
      summary = "Search students by keyword",
      description = "Searches for students matching the given keyword. Messages are displayed in the language specified by Accept-Language header."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Search completed successfully",
          content = @Content(schema = @Schema(implementation = Map.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal server error",
          content = @Content(schema = @Schema(implementation = Map.class))
      )
  })
  public Map<String, Object> searchStudents(
      @Parameter(description = "Keyword to search for in student records")
      @PathVariable String keyword
  ) {
    try {
      List<StudentResponse> StudentResponses = studentService.searchStudents(keyword);

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.OK.value());
      response.put("message", messageUtil.getMessage("student.search.success"));
      response.put("data", StudentResponses);

      log.info("Found " + StudentResponses.size() + " students");

      return response;
    } catch (Exception e) {
      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.put("message", messageUtil.getMessage("system.error.bad.request"));
      response.put("data", 0);

      log.error(e.getMessage());

      return response;
    }
  }

  @GetMapping("/search-by-department-and-name")
  @Operation(
      summary = "Search students by department and name",
      description = "Searches for students in a specific department, optionally filtered by name. Messages are displayed in the language specified by Accept-Language header."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Search completed successfully",
          content = @Content(schema = @Schema(implementation = Map.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal server error",
          content = @Content(schema = @Schema(implementation = Map.class))
      )
  })
  public Map<String, Object> searchStudents(
      @Parameter(description = "Optional name to filter by")
      @RequestParam(required = false) String keyword,
      @Parameter(description = "Department to filter by", required = true)
      @RequestParam(required = true) String department
  ) {
    try {
      List<StudentResponse> StudentResponses = studentService.searchStudentsByDepartmentAndName(
          keyword, department);

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.OK.value());
      response.put("message", messageUtil.getMessage("student.search.success"));
      response.put("data", StudentResponses);

      log.info("Found " + StudentResponses.size() + " students");

      return response;
    } catch (Exception e) {
      Map<String, Object> response = new LinkedHashMap<>();
      response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.put("message", messageUtil.getMessage("system.error.bad.request"));
      response.put("data", 0);

      log.error(e.getMessage());

      return response;
    }
  }
}

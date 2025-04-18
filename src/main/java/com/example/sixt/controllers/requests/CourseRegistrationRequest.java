package com.example.sixt.controllers.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseRegistrationRequest {
    @NotNull
    private Long courseId;
    
    @NotNull
    private String semester;
    
    @NotNull
    private Integer academicYear;
}
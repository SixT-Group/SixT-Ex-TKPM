package com.example.sixt.controllers.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GradeUpdateRequest {
    @Min(0) @Max(10)
    private Float midtermGrade;
    
    @Min(0) @Max(10)
    private Float finalGrade;
    
    @Min(0) @Max(10)
    private Float otherGrade;
}
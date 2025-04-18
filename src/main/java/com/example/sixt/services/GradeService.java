package com.example.sixt.services;

import com.example.sixt.models.GradeEntity;
import java.util.List;

public interface GradeService {
    GradeEntity updateGrade(Long registrationId, Float midterm, Float finalGrade, Float otherGrade);
    List<GradeEntity> getStudentGrades(Long studentId);
    byte[] generateTranscript(Long studentId);
}
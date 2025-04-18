package com.example.sixt.services;

import com.example.sixt.models.CourseRegistrationEntity;
import java.util.List;

public interface CourseRegistrationService {
    CourseRegistrationEntity registerCourse(Long studentId, Long courseId, String semester, Integer academicYear);
    void cancelRegistration(Long registrationId);
    List<CourseRegistrationEntity> getStudentRegistrations(Long studentId, String semester, Integer academicYear);
}
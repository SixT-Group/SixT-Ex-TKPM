package com.example.sixt.repositories;

import com.example.sixt.enums.CourseProcessStatus;
import com.example.sixt.models.CourseRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRegistrationRepository extends JpaRepository<CourseRegistrationEntity, Long> {
    List<CourseRegistrationEntity> findByStudentIdAndSemesterAndAcademicYear(
        Long studentId, String semester, Integer academicYear);
    boolean existsById(Long id);
    long countByCourseIdAndStatus(Long courseId, CourseProcessStatus status);
}
package com.example.sixt.repositories;

import com.example.sixt.models.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    CourseEntity findByCourseCode(String courseCode);
}
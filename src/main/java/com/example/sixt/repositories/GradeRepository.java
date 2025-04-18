package com.example.sixt.repositories;

import com.example.sixt.models.GradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<GradeEntity, Long> {
    List<GradeEntity> findByRegistrationStudentId(Long studentId);
}
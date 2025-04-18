package com.example.sixt.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "grades")
public class GradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "registration_id", nullable = false)
    private CourseRegistrationEntity registration;

    private Float midtermGrade;
    private Float finalGrade;
    private Float otherGrade;
    
    @Column(nullable = false)
    private Float totalGrade;

    @Column(nullable = false)
    private LocalDateTime lastModified;
}
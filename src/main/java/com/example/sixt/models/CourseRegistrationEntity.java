package com.example.sixt.models;

import com.example.sixt.enums.CourseProcessStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;

@Entity
@Table(name = "course_registrations")
@Getter
@Setter
public class CourseRegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @Column(name = "class_id", nullable = false)
    private String classId;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private Integer academicYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CourseProcessStatus status = CourseProcessStatus.ONGOING;

    private Double grade;

    @Column(name = "terminated_before")
    private Date terminatedBefore;

    @Column(name = "terminated_at")
    private Date terminatedAt;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date updatedAt;
}
package com.example.sixt.models;

import com.example.sixt.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "courses")
@Getter
@Setter
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false, unique = true)
    private String courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    private Long credits;

    @Column(name = "department_id")
    private Long departmentId;

    private String description;

    @Column(name = "prerequisite_course")
    private String prerequisiteCourse; // reference to courseId

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CourseStatus status = CourseStatus.ACTIVATED; // when you want to delete a course, set this attribute to 'deactivated'

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date updatedAt;
}

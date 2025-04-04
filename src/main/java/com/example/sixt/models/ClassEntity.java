package com.example.sixt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "classes")
@Getter
@Setter
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_id", nullable = false, unique = true)
    private String classId;

    @Column(name = "course_id", nullable = false, unique = true)
    private String courseId;

    private String year;
    private String semester;
    private String teacher;
    @Column(name = "maximum_quantity")
    private Long maximumQuantity;
    private String schedule;
    private String room;
}

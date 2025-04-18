package com.example.sixt.services.impl;

import com.example.sixt.enums.RegistrationStatus;
import com.example.sixt.exceptions.InvalidDataException;
import com.example.sixt.models.CourseEntity;
import com.example.sixt.models.CourseRegistrationEntity;
import com.example.sixt.models.StudentEntity;
import com.example.sixt.repositories.CourseRegistrationRepository;
import com.example.sixt.repositories.CourseRepository;
import com.example.sixt.repositories.StudentRepository;
import com.example.sixt.services.CourseRegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseRegistrationServiceImpl implements CourseRegistrationService {
    private final CourseRegistrationRepository registrationRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public CourseRegistrationServiceImpl(CourseRegistrationRepository registrationRepository,
                                       CourseRepository courseRepository,
                                       StudentRepository studentRepository) {
        this.registrationRepository = registrationRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public CourseRegistrationEntity registerCourse(Long studentId, Long courseId, String semester, Integer academicYear) {
        StudentEntity student = studentRepository.findById(studentId)
            .orElseThrow(() -> new InvalidDataException("Student not found"));
        
        CourseEntity course = courseRepository.findById(courseId)
            .orElseThrow(() -> new InvalidDataException("Course not found"));

        // Check if student already registered for this course
        boolean alreadyRegistered = registrationRepository
            .findByStudentIdAndSemesterAndAcademicYear(studentId, semester, academicYear)
            .stream()
            .anyMatch(reg -> reg.getCourse().getId().equals(courseId));

        if (alreadyRegistered) {
            throw new InvalidDataException("Student already registered for this course");
        }

        // Check course capacity
        long currentRegistrations = registrationRepository.countByCourseIdAndStatus(
            courseId, RegistrationStatus.APPROVED);
        if (currentRegistrations >= course.getMaxStudents()) {
            throw new InvalidDataException("Course is full");
        }

        CourseRegistrationEntity registration = new CourseRegistrationEntity();
        registration.setStudent(student);
        registration.setCourse(course);
        registration.setSemester(semester);
        registration.setAcademicYear(academicYear);
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setStatus(RegistrationStatus.PENDING);

        return registrationRepository.save(registration);
    }

    @Override
    @Transactional
    public void cancelRegistration(Long registrationId) {
        CourseRegistrationEntity registration = registrationRepository.findById(registrationId)
            .orElseThrow(() -> new InvalidDataException("Registration not found"));

        // Only allow cancellation if status is PENDING or APPROVED
        if (registration.getStatus() != RegistrationStatus.PENDING 
            && registration.getStatus() != RegistrationStatus.APPROVED) {
            throw new InvalidDataException("Cannot cancel registration with status: " + registration.getStatus());
        }

        registration.setStatus(RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);
    }

    @Override
    public List<CourseRegistrationEntity> getStudentRegistrations(Long studentId, String semester, Integer academicYear) {
        // Verify student exists
        if (!studentRepository.existsById(studentId)) {
            throw new InvalidDataException("Student not found");
        }

        return registrationRepository.findByStudentIdAndSemesterAndAcademicYear(
            studentId, semester, academicYear);
    }

}
package com.example.sixt.services;

import com.example.sixt.enums.StudentStatus;
import com.example.sixt.models.StudentStatusEntity;

public interface StudentStatusTransitionManager {
    boolean canTransition(StudentStatusEntity currentStatus, StudentStatusEntity newStatus);
    void validateTransition(StudentStatusEntity currentStatus, StudentStatusEntity newStatus);
    StudentStatusEntity applyTransition(StudentStatusEntity currentStatus, StudentStatusEntity newStatus);
} 
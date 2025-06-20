package com.example.sixt.services.impl;

import com.example.sixt.enums.StudentStatus;
import com.example.sixt.exceptions.InvalidDataException;
import com.example.sixt.models.StudentStatusEntity;
import com.example.sixt.services.StudentStatusTransitionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class StudentStatusTransitionManagerImpl implements StudentStatusTransitionManager {
    
    private static final Logger log = LoggerFactory.getLogger(StudentStatusTransitionManagerImpl.class);
    
    private final Map<StudentStatus, Set<StudentStatus>> statusTransitionRules;

    @Autowired
    public StudentStatusTransitionManagerImpl(Map<StudentStatus, Set<StudentStatus>> statusTransitionRules) {
        this.statusTransitionRules = statusTransitionRules;
    }

    @Override
    public boolean canTransition(StudentStatusEntity currentStatus, StudentStatusEntity newStatus) {
        if (currentStatus == null || newStatus == null) {
            return false;
        }
        
        if (currentStatus.getId().equals(newStatus.getId())) {
            return true; // Same status, no transition needed
        }
        
        try {
            StudentStatus currentStatusEnum = StudentStatus.valueOf(currentStatus.getName());
            StudentStatus newStatusEnum = StudentStatus.valueOf(newStatus.getName());
            
            Set<StudentStatus> allowedTransitions = statusTransitionRules.get(currentStatusEnum);
            return allowedTransitions != null && allowedTransitions.contains(newStatusEnum);
        } catch (IllegalArgumentException e) {
            log.error("Invalid status names: current={}, new={}", currentStatus.getName(), newStatus.getName());
            return false;
        }
    }

    @Override
    public void validateTransition(StudentStatusEntity currentStatus, StudentStatusEntity newStatus) {
        if (!canTransition(currentStatus, newStatus)) {
            throw new InvalidDataException(
                String.format("Cannot transition from status '%s' to '%s'", 
                    currentStatus.getName(), newStatus.getName())
            );
        }
    }

    @Override
    public StudentStatusEntity applyTransition(StudentStatusEntity currentStatus, StudentStatusEntity newStatus) {
        validateTransition(currentStatus, newStatus);
        
        log.info("Successfully transitioned status from '{}' to '{}'", 
                currentStatus.getName(), newStatus.getName());
        
        return newStatus;
    }
} 
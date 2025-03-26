package com.example.sixt.configs;

import com.example.sixt.enums.StudentStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class StatusTransitionConfig {
    @Bean
    public Map<StudentStatus, Set<StudentStatus>> statusTransitionRules() {
        Map<StudentStatus, Set<StudentStatus>> rules = new HashMap<>();
        
        rules.put(StudentStatus.STUDYING, new HashSet<>(Arrays.asList(
            StudentStatus.TEMPORARILY_SUSPENDED,
            StudentStatus.GRADUATED,
            StudentStatus.DROPPED_OUT
        )));
        rules.put(StudentStatus.TEMPORARILY_SUSPENDED, new HashSet<>(Arrays.asList(
            StudentStatus.STUDYING,
            StudentStatus.DROPPED_OUT
        )));
        rules.put(StudentStatus.DROPPED_OUT, new HashSet<>());
        rules.put(StudentStatus.GRADUATED, new HashSet<>());
        
        return rules;
    }
}
package com.example.sixt.enums;

public enum RegistrationStatus {
    PENDING,        // Initial state when student registers
    APPROVED,       // Registration approved by admin/system
    REJECTED,       // Registration rejected due to various reasons
    CANCELLED,      // Student cancelled the registration
    COMPLETED,      // Course completed with grades
    DROPPED         // Student dropped the course during allowed period
}
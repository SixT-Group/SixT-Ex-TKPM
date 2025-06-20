package com.example.sixt.services;

import com.example.sixt.controllers.requests.IdentityDocumentRequest;
import com.example.sixt.models.IdentityDocumentEntity;

public interface IdentityDocumentService {
    IdentityDocumentEntity saveStudentIdentityDocument(String studentId, IdentityDocumentRequest identityDocument);
    IdentityDocumentEntity updateStudentIdentityDocument(String studentId, IdentityDocumentRequest identityDocument);
    IdentityDocumentEntity getStudentIdentityDocument(String studentId);
    void deleteStudentIdentityDocument(String studentId);
} 
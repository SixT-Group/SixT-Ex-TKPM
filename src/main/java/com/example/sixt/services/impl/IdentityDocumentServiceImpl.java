package com.example.sixt.services.impl;

import com.example.sixt.controllers.requests.IdentityDocumentRequest;
import com.example.sixt.models.IdentityDocumentEntity;
import com.example.sixt.repositories.IdentityDocumentRepository;
import com.example.sixt.services.IdentityDocumentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdentityDocumentServiceImpl implements IdentityDocumentService {
    
    private final IdentityDocumentRepository identityDocumentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public IdentityDocumentServiceImpl(IdentityDocumentRepository identityDocumentRepository, ModelMapper modelMapper) {
        this.identityDocumentRepository = identityDocumentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public IdentityDocumentEntity saveStudentIdentityDocument(String studentId, IdentityDocumentRequest identityDocument) {
        if (identityDocument == null) {
            return null;
        }
        
        IdentityDocumentEntity identityDocumentEntity = modelMapper.map(identityDocument, IdentityDocumentEntity.class);
        identityDocumentEntity.setStudentId(studentId);
        return identityDocumentRepository.save(identityDocumentEntity);
    }

    @Override
    @Transactional
    public IdentityDocumentEntity updateStudentIdentityDocument(String studentId, IdentityDocumentRequest identityDocument) {
        // Delete existing identity document first
        deleteStudentIdentityDocument(studentId);
        
        // Save new identity document
        return saveStudentIdentityDocument(studentId, identityDocument);
    }

    @Override
    public IdentityDocumentEntity getStudentIdentityDocument(String studentId) {
        return identityDocumentRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional
    public void deleteStudentIdentityDocument(String studentId) {
        identityDocumentRepository.deleteByStudentId(studentId);
    }
} 
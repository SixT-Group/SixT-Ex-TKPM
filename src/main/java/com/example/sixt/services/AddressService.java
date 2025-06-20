package com.example.sixt.services;

import com.example.sixt.controllers.requests.AddressRequest;
import com.example.sixt.models.AddressEntity;

import java.util.List;

public interface AddressService {
    List<AddressEntity> saveStudentAddresses(String studentId, List<AddressRequest> addresses);
    List<AddressEntity> updateStudentAddresses(String studentId, List<AddressRequest> addresses);
    List<AddressEntity> getStudentAddresses(String studentId);
    void deleteStudentAddresses(String studentId);
} 
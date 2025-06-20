package com.example.sixt.services.impl;

import com.example.sixt.controllers.requests.AddressRequest;
import com.example.sixt.models.AddressEntity;
import com.example.sixt.repositories.AddressRepository;
import com.example.sixt.services.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public List<AddressEntity> saveStudentAddresses(String studentId, List<AddressRequest> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return List.of();
        }
        
        List<AddressEntity> addressEntities = addresses.stream()
                .map(address -> modelMapper.map(address, AddressEntity.class))
                .peek(address -> address.setStudentId(studentId))
                .collect(Collectors.toList());
                
        return addressRepository.saveAll(addressEntities);
    }

    @Override
    @Transactional
    public List<AddressEntity> updateStudentAddresses(String studentId, List<AddressRequest> addresses) {
        // Delete existing addresses first
        deleteStudentAddresses(studentId);
        
        // Save new addresses
        return saveStudentAddresses(studentId, addresses);
    }

    @Override
    public List<AddressEntity> getStudentAddresses(String studentId) {
        return addressRepository.findAllByStudentId(studentId);
    }

    @Override
    @Transactional
    public void deleteStudentAddresses(String studentId) {
        addressRepository.deleteAllByStudentId(studentId);
    }
} 
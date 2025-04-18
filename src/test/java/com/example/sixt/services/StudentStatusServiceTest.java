package com.example.sixt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.sixt.controllers.requests.StatusCreationRequest;
import com.example.sixt.exceptions.InvalidDataException;
import com.example.sixt.models.StudentStatusEntity;
import com.example.sixt.repositories.StudentStatusRepository;
import com.example.sixt.services.impl.StudentStatusServiceImpl;

@ExtendWith(MockitoExtension.class)
public class StudentStatusServiceTest {
    @Mock
    private StudentStatusRepository studentStatusRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private StudentStatusServiceImpl studentStatusService;

    private StudentStatusEntity testStatus;
    private StatusCreationRequest creationRequest;

    @BeforeEach
    void setUp() {
        testStatus = new StudentStatusEntity();
        testStatus.setId(1L);
        testStatus.setName("Active");

        creationRequest = new StatusCreationRequest();
        creationRequest.setName("Active");
    }

    @Test
    void updateStatus_ShouldSuccess() {
        when(studentStatusRepository.findById(1L)).thenReturn(Optional.of(testStatus));
        when(studentStatusRepository.save(any())).thenReturn(testStatus);

        StudentStatusEntity result = studentStatusService.updateStatus(1L, "Inactive");

        assertEquals("Inactive", result.getName());
        verify(studentStatusRepository).save(testStatus);
    }

    @Test
    void updateStatus_ShouldThrowWhenNotFound() {
        when(studentStatusRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, 
            () -> studentStatusService.updateStatus(1L, "Inactive"));
    }

    @Test
    void updateStatus_ShouldThrowWhenNameInvalid() {
        assertThrows(InvalidDataException.class,
            () -> studentStatusService.updateStatus(1L, null));
        
        assertThrows(InvalidDataException.class,
            () -> studentStatusService.updateStatus(1L, " "));
    }

    @Test
    void addStatus_ShouldSuccess() {
        when(studentStatusRepository.findByName("Active")).thenReturn(null);
        when(studentStatusRepository.save(any())).thenReturn(testStatus);

        StudentStatusEntity result = studentStatusService.addStatus(creationRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(studentStatusRepository).save(any(StudentStatusEntity.class));
    }

    @Test
    void addStatus_ShouldThrowWhenNameExists() {
        when(studentStatusRepository.findByName("Active")).thenReturn(testStatus);

        assertThrows(InvalidDataException.class, 
            () -> studentStatusService.addStatus(creationRequest));
    }

    @Test
    void getStatusById_ShouldSuccess() {
        when(studentStatusRepository.findById(1L)).thenReturn(Optional.of(testStatus));

        StudentStatusEntity result = studentStatusService.getStatusById(1L);

        assertEquals(testStatus, result);
    }

    @Test
    void getStatusById_ShouldThrowWhenNotFound() {
        when(studentStatusRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, 
            () -> studentStatusService.getStatusById(1L));
    }
}
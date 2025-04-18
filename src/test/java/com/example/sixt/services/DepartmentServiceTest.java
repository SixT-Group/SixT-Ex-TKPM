package com.example.sixt.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.hibernate.annotations.processing.Exclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.sixt.exceptions.InvalidDataException;
import com.example.sixt.models.DepartmentEntity;
import com.example.sixt.repositories.DepartmentRepository;
import com.example.sixt.services.impl.DepartmentServiceImpl;


@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Spy
    private ModelMapper modelMapper;


    @InjectMocks
    private DepartmentServiceImpl departmentServiceImpl;

    private DepartmentEntity departmentEntity;

    @BeforeEach
    public void setUp() {
        departmentEntity = new DepartmentEntity();
        departmentEntity.setId(1L);
        departmentEntity.setName("HR");
    }

    @Test
    public void testUpdateDepartment_Success() {
        //Given
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(departmentEntity));
        when(departmentRepository.save(departmentEntity)).thenReturn(departmentEntity);
        //When
        DepartmentEntity updatedDepartment = departmentServiceImpl.updateDepartment(1L, "HR_Updated");
        //Then
        assert updatedDepartment.getName().equals("HR_Updated");
        assert updatedDepartment.getId().equals(1L);
        assert updatedDepartment.getName().equals("HR_Updated");
    }

     @Test
    public void testUpdateDepartment_NotFound() {
        // Given
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // When/Then
        assertThrows(InvalidDataException.class, () -> {
            departmentServiceImpl.updateDepartment(1L, "HR_Updated");
        });
    }

    
}

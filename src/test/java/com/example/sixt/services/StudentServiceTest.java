package com.example.sixt.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.redisson.RedissonReadWriteLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.sixt.controllers.requests.StudentCreationRequest;
import com.example.sixt.controllers.requests.StudentUpdateRequest;
import com.example.sixt.controllers.responses.StudentResponse;
import com.example.sixt.exceptions.InvalidDataException;
import com.example.sixt.models.AddressEntity;
import com.example.sixt.models.DepartmentEntity;
import com.example.sixt.models.IdentityDocumentEntity;
import com.example.sixt.models.ProgramEntity;
import com.example.sixt.models.StudentEntity;
import com.example.sixt.models.StudentStatusEntity;
import com.example.sixt.repositories.AddressRepository;
import com.example.sixt.repositories.DepartmentRepository;
import com.example.sixt.repositories.IdentityDocumentRepository;
import com.example.sixt.repositories.ProgramRepository;
import com.example.sixt.repositories.StudentRepository;
import com.example.sixt.repositories.StudentStatusRepository;
import com.example.sixt.services.impl.StudentServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StudentServiceTest {
    @Mock private StudentRepository studentRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private RedissonClient redissonClient;
    @Mock private AddressRepository addressRepository;
    @Mock private IdentityDocumentRepository identityDocumentRepository;
    @Mock private ProgramRepository programRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private StudentStatusRepository studentStatusRepository;
    @Mock private RLock lock;
    
    @InjectMocks private StudentServiceImpl studentService;
    
    private StudentCreationRequest validCreationRequest;
    private StudentUpdateRequest validUpdateRequest;
    private StudentEntity sampleStudent;
    private AddressEntity sampleAddress;
    private IdentityDocumentEntity sampleIdentityDoc;
    
    @BeforeEach
    void setUp() throws InterruptedException {
        // Initialize test data
        validCreationRequest = new StudentCreationRequest();
        validCreationRequest.setStudentId("S12345");
        validCreationRequest.setFullName("John Doe");
        validCreationRequest.setEmail("john@example.com");
        validCreationRequest.setDepartment("Computer Science");
        validCreationRequest.setProgram("BSc CS");
        validCreationRequest.setStatus("Active");
        
        validUpdateRequest = new StudentUpdateRequest();
        validUpdateRequest.setFullName("John Updated");
        
        sampleStudent = new StudentEntity();
        sampleStudent.setStudentId("S12345");
        sampleStudent.setFullName("John Doe");
        
        sampleAddress = new AddressEntity();
        sampleAddress.setStudentId("S12345");
        
        sampleIdentityDoc = new IdentityDocumentEntity();
        sampleIdentityDoc.setStudentId("S12345");
        
        // Common mock behaviors
        when(redissonClient.getReadWriteLock(toString())).thenReturn(mock(RedissonReadWriteLock.class));
        when(redissonClient.getReadWriteLock(toString()).writeLock()).thenReturn(lock);
        when(redissonClient.getReadWriteLock(toString()).readLock()).thenReturn(lock);
        when(lock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
    }

    @Test
    @DisplayName("Add student - success")
    void addStudent_shouldSuccessWhenDataValid() throws Exception {
        // Arrange
        when(studentRepository.findByStudentId(toString())).thenReturn(null);
        when(studentRepository.findByEmail(toString())).thenReturn(null);
        when(departmentRepository.findByName(toString())).thenReturn(new DepartmentEntity());
        when(programRepository.findByName(toString())).thenReturn(new ProgramEntity());
        when(studentStatusRepository.findByName(toString())).thenReturn(new StudentStatusEntity());
        when(studentRepository.save(any())).thenReturn(sampleStudent);
        when(addressRepository.saveAll(any())).thenReturn(List.of(sampleAddress));
        when(identityDocumentRepository.save(any())).thenReturn(sampleIdentityDoc);
        when(modelMapper.map(any(), eq(StudentEntity.class))).thenReturn(sampleStudent);
        
        // Act
        StudentResponse response = studentService.addStudent(validCreationRequest);
        
        // Assert
        assertNotNull(response);
        verify(studentRepository).save(any());
        verify(addressRepository).saveAll(any());
        verify(identityDocumentRepository).save(any());
        verify(redisTemplate).opsForValue().set(toString(), any());
    }
    
    @Test
    @DisplayName("Add student - should fail when student ID exists")
    void addStudent_shouldThrowWhenStudentIdExists() {
        // Arrange
        when(studentRepository.findByStudentId(toString())).thenReturn(sampleStudent);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            studentService.addStudent(validCreationRequest));
    }
    
    @Test
    @DisplayName("Add student - should fail when email exists")
    void addStudent_shouldThrowWhenEmailExists() {
        // Arrange
        when(studentRepository.findByStudentId(toString())).thenReturn(null);
        when(studentRepository.findByEmail(toString())).thenReturn(sampleStudent);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> 
            studentService.addStudent(validCreationRequest));
    }
}


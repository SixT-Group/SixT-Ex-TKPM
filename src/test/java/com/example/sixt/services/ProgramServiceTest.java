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

import com.example.sixt.controllers.requests.ProgramCreationRequest;
import com.example.sixt.exceptions.InvalidDataException;
import com.example.sixt.models.ProgramEntity;
import com.example.sixt.repositories.ProgramRepository;
import com.example.sixt.services.impl.ProgramServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProgramServiceTest {
    @Mock
    private ProgramRepository programRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private ProgramServiceImpl programService;

    private ProgramEntity testProgram;
    private ProgramCreationRequest request;

    @BeforeEach
    void setUp() {
        testProgram = new ProgramEntity();
        testProgram.setId(1L);
        testProgram.setName("Computer Science");


        request = new ProgramCreationRequest();
        request.setName("Computer Science");
    }

    @Test
    void addProgram_ShouldSuccess() {
        // Setup mocks
        when(programRepository.findByName(request.getName())).thenReturn(null);
        when(programRepository.save(any(ProgramEntity.class))).thenReturn(testProgram);

        // Execute
        ProgramEntity result = programService.addProgram(request);

        // Verify
        assertNotNull(result);
        assertEquals(testProgram.getId(), result.getId());
        assertEquals(testProgram.getName(), result.getName());
        
        // Verify interactions
        verify(programRepository).findByName(request.getName());
        verify(programRepository).save(any(ProgramEntity.class));
        verify(modelMapper).map(request, ProgramEntity.class);
    }

    @Test
    void addProgram_ShouldThrowWhenNameExists() {
        ProgramCreationRequest request = new ProgramCreationRequest();
        request.setName("Computer Science");

        when(programRepository.findByName(toString())).thenReturn(testProgram);

        assertThrows(RuntimeException.class, () -> programService.addProgram(request));
    }

    @Test
    void getProgramById_ShouldSuccess() {
        when(programRepository.findById(1L)).thenReturn(Optional.of(testProgram));

        ProgramEntity result = programService.getProgramById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getProgramById_ShouldThrowWhenNotFound() {
        when(programRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, () -> programService.getProgramById(1L));
    }

    @Test
    void updateProgram_ShouldSuccess() {
        when(programRepository.findById(1L)).thenReturn(Optional.of(testProgram));
        when(programRepository.save(any(ProgramEntity.class))).thenReturn(testProgram);

        ProgramEntity result = programService.updateProgram(1L, "Updated Name");

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
    }

    @Test
    void updateProgram_ShouldThrowWhenNotFound() {
        when(programRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, 
            () -> programService.updateProgram(1L, "Updated Name"));
    }

    @Test
    void updateProgram_ShouldThrowWhenNameInvalid() {
        assertThrows(InvalidDataException.class,
            () -> programService.updateProgram(1L, null));
        
        assertThrows(InvalidDataException.class,
            () -> programService.updateProgram(1L, " "));
    }
}
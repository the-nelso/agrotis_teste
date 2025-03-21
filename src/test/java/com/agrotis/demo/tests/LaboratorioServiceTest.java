package com.agrotis.service;

import com.agrotis.demo.model.Laboratorio;
import com.agrotis.demo.repository.LaboratorioRepository;
import com.agrotis.demo.service.LaboratorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LaboratorioServiceTest {

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @InjectMocks
    private LaboratorioService laboratorioService;

    @Test
    public void testGetRelatorio() {
        Laboratorio laboratorio = new Laboratorio();
        laboratorio.setId(1L);
        laboratorio.setNome("Laboratório 1");

        when(laboratorioRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.singletonList(laboratorio));

        List<Laboratorio> result = laboratorioService.getRelatorio(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                "teste",
                1
        );

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laboratório 1", result.get(0).getNome());

        verify(laboratorioRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void testGetRelatorio_SemResultados() {
        when(laboratorioRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.emptyList());

        List<Laboratorio> result = laboratorioService.getRelatorio(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                "inexistente",
                5
        );

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(laboratorioRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void testGetRelatorio_MultiplosLaboratorios() {
        Laboratorio lab1 = new Laboratorio();
        lab1.setId(1L);
        lab1.setNome("Laboratório A");

        Laboratorio lab2 = new Laboratorio();
        lab2.setId(2L);
        lab2.setNome("Laboratório B");

        List<Laboratorio> laboratorios = Arrays.asList(lab1, lab2);

        when(laboratorioRepository.findAll(any(Specification.class)))
                .thenReturn(laboratorios);

        List<Laboratorio> result = laboratorioService.getRelatorio(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                "observação",
                2
        );

        assertNotNull(result);
        assertEquals(2, result.size()); // Confirma que retornou dois laboratórios
        assertEquals("Laboratório A", result.get(0).getNome());
        assertEquals("Laboratório B", result.get(1).getNome());
        verify(laboratorioRepository, times(1)).findAll(any(Specification.class));
    }

}

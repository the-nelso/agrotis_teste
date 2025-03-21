package com.agrotis.demo.tests;

import com.agrotis.demo.model.Laboratorio;
import com.agrotis.demo.model.Pessoa;
import com.agrotis.demo.model.Propriedade;
import com.agrotis.demo.repository.LaboratorioRepository;
import com.agrotis.demo.repository.PessoaRepository;
import com.agrotis.demo.service.LaboratorioService;
import com.agrotis.demo.service.PessoaService;
import com.agrotis.demo.service.PropriedadeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private LaboratorioService laboratorioService;

    @Mock
    private PropriedadeService propriedadeService;

    @InjectMocks
    private PessoaService pessoaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João Silva");
        pessoa.setDataInicial(LocalDateTime.now());
        pessoa.setDataFinal(LocalDateTime.now().plusDays(10));

        Laboratorio laboratorio = new Laboratorio();
        laboratorio.setNome("Lab 1");
        laboratorio.setId(1L);
        pessoa.setLaboratorio(laboratorio);

        when(pessoaRepository.existsById(anyLong())).thenReturn(false);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Pessoa result = pessoaRepository.save(pessoa);

        assertNotNull(result);
        assertEquals("João Silva", result.getNome());

        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }


    @Test
    public void testCreatePessoaComLaboratorioInexistente() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João Silva");
        pessoa.setDataInicial(LocalDateTime.now());
        pessoa.setDataFinal(LocalDateTime.now().plusDays(10));
        pessoa.setLaboratorio(new Laboratorio());
        pessoa.getLaboratorio().setId(999L);

        when(laboratorioService.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> pessoaService.createPessoa(pessoa));
        verify(laboratorioService, times(1)).findById(999L);
    }

    @Test
    public void testCreatePessoaComDataInvalida() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Maria Souza");
        pessoa.setDataInicial(LocalDateTime.now().plusDays(5)); // Data inicial futura
        pessoa.setDataFinal(LocalDateTime.now()); // Data final no passado (inválida)

        Laboratorio laboratorio = new Laboratorio();
        laboratorio.setId(1L);
        laboratorio.setNome("Lab X");
        pessoa.setLaboratorio(laboratorio);

        when(laboratorioService.findById(1L)).thenReturn(Optional.of(laboratorio));

        assertThrows(ResponseStatusException.class, () -> pessoaService.createPessoa(pessoa));
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    @Test
    public void testUpdatePessoa() {
        Long pessoaId = 1L;

        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setId(pessoaId);
        pessoaExistente.setNome("João Antigo");

        Laboratorio laboratorio = new Laboratorio();
        laboratorio.setId(1L);
        laboratorio.setNome("Lab 1");

        Propriedade propriedade = new Propriedade();
        propriedade.setId(2L);
        propriedade.setNome("Propriedade 1");

        Pessoa pessoaAtualizada = new Pessoa();
        pessoaAtualizada.setNome("João Novo");
        pessoaAtualizada.setDataInicial(LocalDateTime.now());
        pessoaAtualizada.setDataFinal(LocalDateTime.now().plusDays(10));
        pessoaAtualizada.setLaboratorio(laboratorio);
        pessoaAtualizada.setInfosPropriedade(propriedade);
        pessoaAtualizada.setObservacoes("Observação alterada");

        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoaExistente));
        when(laboratorioService.findById(1L)).thenReturn(Optional.of(laboratorio));
        when(propriedadeService.findById(2L)).thenReturn(Optional.of(propriedade));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaAtualizada);

        Pessoa result = pessoaService.updatePessoa(pessoaId, pessoaAtualizada);

        assertNotNull(result);
        assertEquals("João Novo", result.getNome());
        assertEquals("Observação alterada", result.getObservacoes());
        assertEquals(1L, result.getLaboratorio().getId());
        assertEquals(2L, result.getInfosPropriedade().getId());

        verify(pessoaRepository, times(1)).findById(pessoaId);
        verify(laboratorioService, times(1)).findById(1L);
        verify(propriedadeService, times(1)).findById(2L);
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }

    @Test
    public void testDeletePessoa() {
        Long id = 1L;
        when(pessoaRepository.existsById(id)).thenReturn(true);

        pessoaService.deletePessoa(id);

        verify(pessoaRepository, times(1)).existsById(id);
        verify(pessoaRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeletePessoaComDependencias() {
        Long id = 3L;
        when(pessoaRepository.existsById(id)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("Pessoa vinculada a registros existentes"))
                .when(pessoaRepository).deleteById(id);

        assertThrows(DataIntegrityViolationException.class, () -> pessoaService.deletePessoa(id));
        verify(pessoaRepository, times(1)).existsById(id);
        verify(pessoaRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeletePessoaInexistente() {
        Long id = 999L;
        when(pessoaRepository.existsById(id)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> pessoaService.deletePessoa(id));
        verify(pessoaRepository, times(1)).existsById(id);
        verify(pessoaRepository, never()).deleteById(id);
    }
}

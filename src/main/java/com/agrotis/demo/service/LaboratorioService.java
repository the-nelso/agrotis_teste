package com.agrotis.demo.service;

import com.agrotis.demo.model.Laboratorio;
import com.agrotis.demo.repository.LaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LaboratorioService {

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    public List<Laboratorio> getRelatorio(
            LocalDateTime dataInicialStart,
            LocalDateTime dataInicialEnd,
            LocalDateTime dataFinalStart,
            LocalDateTime dataFinalEnd,
            String observacoes,
            int quantidadeMinimaPessoas) {

        Specification<Laboratorio> spec = LaboratorioSpecification.withFilters(
                dataInicialStart,
                dataInicialEnd,
                dataFinalStart,
                dataFinalEnd,
                observacoes,
                quantidadeMinimaPessoas
        );

        return laboratorioRepository.findAll(spec);
    }

    public Optional<Laboratorio> findById(Long id){
          return laboratorioRepository.findById(id);
    }
}

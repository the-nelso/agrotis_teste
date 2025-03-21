package com.agrotis.demo.service;

import com.agrotis.demo.model.Laboratorio;
import com.agrotis.demo.model.Propriedade;
import com.agrotis.demo.repository.PropriedadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class    PropriedadeService {

    @Autowired
    PropriedadeRepository propriedadeRepository;

    public Optional<Propriedade> findById(Long id){
        return propriedadeRepository.findById(id);
    }
}

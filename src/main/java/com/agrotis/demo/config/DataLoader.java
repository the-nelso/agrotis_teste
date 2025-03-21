package com.agrotis.demo.config;

import com.agrotis.demo.model.Laboratorio;
import com.agrotis.demo.model.Propriedade;
import com.agrotis.demo.repository.LaboratorioRepository;
import com.agrotis.demo.repository.PropriedadeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final LaboratorioRepository laboratorioRepository;
    private final PropriedadeRepository propriedadeRepository;

    public DataLoader(LaboratorioRepository laboratorioRepository, PropriedadeRepository propriedadeRepository) {
        this.laboratorioRepository = laboratorioRepository;
        this.propriedadeRepository = propriedadeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (laboratorioRepository.count() == 0) {
            Laboratorio lab1 = new Laboratorio();
            lab1.setNome("Laboratório 1");
            laboratorioRepository.save(lab1);

            Laboratorio lab2 = new Laboratorio();
            lab2.setNome("Laboratório 2");
            laboratorioRepository.save(lab2);

            Laboratorio lab3 = new Laboratorio();
            lab3.setNome("Laboratório 3");
            laboratorioRepository.save(lab3);

            System.out.println("Laboratórios cadastrados com sucesso!");
        }

        if (propriedadeRepository.count() == 0) {
            Propriedade prop1 = new Propriedade();
            prop1.setNome("Propriedade 1");
            propriedadeRepository.save(prop1);

            Propriedade prop2 = new Propriedade();
            prop2.setNome("Propriedade 2");
            propriedadeRepository.save(prop2);

            Propriedade prop3 = new Propriedade();
            prop3.setNome("Propriedade 3");
            propriedadeRepository.save(prop3);

            System.out.println("Propriedades cadastradas com sucesso!");
        }
    }
}

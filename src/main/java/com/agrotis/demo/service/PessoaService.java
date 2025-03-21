package com.agrotis.demo.service;

import com.agrotis.demo.model.Laboratorio;
import com.agrotis.demo.model.Pessoa;
import com.agrotis.demo.model.Propriedade;
import com.agrotis.demo.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LaboratorioService laboratorioService;

    @Autowired
    private PropriedadeService propriedadeService;

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    public Optional<Pessoa> findById(Long id) {
        return pessoaRepository.findById(id);
    }

    public ResponseEntity<Pessoa> save(Pessoa pessoa) throws Exception{
        Optional<Laboratorio> lab = laboratorioService.findById(pessoa.getLaboratorio().getId());
        if(lab.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pessoaRepository.save(pessoa));
    }

    public void deletePessoa(Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada com o ID: " + id);
        }

        pessoaRepository.deleteById(id);
    }

    public Pessoa createPessoa(Pessoa pessoa) {
        validarCamposObrigatorios(pessoa);

        if (pessoa.getLaboratorio() != null) {
            Long laboratorioId = pessoa.getLaboratorio().getId();
            Optional<Laboratorio> laboratorioOpt = laboratorioService.findById(laboratorioId);
            if (laboratorioOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Laboratório não encontrado com o ID: " + laboratorioId);
            }
            pessoa.setLaboratorio(laboratorioOpt.get());
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Laboratório não pode ser nulo: ");
        }

        if (pessoa.getInfosPropriedade() != null) {
            Long propriedadeId = pessoa.getInfosPropriedade().getId();
            Optional<Propriedade> propriedadeOpt = propriedadeService.findById(propriedadeId);
            if (propriedadeOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Propriedade não encontrada com o ID: " + propriedadeId);
            }
            pessoa.setInfosPropriedade(propriedadeOpt.get());
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Propriedade não pode ser nulo: ");
        }

        return pessoaRepository.save(pessoa);
    }

    public Pessoa updatePessoa(Long id, Pessoa pessoaAtualizada) {
        Optional<Pessoa> pessoaExistenteOpt = pessoaRepository.findById(id);
        if (pessoaExistenteOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada com o ID: " + id);
        }

        Pessoa pessoaExistente = pessoaExistenteOpt.get();

        validarCamposObrigatorios(pessoaAtualizada);

        if (pessoaAtualizada.getLaboratorio() != null) {
            Long laboratorioId = pessoaAtualizada.getLaboratorio().getId();
            Optional<Laboratorio> laboratorioOpt = laboratorioService.findById(laboratorioId);
            if (laboratorioOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Laboratório não encontrado com o ID: " + laboratorioId);
            }
            pessoaExistente.setLaboratorio(laboratorioOpt.get());
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Laboratório não pode ser nulo: ");
        }

        if (pessoaAtualizada.getInfosPropriedade() != null) {
            Long propriedadeId = pessoaAtualizada.getInfosPropriedade().getId();
            Optional<Propriedade> propriedadeOpt = propriedadeService.findById(propriedadeId);
            if (propriedadeOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Propriedade não encontrada com o ID: " + propriedadeId);
            }
            pessoaExistente.setInfosPropriedade(propriedadeOpt.get());
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Propriedade não pode ser nulo: ");
        }

        pessoaExistente.setNome(pessoaAtualizada.getNome());
        pessoaExistente.setDataInicial(pessoaAtualizada.getDataInicial());
        pessoaExistente.setDataFinal(pessoaAtualizada.getDataFinal());
        pessoaExistente.setObservacoes(pessoaAtualizada.getObservacoes());

        return pessoaRepository.save(pessoaExistente);
    }

    private void validarCamposObrigatorios(Pessoa pessoa) {
        if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'nome' é obrigatório.");
        }

        if (pessoa.getDataInicial() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'dataInicial' é obrigatório.");
        }

        if (pessoa.getDataFinal() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'dataFinal' é obrigatório.");
        }

        if (pessoa.getDataFinal().isBefore(pessoa.getDataInicial())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A 'dataFinal' deve ser posterior à 'dataInicial'.");
        }
    }
}

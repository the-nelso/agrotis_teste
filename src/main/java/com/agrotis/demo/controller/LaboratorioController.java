package com.agrotis.demo.controller;

import com.agrotis.demo.model.Laboratorio;
import com.agrotis.demo.service.LaboratorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/laboratorios")
public class LaboratorioController {

    @Autowired
    private LaboratorioService laboratorioService;

    @GetMapping("/relatorio")
    public ResponseEntity<List<Laboratorio>> getRelatorio(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicialStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicialEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinalStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinalEnd,
            @RequestParam(required = false) String observacoes,
            @RequestParam int quantidadeMinimaPessoas) {

        List<Laboratorio> laboratorios = laboratorioService.getRelatorio(
                dataInicialStart,
                dataInicialEnd,
                dataFinalStart,
                dataFinalEnd,
                observacoes,
                quantidadeMinimaPessoas
        );

        return ResponseEntity.ok(laboratorios);
    }
}

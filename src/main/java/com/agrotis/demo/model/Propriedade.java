package com.agrotis.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Propriedade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "infosPropriedade", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Pessoa> pessoas = new ArrayList<>();
}

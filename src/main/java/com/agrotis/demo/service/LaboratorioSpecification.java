package com.agrotis.demo.service;

import com.agrotis.demo.model.Laboratorio;
import com.agrotis.demo.model.Pessoa;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class LaboratorioSpecification {

    public static Specification<Laboratorio> withFilters(
            LocalDateTime dataInicialStart,
            LocalDateTime dataInicialEnd,
            LocalDateTime dataFinalStart,
            LocalDateTime dataFinalEnd,
            String observacoes,
            int quantidadeMinimaPessoas) {

        return (Root<Laboratorio> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            Join<Laboratorio, Pessoa> pessoasJoin = root.join("pessoas", JoinType.INNER);

            Predicate predicate = builder.conjunction();

            if (dataInicialStart != null) {
                predicate = builder.and(predicate, builder.greaterThanOrEqualTo(pessoasJoin.get("dataInicial"), dataInicialStart));
            }
            if (dataInicialEnd != null) {
                predicate = builder.and(predicate, builder.lessThanOrEqualTo(pessoasJoin.get("dataInicial"), dataInicialEnd));
            }

            if (dataFinalStart != null) {
                predicate = builder.and(predicate, builder.greaterThanOrEqualTo(pessoasJoin.get("dataFinal"), dataFinalStart));
            }
            if (dataFinalEnd != null) {
                predicate = builder.and(predicate, builder.lessThanOrEqualTo(pessoasJoin.get("dataFinal"), dataFinalEnd));
            }

            if (observacoes != null && !observacoes.isEmpty()) {
                predicate = builder.and(predicate, builder.like(pessoasJoin.get("observacoes"), "%" + observacoes + "%"));
            }

            query.groupBy(root.get("id"));
            query.having(builder.greaterThanOrEqualTo(builder.count(pessoasJoin), (long) quantidadeMinimaPessoas));

            if (dataInicialStart != null || dataInicialEnd != null) {
                query.orderBy(
                        builder.desc(builder.count(pessoasJoin)),
                        builder.asc(builder.min(pessoasJoin.get("dataInicial")))
                );
            } else {
                query.orderBy(builder.desc(builder.count(pessoasJoin)));
            }

            return predicate;
        };
    }
}
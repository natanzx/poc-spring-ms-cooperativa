package br.com.poc.desafio.domain.entity;


import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "PAUTA")
public class PautaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pauta_seq")
    @SequenceGenerator(name = "pauta_seq", sequenceName = "PAUTA_SEQ", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "TITULO", nullable = false)
    private String titulo;

    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL)
    private Set<SessaoVotacaoEntity> sessoes;

    @PrePersist
    public void onPrePersist() {
        this.setDataCriacao(LocalDateTime.now());
    }
}

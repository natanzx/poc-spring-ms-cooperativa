package br.com.poc.desafio.domain.entity;

import br.com.poc.desafio.domain.type.VotoType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "VOTO_ASSOCIADO")
public class VotoAssociadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voto_associado_seq")
    @SequenceGenerator(name = "voto_associado_seq", sequenceName = "VOTO_ASSOCIADO_SEQ", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "ASSOCIADO_CPF", nullable = false)
    private String associadoCpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "VOTO", nullable = false)
    private VotoType voto;

    @ManyToOne
    @JoinColumn(name = "SESSAO_VOTACAO_ID", nullable = false)
    private SessaoVotacaoEntity sessaoVotacao;
}

package br.com.poc.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.poc.desafio.domain.entity.VotoAssociadoEntity;
import br.com.poc.desafio.domain.type.VotoType;

@Repository
public interface VotoAssociadoRepository extends JpaRepository<VotoAssociadoEntity, Long> {

    Boolean existsBySessaoVotacao_PautaIdAndAssociadoCpf(final Long pautaId, final String associadoCpf);

    Integer countBySessaoVotacao_PautaIdAndVoto(final Long pautaId, final VotoType voto);
}

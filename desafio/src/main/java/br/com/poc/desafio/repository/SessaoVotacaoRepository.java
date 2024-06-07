package br.com.poc.desafio.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.poc.desafio.domain.entity.SessaoVotacaoEntity;

@Repository
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacaoEntity, Long> {

    Optional<SessaoVotacaoEntity> findByPautaIdAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
        final Long pautaId, final LocalDateTime dataInicio, final LocalDateTime dataFim);
}

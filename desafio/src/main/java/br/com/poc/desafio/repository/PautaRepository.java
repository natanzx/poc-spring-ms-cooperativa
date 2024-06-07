package br.com.poc.desafio.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.poc.desafio.domain.entity.PautaEntity;

@Repository
public interface PautaRepository extends JpaRepository<PautaEntity, Long> {

}

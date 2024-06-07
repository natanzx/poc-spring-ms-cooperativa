package br.com.poc.desafio.service;

import org.springframework.stereotype.Service;

import br.com.poc.desafio.repository.VotoAssociadoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidarAssociadoVotouEmPautaService {

    private final VotoAssociadoRepository repository;

    public Boolean validar(final Long pautaId, final String associadoCpf) {
        return repository.existsBySessaoVotacao_PautaIdAndAssociadoCpf(pautaId, associadoCpf);
    }

}

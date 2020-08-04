package com.sistemaponto.api.impl;

import java.util.Optional;

import com.sistemaponto.api.models.Funcionario;
import com.sistemaponto.api.repositories.FuncionarioRepository;
import com.sistemaponto.api.services.FuncionarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    private static final Logger log = LoggerFactory.getLogger(FuncionarioServiceImpl.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public Optional<Funcionario> buscaPorCPF(String cpf) {
        log.info("Buscando um funcionário para o CPF {}", cpf);
        return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
    }

    @Override
    public Optional<Funcionario> buscarPorEmail(String email) {
        log.info("Buscando um funcionário para o e-mail {}", email);
        return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
    }

    @Override
    public Optional<Funcionario> buscarPorId(Long id) {
        log.info("Buscando um funcionário para o id {}", id);
        Optional<Funcionario> funcionario = this.funcionarioRepository.findById(id);
        if(funcionario.isPresent()){
            return funcionario;
        }
        return null;
    }

    @Override
    public Funcionario persistir(Funcionario funcionario) {
        log.info("Persistindo funcionário: {}", funcionario);
        return this.funcionarioRepository.save(funcionario);
    }
    
    
}
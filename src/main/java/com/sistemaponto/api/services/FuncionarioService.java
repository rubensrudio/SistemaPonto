package com.sistemaponto.api.services;

import java.util.Optional;

import com.sistemaponto.api.models.Funcionario;

public interface FuncionarioService {
    
    /**
     * Persiste um funcionário na base de dados.
     * 
     * @param funcionario
     * @return Funcionario
     */
    Funcionario persistir(Funcionario funcionario);

    /**
     * Busca e retorna um funcionário dado um CPF.
     * 
     * @param cpf
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> buscaPorCPF(String cpf);

    /**
     * Busca e retorna um funcionário por ID.
     * 
     * @param email
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> buscarPorEmail(String email);
    
    /**
     * Busca e retorna um funcionario por ID
     * 
     * @param id
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> buscarPorId(Long id);
}
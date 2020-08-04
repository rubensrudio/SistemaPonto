package com.sistemaponto.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import com.sistemaponto.api.dtos.CadastroPJDto;
import com.sistemaponto.api.enums.PerfilEnum;
import com.sistemaponto.api.models.Empresa;
import com.sistemaponto.api.models.Funcionario;
import com.sistemaponto.api.response.Response;
import com.sistemaponto.api.services.EmpresaService;
import com.sistemaponto.api.services.FuncionarioService;
import com.sistemaponto.api.utils.PasswordUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {
    
    private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private EmpresaService empresaService;

    public CadastroPJController() {
    }

    @PostMapping
    public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto cadastroPJDto,
            BindingResult result) throws NoSuchAlgorithmException{
        log.info("Cadastrando PJ: {}", cadastroPJDto.toString());
        Response<CadastroPJDto> response = new Response<CadastroPJDto>();

        validarDadosExistentes(cadastroPJDto, result);
        Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDto);
        Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDto, result);

        if(result.hasErrors()){
            log.error("Erro validando dados de cadastro PJ: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.empresaService.persistir(empresa);
        funcionario.setEmpresa(empresa);
        this.funcionarioService.persistir(funcionario);
        
        response.setData(this.converterCadastroPjDto(funcionario));
        return ResponseEntity.ok(response);
    }

    /**
     * Verifica se a empresa ou funcionário já existem na base de dados.
     * 
     * @param cadastroPJDto
     * @param result
     */
    private void validarDadosExistentes(CadastroPJDto cadastroPJDto, BindingResult result){
        this.empresaService.buscarPorCnpj(cadastroPJDto.getCnpj())
            .ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));
        
        this.funcionarioService.buscaPorCPF(cadastroPJDto.getCpf())
            .ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

        this.funcionarioService.buscarPorEmail(cadastroPJDto.getEmail())
            .ifPresent(func -> result.addError(new ObjectError("funcionario", "E-mail já existente.")));
    }

    /**
     * Converte os dados do DTO para empresa.
     * 
     * @param cadastroPJDto
     * @return Empresa
     */
    private Empresa converterDtoParaEmpresa(CadastroPJDto cadastroPJDto){
        Empresa empresa = new Empresa();
        empresa.setCnpj(cadastroPJDto.getCnpj());
        empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());

        return empresa;
    }

    /**
     * Converte os dados do DTO para funcionário.
     * 
     * @param cadastroPJDto
     * @param result
     * @return Funcionario
     * @throws NoSuchAlgorithmException
     */
    private Funcionario converterDtoParaFuncionario(CadastroPJDto cadastroPJDto, BindingResult result)
            throws NoSuchAlgorithmException {
        Funcionario funcionario = new Funcionario();
        funcionario.setCpf(cadastroPJDto.getCpf());
        funcionario.setEmail(cadastroPJDto.getEmail());
        funcionario.setNome(cadastroPJDto.getNome());
        funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));
        funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);

        return funcionario;
    }

    /**
     * Popula o DTO de cadastro com os dados do funcionário e empresa.
     * 
     * @param funcionario
     * @return CadastroPJDto
     */
    private CadastroPJDto converterCadastroPjDto(Funcionario funcionario){
        CadastroPJDto cadastroPJDto = new CadastroPJDto();
        cadastroPJDto.setId(funcionario.getId());
        cadastroPJDto.setNome(funcionario.getNome());
        cadastroPJDto.setEmail(funcionario.getEmail());
        cadastroPJDto.setCpf(funcionario.getCpf());
        cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
        cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());

        return cadastroPJDto;
    }
}
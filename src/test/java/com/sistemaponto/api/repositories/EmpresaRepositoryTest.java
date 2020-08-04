package com.sistemaponto.api.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sistemaponto.api.models.Empresa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest {
    
    @Autowired
    private EmpresaRepository empresaRepository;

    private static final String CNPJ = "01234560001789";

    @Before
    public void setUp() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa Exemplo");
        empresa.setCnpj(CNPJ);
        this.empresaRepository.save(empresa);
    }

    @After
    public void tearDown() throws Exception{
        this.empresaRepository.deleteAll();
    }

    @Test
    public void buscarEmpresaPorCNPJ(){
        Empresa empresa = new Empresa();
        empresa = this.empresaRepository.findByCnpj(CNPJ);

        assertEquals(CNPJ, empresa.getCnpj());
    }
}
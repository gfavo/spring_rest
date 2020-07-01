package com.example.demo;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@RestController
public class Controller {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/login")
    public boolean login() {
        return true;
    }
    
    @GetMapping("/transacoes")
    public List<Transacao> transacao(@RequestParam(value = "name")String name) {
        
     int id =  jdbcTemplate.queryForObject("select id from usuario where nome=?", new Object[] { name }, Integer.class);
        
     List<Transacao> transacoes = jdbcTemplate.query("select produto,valor from transacoes where idusuario=?",new Object[] { id }, new BeanPropertyRowMapper(Transacao.class));
     
     return transacoes;
    }
    
    @GetMapping("/idqr")
    public int idqr(@RequestParam(value = "name") String name) {
        int idqrcode =  jdbcTemplate.queryForObject("select idqrcode from usuario where nome=?", new Object[] { name }, Integer.class);
        
        return idqrcode;
    }
    
     @GetMapping("/cadastro")
    public boolean cadastro(@RequestParam(value = "nome") String nome,@RequestParam(value = "senha") String senha,@RequestParam(value = "qrcode") int qrcode) {
        
     jdbcTemplate.update("insert into usuario (nome,senha,idqrcode) values(?,?,?)", nome,senha,qrcode);
        
        return true;
    }
    
       @GetMapping("/cadastroTransacoes")
    public boolean cadastroTransacoes(@RequestParam(value = "nome") String nome,@RequestParam(value = "valor") int valor,@RequestParam(value = "produto") String produto) {
        
      int id =  jdbcTemplate.queryForObject("select id from usuario where nome=?", new Object[] { nome }, Integer.class);
        
     jdbcTemplate.update("insert into transacoes (valor,produto,idusuario) values(?,?,?)", valor,produto,id);
        
        return true;
    }


}

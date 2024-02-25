package com.example.crudcidades.cidade;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;


@Controller
public class CidadeController {

    private Set<Cidade> cidades;

    private final CidadeRepository repository;

    public CidadeController(CidadeRepository repository) {
        this.cidades = new HashSet<>();
        this.repository = repository;
    }

    @GetMapping("/")    
    public String listar(Model memoria){

        memoria.addAttribute("listaCidades", this.repository
                                               .findAll()
                                                .stream()
                                                .map(cidade -> new Cidade(
                                                    cidade.getNome(), 
                                                    cidade.getEstado()))
                                                .collect(Collectors.toList()));

        return "/crud";
    }

    @PostMapping("/criar")
    public String criar(@Valid Cidade cidade, BindingResult validacao, Model memoria) {
        
        if (validacao.hasErrors()){
            validacao
                .getFieldErrors()
                .forEach(error ->
                        memoria.addAttribute(
                            error.getField(),
                            error.getDefaultMessage()
                        )
                );  

            memoria.addAttribute("nomeInformado", cidade.getNome());
            memoria.addAttribute("estadoInformado", cidade.getEstado());
            memoria.addAttribute("listaCidades", this.cidades);

            return ("/crud");
        } else{
            this.repository.save(cidade.clonar());
        }
        return "redirect:/";
    }
    
    @GetMapping("/excluir")
    public String excluir(
        @RequestParam String nome,
        @RequestParam String estado) {
          
        var cidadeEstadoEncontrada = this.repository.findByNomeAndEstado(nome, estado);

        cidadeEstadoEncontrada.ifPresent(this.repository::delete);
        
        return "redirect:/";
    }

    @GetMapping("/prepararAlterar")
    public String prepararAlterar(
        @RequestParam String nome,
        @RequestParam String estado,
        Model memoria){
            var cidadeAtual = this.repository.findByNomeAndEstado(nome, estado);

            cidadeAtual.ifPresent(cidadeEncontrada -> {
                memoria.addAttribute("cidadeAtual", cidadeEncontrada);
                memoria.addAttribute("listaCidades", this.repository.findAll());
            });

        return "/crud";
    }

    @PostMapping("/alterar")
    public String alterar(
        @RequestParam String nomeAtual,
        @RequestParam String estadoAtual,
        Cidade cidade){

            var cidadeAtual = this.repository.findByNomeAndEstado(nomeAtual, estadoAtual);

            if(cidadeAtual.isPresent()){
                var cidadeEncontrada = cidadeAtual.get();
                cidadeEncontrada.setNome(cidade.getNome());
                cidadeEncontrada.setEstado(cidade.getEstado());

                this.repository.saveAndFlush(cidadeEncontrada);
            }

            return "redirect:/";

    }
    
}

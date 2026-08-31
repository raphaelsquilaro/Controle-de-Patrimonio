package sp.senai.org.controle_de_almoxarifado.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sp.senai.org.controle_de_almoxarifado.repository.MovimentacaoRepository;

@Controller
@RequestMapping("/movimentacao")
public class MovimentacaoController {

    private final MovimentacaoRepository movimentacaoRepository;

    public MovimentacaoController(
            MovimentacaoRepository movimentacaoRepository
    ) {
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @GetMapping("/listar")
    public String listar(Model model) {

        model.addAttribute(
                "movimentacoes",
                movimentacaoRepository.findAll()
        );

        return "movimentacao/listar_movimentacao";
    }
}
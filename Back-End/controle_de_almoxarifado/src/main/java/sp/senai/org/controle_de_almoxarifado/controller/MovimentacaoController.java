package sp.senai.org.controle_de_almoxarifado.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sp.senai.org.controle_de_almoxarifado.service.MovimentacaoService;

@Controller
@RequestMapping("/movimentacao")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(
            MovimentacaoService movimentacaoService
    ) {
        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute(
                "movimentacoes",
                movimentacaoService.listarMovimentacoes()
        );

        return "movimentacao/listar_movimentacao";
    }
}

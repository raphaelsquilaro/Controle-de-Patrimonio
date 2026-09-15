package sp.senai.org.controle_de_almoxarifado.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sp.senai.org.controle_de_almoxarifado.model.Produto;
import sp.senai.org.controle_de_almoxarifado.model.enums.StatusProduto;
import sp.senai.org.controle_de_almoxarifado.repository.ProdutoRepository;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/listar")
    public String listar(Model model) {

        model.addAttribute(
                "produtos",
                produtoRepository.findAll()
        );

        return "produto/listar_produto";
    }

    @GetMapping("/form-cadastro")
    public String cadastro(Model model) {

        model.addAttribute(
                "produto",
                new Produto()
        );

        model.addAttribute(
                "statusProdutos",
                StatusProduto.values()
        );

        return "produto/cadastro_produto";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("produto") Produto produto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "statusProdutos",
                    StatusProduto.values()
            );

            return "produto/cadastro_produto";
        }

        produtoRepository.save(produto);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Produto salvo com sucesso!"
        );

        return "redirect:/produto/listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        return produtoRepository.findById(id)
                .map(produto -> {

                    model.addAttribute(
                            "produto",
                            produto
                    );

                    model.addAttribute(
                            "statusProdutos",
                            StatusProduto.values()
                    );

                    return "produto/cadastro_produto";

                })
                .orElseGet(() -> {

                    redirectAttributes.addFlashAttribute(
                            "mensagem",
                            "Produto não encontrado!"
                    );

                    return "redirect:/produto/listar";
                });
    }

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        if (produtoRepository.existsById(id)) {

            produtoRepository.deleteById(id);

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Produto excluído com sucesso!"
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Produto não encontrado!"
            );
        }

        return "redirect:/produto/listar";
    }
}
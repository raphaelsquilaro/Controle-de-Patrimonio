package sp.senai.org.controle_de_almoxarifado.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sp.senai.org.controle_de_almoxarifado.model.Ambiente;
import sp.senai.org.controle_de_almoxarifado.model.Produto;
import sp.senai.org.controle_de_almoxarifado.repository.AmbienteRepository;

import java.util.ArrayList;

@Controller
@RequestMapping("/ambiente")
public class AmbienteController {

    private final AmbienteRepository repository;


    public AmbienteController(AmbienteRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/listar")
    public String listar(Model model) {

        model.addAttribute(
                "ambientes",
                repository.findAll()
        );

        return "ambiente/listar_ambiente";
    }


    @GetMapping("/form-cadastro")
    public String cadastro(Model model) {

        Ambiente ambiente = new Ambiente();

        if (ambiente.getProdutos() == null) {
            ambiente.setProdutos(new ArrayList<>());
        }

        model.addAttribute(
                "ambiente",
                ambiente
        );

        return "ambiente/cadastro_ambiente";
    }


    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("ambiente") Ambiente ambiente,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {

            return "ambiente/cadastro_ambiente";
        }


        if (ambiente.getProdutos() == null) {

            ambiente.setProdutos(
                    new ArrayList<>()
            );
        }


        repository.save(ambiente);


        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Ambiente salvo com sucesso!"
        );


        return "redirect:/ambiente/listar";
    }


    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        return repository.findById(id)

                .map(ambiente -> {

                    if (ambiente.getProdutos() == null) {

                        ambiente.setProdutos(
                                new ArrayList<>()
                        );
                    }

                    model.addAttribute(
                            "ambiente",
                            ambiente
                    );

                    return "ambiente/cadastro_ambiente";

                })

                .orElseGet(() -> {

                    redirectAttributes.addFlashAttribute(
                            "mensagem",
                            "Ambiente não encontrado"
                    );

                    return "redirect:/ambiente/listar";
                });
    }


    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        if (repository.existsById(id)) {

            repository.deleteById(id);

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Ambiente excluído com sucesso!"
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Ambiente não encontrado!"
            );
        }


        return "redirect:/ambiente/listar";
    }


    @PostMapping("/addproduto")
    public String addProduto(
            @ModelAttribute("ambiente") Ambiente ambiente,
            Model model
    ) {

        if (ambiente.getProdutos() == null) {

            ambiente.setProdutos(
                    new ArrayList<>()
            );
        }


        ambiente.getProdutos().add(
                new Produto()
        );


        model.addAttribute(
                "ambiente",
                ambiente
        );


        return "ambiente/cadastro_ambiente :: produtos";
    }


    @PostMapping("/removeproduto")
    public String removeProduto(
            @ModelAttribute("ambiente") Ambiente ambiente,
            @RequestParam("value") int index,
            Model model
    ) {

        if (
                ambiente.getProdutos() != null
                        && index >= 0
                        && index < ambiente.getProdutos().size()
        ) {

            ambiente.getProdutos().remove(index);
        }


        model.addAttribute(
                "ambiente",
                ambiente
        );


        return "ambiente/cadastro_ambiente :: produtos";
    }
}
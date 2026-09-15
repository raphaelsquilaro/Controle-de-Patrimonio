package sp.senai.org.controle_de_almoxarifado.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sp.senai.org.controle_de_almoxarifado.model.LeitorRFID;
import sp.senai.org.controle_de_almoxarifado.repository.LeitorRepository;

import java.util.Optional;

@Controller
@RequestMapping("/leitor")
public class LeitorController {

    private final LeitorRepository leitorRepository;

    public LeitorController(LeitorRepository leitorRepository) {
        this.leitorRepository = leitorRepository;
    }

    @GetMapping("/listar")
    public String listar(Model model) {

        model.addAttribute(
                "leitores",
                leitorRepository.findAll()
        );

        return "leitor/listar_leitor";
    }

    @GetMapping("/form-cadastro")
    public String cadastro(Model model) {

        model.addAttribute(
                "leitorRFID",
                new LeitorRFID()
        );

        return "leitor/cadastro_leitor";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("leitorRFID") LeitorRFID leitorRFID,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {
            return "leitor/cadastro_leitor";
        }

        leitorRepository.save(leitorRFID);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Leitor RFID salvo com sucesso!"
        );

        return "redirect:/leitor/listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        Optional<LeitorRFID> leitorOpt =
                leitorRepository.findById(id);

        if (leitorOpt.isPresent()) {

            model.addAttribute(
                    "leitorRFID",
                    leitorOpt.get()
            );

            return "leitor/cadastro_leitor";
        }

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Leitor não encontrado!"
        );

        return "redirect:/leitor/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        Optional<LeitorRFID> leitorOpt =
                leitorRepository.findById(id);

        if (leitorOpt.isPresent()) {

            leitorRepository.delete(leitorOpt.get());

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Leitor RFID removido com sucesso!");

        } else {

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Registro de leitor não encontrado para exclusão."
            );
        }

        return "redirect:/leitor/listar";
    }
}
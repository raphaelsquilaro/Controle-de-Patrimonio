package sp.senai.org.controle_de_almoxarifado.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sp.senai.org.controle_de_almoxarifado.model.Usuario;
import sp.senai.org.controle_de_almoxarifado.model.enums.NivelAcesso;
import sp.senai.org.controle_de_almoxarifado.repository.UsuarioRepository;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    // ==========================================
    // LISTAR
    // GET /user/listar
    // ==========================================

    @GetMapping("/listar")
    public String listar(Model model) {

        model.addAttribute(
                "users",
                usuarioRepository.findAll()
        );

        return "usuario/listar_usuario";
    }


    // ==========================================
    // FORMULÁRIO DE CADASTRO
    // GET /user/form-cadastro
    // ==========================================

    @GetMapping("/form-cadastro")
    public String cadastro(Model model) {

        model.addAttribute(
                "usuario",
                new Usuario()
        );

        model.addAttribute(
                "niveisAcesso",
                NivelAcesso.values()
        );

        return "usuario/cadastro_usuario";
    }


    // ==========================================
    // SALVAR
    // POST /user/salvar
    // ==========================================

    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "niveisAcesso",
                    NivelAcesso.values()
            );

            return "usuario/cadastro_usuario";
        }

        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Usuário salvo com sucesso!"
        );

        return "redirect:/user/listar";
    }


    // ==========================================
    // EDITAR
    // GET /user/editar/{id}
    // ==========================================

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        Optional<Usuario> usuarioOpt =
                usuarioRepository.findById(id);

        if (usuarioOpt.isPresent()) {

            model.addAttribute(
                    "usuario",
                    usuarioOpt.get()
            );

            model.addAttribute(
                    "niveisAcesso",
                    NivelAcesso.values()
            );

            return "usuario/cadastro_usuario";

        } else {

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Usuário não encontrado!"
            );

            return "redirect:/user/listar";
        }
    }


    // ==========================================
    // EXCLUIR
    // GET /user/excluir/{id}
    // ==========================================

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        Optional<Usuario> usuarioOpt =
                usuarioRepository.findById(id);

        if (usuarioOpt.isPresent()) {

            usuarioRepository.delete(
                    usuarioOpt.get()
            );

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Usuário removido com sucesso!"
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Usuário não encontrado para exclusão!"
            );
        }

        return "redirect:/user/listar";
    }
}
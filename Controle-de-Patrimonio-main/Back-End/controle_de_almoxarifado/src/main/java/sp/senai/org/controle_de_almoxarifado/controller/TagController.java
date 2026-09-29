package sp.senai.org.controle_de_almoxarifado.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sp.senai.org.controle_de_almoxarifado.model.Produto;
import sp.senai.org.controle_de_almoxarifado.model.TagRFID;
import sp.senai.org.controle_de_almoxarifado.repository.ProdutoRepository;
import sp.senai.org.controle_de_almoxarifado.repository.TagRepository;

import java.util.Optional;

@Controller
@RequestMapping("/tag")
public class TagController {

    private final ProdutoRepository produtoRepository;
    private final TagRepository tagRepository;

    public TagController(
            ProdutoRepository produtoRepository,
            TagRepository tagRepository) {

        this.produtoRepository = produtoRepository;
        this.tagRepository = tagRepository;
    }

    // 1. Listar todas as Tags
    // Rota: /tag/listar
    @GetMapping("/listar")
    public String listar(Model model) {

        model.addAttribute("tags", tagRepository.findAll());

        return "tag/listar_tag";
    }

    // 2. Formulário de cadastro
    // Rota: /tag/form-cadastro
    @GetMapping("/form-cadastro")
    public String cadastro(Model model) {

        TagRFID tag = new TagRFID();

        tag.setProduto(new Produto());
        tag.setAtivo(true);

        model.addAttribute("tagRFID", tag);
        model.addAttribute("produtos", produtoRepository.findAll());

        return "tag/cadastro_tag";
    }

    // 3. Salvar / Atualizar Tag
    // Rota: /tag/salvar
    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute("tagRFID") TagRFID tag,
            RedirectAttributes redirectAttributes) {

        // Verifica se está editando uma Tag existente
        if (tag.getId() != null) {

            Optional<TagRFID> tagExistenteOpt =
                    tagRepository.findById(tag.getId());

            if (tagExistenteOpt.isPresent()) {

                TagRFID tagExistente = tagExistenteOpt.get();

                // Atualiza o código da Tag
                tagExistente.setCodigoTag(tag.getCodigoTag());

                // Atualiza o status
                tagExistente.setAtivo(tag.getAtivo());

                // Atualiza o produto
                if (tag.getProduto() != null
                        && tag.getProduto().getId() != null) {

                    Optional<Produto> produtoOpt =
                            produtoRepository.findById(tag.getProduto().getId());

                    if (produtoOpt.isPresent()) {
                        tagExistente.setProduto(produtoOpt.get());
                    }
                } else {
                    tagExistente.setProduto(null);
                }

                tagRepository.save(tagExistente);

                redirectAttributes.addFlashAttribute(
                        "mensagem",
                        "Tag alterada com sucesso!"
                );

                return "redirect:/tag/listar";
            }
        }

        // Cadastro de uma nova Tag
        if (tag.getProduto() != null
                && tag.getProduto().getId() != null) {

            Long produtoId = tag.getProduto().getId();

            Optional<Produto> produtoOpt =
                    produtoRepository.findById(produtoId);

            if (produtoOpt.isPresent()) {
                tag.setProduto(produtoOpt.get());
            } else {
                redirectAttributes.addFlashAttribute(
                        "mensagem",
                        "Produto não encontrado!"
                );

                return "redirect:/tag/form-cadastro";
            }
        }

        tagRepository.save(tag);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Tag cadastrada com sucesso!"
        );

        return "redirect:/tag/listar";
    }

    // 4. Carregar formulário para edição
    // Rota: /tag/editar/{id}
    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<TagRFID> tagOpt = tagRepository.findById(id);

        if (tagOpt.isPresent()) {

            model.addAttribute("tagRFID", tagOpt.get());
            model.addAttribute(
                    "produtos",
                    produtoRepository.findAll()
            );

            return "tag/cadastro_tag";

        } else {

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Tag não encontrada!"
            );

            return "redirect:/tag/listar";
        }
    }

    // 5. Excluir Tag
    // Rota: /tag/excluir/{id}
    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Optional<TagRFID> tagOpt = tagRepository.findById(id);

        if (tagOpt.isPresent()) {

            TagRFID tag = tagOpt.get();

            // Remove a associação com o produto
            tag.setProduto(null);

            tagRepository.save(tag);

            // Exclui a Tag
            tagRepository.delete(tag);

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Tag removida com sucesso!"
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Tag não encontrada para exclusão."
            );
        }

        return "redirect:/tag/listar";
    }
}
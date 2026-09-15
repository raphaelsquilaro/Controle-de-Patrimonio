package sp.senai.org.controle_de_almoxarifado.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sp.senai.org.controle_de_almoxarifado.model.Estoque;
import sp.senai.org.controle_de_almoxarifado.model.Produto;
import sp.senai.org.controle_de_almoxarifado.repository.EstoqueRepository;
import sp.senai.org.controle_de_almoxarifado.repository.ProdutoRepository;

import java.util.Optional;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueRepository estoquerepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueController(EstoqueRepository estoquerepository, ProdutoRepository produtoRepository) {
        this.estoquerepository = estoquerepository;
        this.produtoRepository = produtoRepository;
    }

    // 1. Listar todos os estoques (Rota: /estoque/listar)
    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("estoques", estoquerepository.findAll());
        return "estoque/listar_estoque"; // Nome do arquivo HTML em templates/estoque/
    }

    // 2. Formulário de Cadastro (Rota: /estoque/form-cadastro)
    @GetMapping("/form-cadastro")
    public String cadastro(Model model) {
        Estoque estoque = new Estoque();
        estoque.setProduto(new Produto()); // Inicializa objeto para evitar erro 400 no binding do Thymeleaf

        model.addAttribute("estoque", estoque);
        model.addAttribute("produtos", produtoRepository.findAll());
        return "estoque/cadastro_estoque";
    }

    // 3. Salvar / Atualizar Estoque (Rota: /estoque/salvar)
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("estoque") Estoque estoque, RedirectAttributes redirectAttributes) {

        // 1. Tratamento para EDIÇÃO de um estoque já existente (via ID do estoque)
        if (estoque.getId() != null) {
            Optional<Estoque> estoqueExistenteOpt = estoquerepository.findById(estoque.getId());
            if (estoqueExistenteOpt.isPresent()) {
                Estoque estoqueExistente = estoqueExistenteOpt.get();
                estoqueExistente.setQuantidade(estoque.getQuantidade());

                if (estoque.getProduto() != null && estoque.getProduto().getId() != null) {
                    Produto produto = produtoRepository.findById(estoque.getProduto().getId()).orElse(null);
                    estoqueExistente.setProduto(produto);
                }

                estoquerepository.save(estoqueExistente);
                redirectAttributes.addFlashAttribute("mensagem", "Estoque alterado com sucesso!");
                return "redirect:/estoque/listar";
            }
        }

        // 2. Tratamento para NOVO CADASTRO
        if (estoque.getProduto() != null && estoque.getProduto().getId() != null) {
            Long produtoId = estoque.getProduto().getId();

            // Verifica se O PRODUTO já possui um estoque cadastrado no banco
            Optional<Estoque> estoquePorProduto = estoquerepository.findByProdutoId(produtoId);

            if (estoquePorProduto.isPresent()) {
                // Se já existe, soma a nova quantidade com a quantidade atual
                Estoque estoqueExistente = estoquePorProduto.get();
                int novaQuantidade = estoqueExistente.getQuantidade() + estoque.getQuantidade();
                estoqueExistente.setQuantidade(novaQuantidade);

                estoquerepository.save(estoqueExistente);
                redirectAttributes.addFlashAttribute("mensagem", "Quantidade somada ao estoque existente do produto com sucesso!");
                return "redirect:/estoque/listar";
            }

            // Se o produto ainda não tem estoque, busca o Produto do banco e associa
            Produto produto = produtoRepository.findById(produtoId).orElse(null);
            estoque.setProduto(produto);
        }

        estoquerepository.save(estoque);
        redirectAttributes.addFlashAttribute("mensagem", "Estoque cadastrado com sucesso!");
        return "redirect:/estoque/listar";
    }

    // 4. Carregar formulário para Edição (Rota: /estoque/editar/{id})
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Estoque> estoqueOpt = estoquerepository.findById(id);

        if (estoqueOpt.isPresent()) {
            model.addAttribute("estoque", estoqueOpt.get());
            model.addAttribute("produtos", produtoRepository.findAll());
            return "estoque/cadastro_estoque";
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Estoque não encontrado!");
            // CORRIGIDO: Redireciona para a URL /estoque/listar
            return "redirect:/estoque/listar";
        }
    }

    // 5. Excluir Estoque (Rota: /estoque/excluir/{id})
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Estoque> estoqueOpt = estoquerepository.findById(id);

        if (estoqueOpt.isPresent()) {
            Estoque estoque = estoqueOpt.get();

            // 1. Quebra a ligação com o Produto se existir
            if (estoque.getProduto() != null) {
                Produto produto = estoque.getProduto();
                produto.setEstoque(null); // Remove a referência no Produto
                estoque.setProduto(null);  // Remove a referência no Estoque
                produtoRepository.save(produto); // Atualiza o produto sem o estoque
            }

            // 2. Agora deleta o Estoque com segurança
            estoquerepository.delete(estoque);

            redirectAttributes.addFlashAttribute("mensagem", "Estoque removido com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Registro de estoque não encontrado para exclusão.");
        }

        return "redirect:/estoque/listar";
    }
}
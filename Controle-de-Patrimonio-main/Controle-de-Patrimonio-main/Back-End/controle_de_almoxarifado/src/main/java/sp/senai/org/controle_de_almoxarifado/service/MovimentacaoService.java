package sp.senai.org.controle_de_almoxarifado.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import sp.senai.org.controle_de_almoxarifado.DTO.Request.MovimentacaoRequest;
import sp.senai.org.controle_de_almoxarifado.model.Estoque;
import sp.senai.org.controle_de_almoxarifado.model.Movimentacao;
import sp.senai.org.controle_de_almoxarifado.model.Produto;
import sp.senai.org.controle_de_almoxarifado.model.Usuario;
import sp.senai.org.controle_de_almoxarifado.repository.EstoqueRepository;
import sp.senai.org.controle_de_almoxarifado.repository.MovimentacaoRepository;
import sp.senai.org.controle_de_almoxarifado.repository.ProdutoRepository;
import sp.senai.org.controle_de_almoxarifado.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstoqueRepository estoqueRepository;

    public MovimentacaoService(
            MovimentacaoRepository movimentacaoRepository,
            ProdutoRepository produtoRepository,
            UsuarioRepository usuarioRepository,
            EstoqueRepository estoqueRepository
    ) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.estoqueRepository = estoqueRepository;
    }

    @Transactional
    public Movimentacao criarMovimentacao(
            MovimentacaoRequest request
    ) {

        // =========================
        // VALIDACOES
        // =========================

        if (request.getProdutoId() == null) {
            throw new RuntimeException(
                    "O produto é obrigatório"
            );
        }

        if (request.getUsuarioId() == null) {
            throw new RuntimeException(
                    "O usuário é obrigatório"
            );
        }

        if (request.getQuantidade() == null
                || request.getQuantidade() <= 0) {

            throw new RuntimeException(
                    "A quantidade deve ser maior que zero"
            );
        }

        if (request.getTipo() == null) {
            throw new RuntimeException(
                    "O tipo de movimentação é obrigatório"
            );
        }

        // =========================
        // BUSCAR PRODUTO
        // =========================

        Produto produto = produtoRepository
                .findById(request.getProdutoId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Produto não encontrado"
                        )
                );

        // =========================
        // BUSCAR USUARIO
        // =========================

        Usuario usuario = usuarioRepository
                .findById(request.getUsuarioId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado"
                        )
                );

        // =========================
        // BUSCAR ESTOQUE
        // =========================

        Estoque estoque = estoqueRepository
                .findByProdutoId(produto.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Estoque do produto não encontrado"
                        )
                );

        int quantidadeAtual = estoque.getQuantidade();

        // =========================
        // ATUALIZAR ESTOQUE
        // =========================

        switch (request.getTipo()) {

            case ENTRADA:
                estoque.setQuantidade(
                        quantidadeAtual + request.getQuantidade()
                );
                break;

            case SAIDA:

                if (quantidadeAtual < request.getQuantidade()) {
                    throw new RuntimeException(
                            "Quantidade insuficiente em estoque"
                    );
                }

                estoque.setQuantidade(
                        quantidadeAtual - request.getQuantidade()
                );
                break;

            case DEVOLUCAO:
                estoque.setQuantidade(
                        quantidadeAtual + request.getQuantidade()
                );
                break;

            case EXTRAVIO:

                if (quantidadeAtual < request.getQuantidade()) {
                    throw new RuntimeException(
                            "Quantidade insuficiente em estoque"
                    );
                }

                estoque.setQuantidade(
                        quantidadeAtual - request.getQuantidade()
                );
                break;

            case MANUTENCAO:
                // Não altera a quantidade do estoque
                break;

            default:
                throw new RuntimeException(
                        "Tipo de movimentação inválido"
                );
        }

        // Salva a nova quantidade
        estoqueRepository.save(estoque);

        // =========================
        // CRIAR MOVIMENTACAO
        // =========================

        Movimentacao movimentacao = new Movimentacao();

        movimentacao.setProduto(produto);
        movimentacao.setUsuario(usuario);
        movimentacao.setTipo(request.getTipo());
        movimentacao.setQuantidade(request.getQuantidade());
        movimentacao.setDataHora(LocalDateTime.now());

        // =========================
        // SALVAR
        // =========================

        return movimentacaoRepository.save(movimentacao);
    }

    public List<Movimentacao> listarMovimentacoes() {

        return movimentacaoRepository.findAll();
    }

    public Movimentacao buscarPorId(Long id) {

        return movimentacaoRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Movimentação não encontrada"
                        )
                );
    }
}

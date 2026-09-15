package sp.senai.org.controle_de_almoxarifado.model;

import jakarta.persistence.*;
import sp.senai.org.controle_de_almoxarifado.model.enums.StatusProduto;

import java.util.List;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private String categoria;

    private String codigoPatrimonio;

    private String localizacao;

    @Enumerated(EnumType.STRING)
    private StatusProduto status;

    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL)
    private Estoque estoque;

    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL)
    private TagRFID tagRFID;

    @OneToMany(mappedBy = "produto")
    private List<Movimentacao> movimentacaos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public StatusProduto getStatus() {
        return status;
    }

    public void setStatus(StatusProduto status) {
        this.status = status;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public TagRFID getTagRFID() {
        return tagRFID;
    }

    public void setTagRFID(TagRFID tagRFID) {
        this.tagRFID = tagRFID;
    }

    public List<Movimentacao> getMovimentacaos() {
        return movimentacaos;
    }

    public void setMovimentacaos(List<Movimentacao> movimentacaos) {
        this.movimentacaos = movimentacaos;
    }

    public String getCodigoPatrimonio() {
        return codigoPatrimonio;
    }

    public void setCodigoPatrimonio(String codigoPatrimonio) {
        this.codigoPatrimonio = codigoPatrimonio;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}

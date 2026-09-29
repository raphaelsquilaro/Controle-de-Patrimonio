package sp.senai.org.controle_de_almoxarifado.model;

import jakarta.persistence.*;

@Entity
public class TagRFID {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoTag;

    private Boolean ativo;

    @OneToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoTag() {
        return codigoTag;
    }

    public void setCodigoTag(String codigoTag) {
        this.codigoTag = codigoTag;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}

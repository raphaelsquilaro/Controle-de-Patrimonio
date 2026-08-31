package sp.senai.org.controle_de_almoxarifado.DTO.Request;

import sp.senai.org.controle_de_almoxarifado.model.enums.TipoMovimentacao;

public class MovimentacaoRequest {

    private Long produtoId;
    private Long usuarioId;
    private TipoMovimentacao tipo;
    private Integer quantidade;

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
package sp.senai.org.controle_de_almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sp.senai.org.controle_de_almoxarifado.model.Movimentacao;

public interface MovimentacaoRepository
        extends JpaRepository<Movimentacao, Long> {
}
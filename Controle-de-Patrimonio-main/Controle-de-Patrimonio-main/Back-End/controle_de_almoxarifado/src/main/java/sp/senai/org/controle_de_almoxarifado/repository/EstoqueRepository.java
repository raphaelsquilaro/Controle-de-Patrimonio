package sp.senai.org.controle_de_almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sp.senai.org.controle_de_almoxarifado.model.Estoque;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProdutoId(Long produtoId);
}

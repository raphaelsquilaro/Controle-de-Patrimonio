package sp.senai.org.controle_de_almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sp.senai.org.controle_de_almoxarifado.model.Produto;

import java.util.Optional;

public interface ProdutoRepository extends
        JpaRepository<Produto, Long> {

}

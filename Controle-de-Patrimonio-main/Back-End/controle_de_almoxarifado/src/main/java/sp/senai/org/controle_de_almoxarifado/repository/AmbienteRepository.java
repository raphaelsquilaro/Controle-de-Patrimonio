package sp.senai.org.controle_de_almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sp.senai.org.controle_de_almoxarifado.model.Ambiente;

public interface AmbienteRepository
        extends JpaRepository<Ambiente, Long> {

}

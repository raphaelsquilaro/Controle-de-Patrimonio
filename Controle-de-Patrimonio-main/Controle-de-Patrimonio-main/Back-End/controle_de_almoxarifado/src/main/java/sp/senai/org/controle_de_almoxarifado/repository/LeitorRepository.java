package sp.senai.org.controle_de_almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sp.senai.org.controle_de_almoxarifado.model.LeitorRFID;

public interface LeitorRepository extends JpaRepository<LeitorRFID, Long> {

}

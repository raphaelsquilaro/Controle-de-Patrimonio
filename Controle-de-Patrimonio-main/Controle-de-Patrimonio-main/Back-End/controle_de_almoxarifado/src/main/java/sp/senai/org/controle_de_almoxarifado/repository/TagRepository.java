package sp.senai.org.controle_de_almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sp.senai.org.controle_de_almoxarifado.model.TagRFID;

import java.util.Optional;

public interface TagRepository
        extends JpaRepository<TagRFID, Long> {

    Optional<TagRFID> findByCodigoTag(String codigoTag);
}

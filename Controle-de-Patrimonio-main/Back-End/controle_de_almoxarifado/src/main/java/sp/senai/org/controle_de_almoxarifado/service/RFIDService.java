package sp.senai.org.controle_de_almoxarifado.service;

import org.springframework.stereotype.Service;
import sp.senai.org.controle_de_almoxarifado.DTO.Request.RFIDRequest;
import sp.senai.org.controle_de_almoxarifado.model.Produto;
import sp.senai.org.controle_de_almoxarifado.model.TagRFID;
import sp.senai.org.controle_de_almoxarifado.repository.TagRepository;

@Service
public class RFIDService {

    private final TagRepository tagRepository;

    public RFIDService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Produto buscarProdutoPorTag(RFIDRequest request) {

        String uid = request.getUid().toUpperCase();

        TagRFID tag = tagRepository
                .findByCodigoTag(uid)
                .orElseThrow(() ->
                        new RuntimeException("Tag RFID não encontrada")
                );

        if (Boolean.FALSE.equals(tag.getAtivo())) {
            throw new RuntimeException("Esta tag RFID está invativa");
        }

        if (tag.getProduto() == null) {
            throw new RuntimeException(
                    "Esta tag não está vinculada a nenhum produto"
            );
        }

        return tag.getProduto();
    }
}

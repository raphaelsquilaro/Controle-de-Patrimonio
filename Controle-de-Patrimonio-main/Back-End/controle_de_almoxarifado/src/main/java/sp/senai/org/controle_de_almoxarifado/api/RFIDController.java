package sp.senai.org.controle_de_almoxarifado.api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sp.senai.org.controle_de_almoxarifado.DTO.Request.RFIDRequest;
import sp.senai.org.controle_de_almoxarifado.model.Produto;
import sp.senai.org.controle_de_almoxarifado.service.RFIDService;

import java.util.Map;

@RestController
@RequestMapping("/api/rfid")
public class RFIDController {

    private final RFIDService rfidService;

    public RFIDController(RFIDService rfidService) {
        this.rfidService = rfidService;
    }

    @PostMapping("/leitura")
    public ResponseEntity<?> receberLeitura(
            @RequestBody RFIDRequest request
    ) {
        try {
            Produto produto =
                    rfidService.buscarProdutoPorTag(request);

            return ResponseEntity.ok(
                    Map.of(
                            "sucesso", true,
                            "mensagem", "Produto identificado",
                            "produtoId", produto.getId(),
                            "nome", produto.getNome(),
                            "categoria", produto.getCategoria()
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "sucesso", false,
                            "mensagem", e.getMessage()
                    )
            );
        }
    }

    @GetMapping("/leitura")
    public ResponseEntity<?> testarLeitura() {
        return ResponseEntity.ok(
                "Endpoint POST /api/rfid/leitura está funcionando!"
        );
    }

    @GetMapping
    public ResponseEntity<?> testar() {

        return ResponseEntity.ok(
                "API RFID está funcionando!"
        );

    }

}
